package com.example.unsplash.data.datasource.web.api

import com.example.unsplash.data.datasource.web.response.OnResponseImage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("/search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("client_id") client_id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Call<OnResponseImage>
}