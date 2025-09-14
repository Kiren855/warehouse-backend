package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.warehouse.entity.WarehouseSequence;
import com.sunny.scm.warehouse.repository.SequenceRepository;
import com.sunny.scm.warehouse.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {
    private final SequenceRepository sequenceRepository;
    @Override
    public long nextSequenceForCompany(Long companyId) {
        Optional<WarehouseSequence> opt = sequenceRepository.findByCompanyIdForUpdate(companyId);
        if (opt.isPresent()) {
            WarehouseSequence ws = opt.get();
            long next = ws.getCurrentValue() + 1;
            ws.setCurrentValue(next);
            sequenceRepository.save(ws);
            return next;
        } else {
            WarehouseSequence ws = new WarehouseSequence();
            ws.setCompanyId(companyId);
            ws.setCurrentValue(1L);
            sequenceRepository.save(ws);
            return 1L;
        }
    }
}
