package com.chanhue.nbk_assignment

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val apiKey = BuildConfig.KAKAO_API_KEY // 키 숨기기
            Log.d("테스트 키 ", "$apiKey")

            val request = chain.request().newBuilder()
                .addHeader("Authorization", "KakaoAK $apiKey")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: KakaoApi = retrofit.create(KakaoApi::class.java)
}
