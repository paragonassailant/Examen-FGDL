package com.example.unsplash.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Urls")
data class IUrls(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("full") val full: String,
    @SerializedName("thumb") val thumb: String
)
