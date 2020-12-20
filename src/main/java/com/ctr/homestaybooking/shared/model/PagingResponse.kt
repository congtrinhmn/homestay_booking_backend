package com.ctr.homestaybooking.shared.model

import com.ctr.homestaybooking.entity.PlaceEntity

/**
 * Created by at-trinhnguyen2 on 2020/12/18
 */
data class PagingResponse(
        /**
         * entity count
         */
        val count: Long? = null,

        /**
         * page number, 0 indicate the first page.
         */
        val pageNumber: Int? = null,

        /**
         * size of page, 0 indicate infinite-sized.
         */
        val pageSize: Int? = null,

        /**
         * Offset from the of pagination.
         */
        val pageOffset: Long? = null,

        /**
         * the number total of pages.
         */
        val pageTotal: Int? = null,

        /**
         * elements of page.
         */
        val elements: List<PlaceEntity>? = null
)
