package com.sunny.scm.warehouse.constant;

public enum ZoneType {
    RECEIVING,   // Khu nhận hàng (inbound)
    STORAGE,     // Khu lưu trữ (chính)
    PICKING,     // Khu lấy hàng theo đơn
    PACKING,     // Khu đóng gói
    SHIPPING,    // Khu xuất hàng (outbound)
    RETURN,      // Khu xử lý hàng trả
    QC           // Khu kiểm tra chất lượng
}
