package com.sunny.scm.warehouse.helper;

public class EntityCodeGenerator {
    public static String generateWarehouseCode(Long companyId, Long sequence) {
        return String.format("WH-%s-%03d", companyId, sequence);
    }

    public static String generateZoneCode(String warehouseCode, Long sequence) {
        return String.format("%s-ZC-%03d", warehouseCode, sequence);
    }

    public static String generateBinCode(String zoneCode, Long sequence) {
        return String.format("%s-BC-%03d", zoneCode, sequence);
    }
}
