package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.grpc.product.ProductPackageRpc;
import com.sunny.scm.warehouse.client.ProductCatalogClient;
import com.sunny.scm.warehouse.constant.PutawayReservationStatus;
import com.sunny.scm.warehouse.dto.receipt.CreateGoodReceiptRequest;
import com.sunny.scm.warehouse.dto.receipt.GroupedPackageDto;
import com.sunny.scm.warehouse.dto.receipt.ProductPackageDto;
import com.sunny.scm.warehouse.dto.receipt.ProductPackageRequest;
import com.sunny.scm.warehouse.dto.suggest.PutawaySuggestionDto;
import com.sunny.scm.warehouse.entity.Bin;
import com.sunny.scm.warehouse.entity.PutawayReservation;
import com.sunny.scm.warehouse.repository.BinRepository;
import com.sunny.scm.warehouse.repository.GoodReceiptItemRepository;
import com.sunny.scm.warehouse.repository.GroupReceiptRepository;
import com.sunny.scm.warehouse.repository.PutawayReservationRepository;
import com.sunny.scm.warehouse.service.GoodReceiptService;
import com.sunny.scm.warehouse.service.PutawayOptimizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PutawayOptimizerServiceImpl implements PutawayOptimizerService {

    private final BinRepository binRepository;
    private final GoodReceiptItemRepository goodReceiptItemRepository;
    private final ProductCatalogClient productCatalogClient;
    private final PutawayReservationRepository putawayReservationRepository;
    private final GoodReceiptService goodReceiptService;

    @Override
    @Transactional
    public List<PutawaySuggestionDto> optimizePutawaySuggestions(Long warehouseId, Long groupReceiptId) {
        CreateGoodReceiptRequest createGoodReceiptRequest = CreateGoodReceiptRequest.builder()
                .productPackages(new ArrayList<>()).build();

         // 1. Lấy grouped products
        List<GroupedPackageDto> groupedPackageDto = goodReceiptItemRepository.findGroupedItemsByGroupReceiptId(groupReceiptId);

        List<ProductPackageRpc> list = productCatalogClient.getProductPackages(groupedPackageDto);
        List<ProductPackageDto> products = list.stream()
                .map(ProductPackageDto::fromRpc)
                .sorted(Comparator.comparing(ProductPackageDto::getVolume).reversed())
                .toList();
        log.info("1. Fetched {} grouped products for GroupReceipt ID: {}", products.size(), groupReceiptId);

        // 2. Preload reservation với PESSIMISTIC_WRITE lock
        Map<Long, List<PutawayReservation>> binReservationsMap = putawayReservationRepository
                .findAllByWarehouseWithLock(warehouseId, PutawayReservationStatus.PENDING)
                .stream()
                .collect(Collectors.groupingBy(r -> r.getBin().getId()));

        // 3. Lấy tất cả bin trong warehouse
        List<Bin> candidateBins = binRepository.findAllByWarehouseId(warehouseId);

        List<PutawaySuggestionDto> suggestions = new ArrayList<>();

        for (ProductPackageDto product : products) {
            int remainingQuantity = product.getTotalQuantity();

            while (remainingQuantity > 0) {
                Bin bestBin = findBestFitBin(candidateBins, binReservationsMap, product, remainingQuantity);

                if (bestBin == null) {
                    ProductPackageRequest request = ProductPackageRequest.builder()
                            .productPackageId(product.getPackageId())
                            .packageQuantity(product.getTotalQuantity())
                            .build();
                    createGoodReceiptRequest.getProductPackages().add(request);
                    break;
                }

                int quantityToPutaway = calculateMaxQuantityInBin(bestBin, binReservationsMap.getOrDefault(bestBin.getId(), Collections.emptyList()), product, remainingQuantity);

                // Tạo reservation
                PutawayReservation reservation = PutawayReservation.builder()
                        .putawayGroupId(groupReceiptId)
                        .productPackageId(product.getPackageId())
                        .bin(bestBin)
                        .quantityReserved(quantityToPutaway)
                        .status(PutawayReservationStatus.PENDING)
                        .build();
                putawayReservationRepository.save(reservation);

                // Cập nhật map để tính toán các lần lặp tiếp theo
                binReservationsMap.computeIfAbsent(bestBin.getId(), k -> new ArrayList<>()).add(reservation);

                // Tạo suggestion DTO
                suggestions.add(PutawaySuggestionDto.builder()
                        .putawayGroupId(groupReceiptId)
                        .productPackageId(product.getPackageId())
                        .suggestedBinId(bestBin.getId())
                                .suggestedBinName(bestBin.getBinName())
                                .suggestedBinCode(bestBin.getBinCode())
                        .suggestedZoneId(bestBin.getZone().getId())
                                .suggestedZoneName(bestBin.getZone().getZoneName())
                                .suggestedZoneCode(bestBin.getZone().getZoneCode())
                        .quantityToPutaway(quantityToPutaway)
                        .productSku(product.getProductSku())
                        .productName(product.getProductName())
                        .packageBarcode(product.getBarcode())
                        .build());

                remainingQuantity -= quantityToPutaway;
            }
        }

        goodReceiptService.createGoodReceiptManual(warehouseId, createGoodReceiptRequest);
        return suggestions;
    }

    private Bin findBestFitBin(List<Bin> candidateBins, Map<Long, List<PutawayReservation>> binReservationsMap,
                               ProductPackageDto product, int quantityToPutaway) {
        List<Bin> filtered = candidateBins.stream()
                .filter(bin -> bin.getLength().compareTo(product.getLength()) >= 0
                        && bin.getWidth().compareTo(product.getWidth()) >= 0
                        && bin.getHeight().compareTo(product.getHeight()) >= 0)
                .filter(bin -> isBinSuitable(bin, binReservationsMap.getOrDefault(bin.getId(), Collections.emptyList()), product, quantityToPutaway))
                .sorted(Comparator.comparing(bin -> {
                    BigDecimal remainingVolume = bin.getMaxVolume().subtract(bin.getCurrentVolumeUsed());
                    List<PutawayReservation> reservations = binReservationsMap.getOrDefault(bin.getId(), Collections.emptyList());
                    BigDecimal reservedVolume = reservations.stream()
                            .map(r -> BigDecimal.valueOf(r.getQuantityReserved()).multiply(product.getVolume()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return remainingVolume.subtract(reservedVolume);
                }))
                .toList();

        return filtered.isEmpty() ? null : filtered.get(0);
    }

    private boolean isBinSuitable(Bin bin, List<PutawayReservation> existingReservations, ProductPackageDto product, int quantityToPutaway) {
        if (existingReservations.isEmpty() && bin.getCurrentVolumeUsed().compareTo(BigDecimal.ZERO) == 0) {
            return bin.getLength().compareTo(product.getLength()) >= 0
                    && bin.getWidth().compareTo(product.getWidth()) >= 0
                    && bin.getHeight().compareTo(product.getHeight()) >= 0;
        }

        if (existingReservations.stream().anyMatch(r -> !r.getProductPackageId().equals(product.getPackageId()))) {
            return false;
        }

        BigDecimal reservedVolume = existingReservations.stream()
                .map(r -> BigDecimal.valueOf(r.getQuantityReserved()).multiply(product.getVolume()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal reservedWeight = existingReservations.stream()
                .map(r -> BigDecimal.valueOf(r.getQuantityReserved()).multiply(product.getWeight()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingVolume = bin.getMaxVolume().subtract(bin.getCurrentVolumeUsed()).subtract(reservedVolume);
        BigDecimal remainingWeight = bin.getMaxWeight().subtract(bin.getCurrentWeightUsed()).subtract(reservedWeight);

        BigDecimal neededVolume = product.getVolume().multiply(BigDecimal.valueOf(quantityToPutaway));
        BigDecimal neededWeight = product.getWeight().multiply(BigDecimal.valueOf(quantityToPutaway));

        return remainingVolume.compareTo(neededVolume) >= 0 && remainingWeight.compareTo(neededWeight) >= 0;
    }

    private int calculateMaxQuantityInBin(Bin bin, List<PutawayReservation> existingReservations,
                                          ProductPackageDto product, int remainingQuantity) {
        BigDecimal reservedVolume = existingReservations.stream()
                .map(r -> BigDecimal.valueOf(r.getQuantityReserved()).multiply(product.getVolume()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal reservedWeight = existingReservations.stream()
                .map(r -> BigDecimal.valueOf(r.getQuantityReserved()).multiply(product.getWeight()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal availableVolume = bin.getMaxVolume().subtract(bin.getCurrentVolumeUsed()).subtract(reservedVolume);
        BigDecimal availableWeight = bin.getMaxWeight().subtract(bin.getCurrentWeightUsed()).subtract(reservedWeight);

        int maxByVolume = availableVolume.divide(product.getVolume(), BigDecimal.ROUND_DOWN).intValue();
        int maxByWeight = availableWeight.divide(product.getWeight(), BigDecimal.ROUND_DOWN).intValue();

        return Math.min(remainingQuantity, Math.min(maxByVolume, maxByWeight));
    }

}
