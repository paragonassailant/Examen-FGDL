package com.example.unsplash.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UImageWithUrls(
    @Embedded val image: UImages,
    @Relation(parentColumn = "idRoom", entityColumn = "id") val urls: IUrls?
)