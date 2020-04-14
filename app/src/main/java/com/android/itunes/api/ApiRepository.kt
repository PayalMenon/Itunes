package com.android.itunes.api

import com.android.itunes.model.Albums
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepository @Inject constructor(val apiService: ApiService) {

    suspend fun fetchAlbums(query: String): NetworkResult<Albums> {
        return withContext(IO) {
            val response = apiService.fetchAlbums(query)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    NetworkResult.Success(body)
                } ?: NetworkResult.Failure(response)
            } else {
                NetworkResult.Failure(response)
            }
        }
    }
}