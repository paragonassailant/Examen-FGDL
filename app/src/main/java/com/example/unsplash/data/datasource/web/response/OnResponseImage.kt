package com.example.unsplash.data.datasource.web.response

import com.example.unsplash.data.entities.UImages
import com.google.gson.annotations.SerializedName


data class OnResponseImage(

    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("results") val result: List<UImages>
)