package com.ctr.homestaybooking.controller.place.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by at-trinhnguyen2 on 2020/12/09
 */

data class RecommendResponse(
        @SerializedName("body") val recommends: List<Recommend>,
        @SerializedName("length") val length: Int
)

data class Recommend(
        @SerializedName("place_id") val placeId: Int,
        @SerializedName("rating") val rating: Double
)
