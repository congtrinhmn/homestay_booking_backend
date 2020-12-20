package com.ctr.homestaybooking.shared.model

/**
 * Created by at-trinhnguyen2 on 2020/12/18
 */
enum class PagingHeaders(val pageName: String) {
    PAGE_SIZE("Page-Size"),
    PAGE_NUMBER("Page-Number"),
    PAGE_OFFSET("Page-Offset"),
    PAGE_TOTAL("Page-Total"),
    COUNT("Count");
}
