package com.android.itunes.api

import com.android.itunes.model.Albums
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService  {

    @GET("/search?media=music")
    suspend fun fetchAlbums(@Query("term") value : String) : Response<Albums>
}