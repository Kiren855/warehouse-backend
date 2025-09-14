package com.sunny.scm.warehouse.service.impl;

import com.sunny.scm.warehouse.entity.CodeSequence;
import com.sunny.scm.warehouse.repository.CodeSequenceRepository;
import com.sunny.scm.warehouse.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {
    private final CodeSequenceRepository codeSequenceRepository;
    @Override
    public Long getNextSequence(String scopeType, Long scopeId) {
        CodeSequence seq = codeSequenceRepository.findByScopeTypeAndScopeIdForUpdate(scopeType, scopeId)
                .orElseGet(() -> {
                    CodeSequence newSeq = CodeSequence.builder()
                            .scopeType(scopeType)
                            .scopeId(scopeId)
                            .nextVal(1L)
                            .build();
                    return codeSequenceRepository.save(newSeq);
                });

        Long next = seq.getNextVal();
        seq.setNextVal(next + 1);
        codeSequenceRepository.save(seq);
        return next;
    }
}
