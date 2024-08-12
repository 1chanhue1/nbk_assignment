package com.chanhue.nbk_assignment


import retrofit2.http.GET
import retrofit2.http.Query



interface KakaoApi {

    @GET("v2/search/image")
    suspend fun searchImages(
        @Query("query") query: String
    ): KakaoResponse
}

