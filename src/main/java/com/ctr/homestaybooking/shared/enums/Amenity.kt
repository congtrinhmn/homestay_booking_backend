package com.ctr.homestaybooking.shared.enums

/**
 * Created by at-trinhnguyen2 on 2020/11/10
 */
enum class Amenity(var id: Int, var amenityName: String) {
    WIFI(1, "Wifi"),
    TV(2, "TV"),
    AIR_CONDITIONING(3, "Điều hòa"),
    REFRIGERATOR(4, "Tủ lạnh"),
    DRYER(5, "Máy sấy"),
    SHAMPOO(6, "Dầu gội, dầu xả"),
    TOILET_PAPER(7, "Giấy vệ sinh"),
    TOWEL(8, "Khăn tắm"),
    JACUZZI(9, "Bồn tắm"),
    TOOTHPASTE(10, "Kem đánh răng"),
    SOAP(12, "Xà phòng tắm"),
    INTERNET(12, "Internet"),
    TISSUE(13, "Khăn giấy"),
    BOTTLED_WATER(14, "Bình nước"),
    HANGER(15, "Móc treo quần áo"),
    CLOSET(16, "Tủ quần áo"),
    IRON(17, "Bàn ủi"),
    MIRROR(18, "Bàn trang điểm"),
    KITCHEN(19, "Nhà bếp"),
    MICROWAVE(20, "Lò vi sóng"),
    STOVE_ELECTRIC(21, "Bếp điện"),
    STOVE_GAS(22, "Bếp gas"),
    WASHER(23, "Máy giặc"),
    COFFEE_POT(24, "Máy pha cà phê"),
    KARAOKE(25, "Karaoke"),
    POOL(26, "Bể bơi"),
    DESK(27, "Bàn làm việc")
}
