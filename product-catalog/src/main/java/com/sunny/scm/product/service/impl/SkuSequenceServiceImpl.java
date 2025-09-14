package com.sunny.scm.product.service.impl;

import com.sunny.scm.product.entity.SkuSequence;
import com.sunny.scm.product.repository.SkuSequenceRepository;
import com.sunny.scm.product.service.SkuSequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkuSequenceServiceImpl implements SkuSequenceService {
    private final SkuSequenceRepository skuSequenceRepository;
    @Override
    public String generateSku(Long companyId) {
        SkuSequence sequence = skuSequenceRepository.findByCompanyId(companyId)
                .orElseGet(() -> {
                    SkuSequence newSeq = new SkuSequence();
                    newSeq.setCompanyId(companyId);
                    newSeq.setLastValue(0L);
                    return skuSequenceRepository.save(newSeq);
                });

        Long nextValue = sequence.getLastValue() + 1;
        sequence.setLastValue(nextValue);
        skuSequenceRepository.save(sequence);

        return "SKU-C" + companyId + "-" + String.format("%06d", nextValue);
    }
}
