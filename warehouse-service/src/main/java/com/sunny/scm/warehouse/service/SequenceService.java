package com.sunny.scm.warehouse.service;

public interface SequenceService {
    Long getNextSequence(String scopeType, Long scopeId);
}
