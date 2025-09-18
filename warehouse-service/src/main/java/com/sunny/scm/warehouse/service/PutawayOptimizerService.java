package com.sunny.scm.warehouse.service;

import com.sunny.scm.warehouse.dto.suggest.PutawaySuggestionDto;

import java.util.List;

public interface PutawayOptimizerService {
    List<PutawaySuggestionDto> optimizePutawaySuggestions(Long warehouseId, Long groupReceiptId);
}
