package com.example.unsplash.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "UImages")
data class UImages(

    @PrimaryKey(autoGenerate = true) val idRoom: Int,
    @SerializedName("id") val id: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("promoted_at") val promoted_at: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?,
    @SerializedName("color") val color: String?,
    @SerializedName("blur_hash") val blur_hash: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("alt_description") val alt_description: String?,
    @SerializedName("likes") val likes: Int,


    ) {
    @Ignore
    @SerializedName("urls")
    var urls: IUrls? = null
}
