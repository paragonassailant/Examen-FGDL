package com.example.unsplash.ui.main.interfaces

import com.example.unsplash.data.entities.UImages

interface IAUImage {

    fun viewImage(item: UImages)
    fun details(item: UImages)
}