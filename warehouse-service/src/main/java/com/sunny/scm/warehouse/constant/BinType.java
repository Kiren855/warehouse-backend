package com.sunny.scm.warehouse.constant;

public enum BinType {
    STANDARD,       // Bin chuẩn, chứa hàng thông thường
    PALLET,         // Chứa pallet
    SHELF,          // Kệ để hàng lẻ
    DRAWER,         // Ngăn kéo, thường cho linh kiện nhỏ
    COLD_STORAGE,   // Bin trong khu lạnh (mát, đông lạnh)
    BULK,           // Chứa hàng cồng kềnh, số lượng lớn
    FRAGILE,        // Dành cho hàng dễ vỡ
    HAZARDOUS,      // Dành cho hàng nguy hiểm
    HEAVY           // Dành cho hàng nặng
}
