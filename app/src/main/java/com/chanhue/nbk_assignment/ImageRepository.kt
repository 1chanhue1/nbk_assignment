package com.chanhue.nbk_assignment

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository(private val context: Context) {

    private val kakaoApi = NetworkClient.apiService
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun searchImages(query: String): List<Image> {
        return withContext(Dispatchers.IO) {
            val response = kakaoApi.searchImages(query)

            Log.d("API 응답값 확인", "$response")

            response.documents.map { document ->
                Image(
                    thumbnailUrl = document.thumbnail_url,
                    displaySitename = document.display_sitename,
                    datetime = document.datetime
                )
            }
        }
    }

    fun saveLastQuery(query: String) {
        sharedPreferences.edit().putString("last_search", query).apply()
    }

    fun getLastQuery(): String? {
        return sharedPreferences.getString("last_search", "")
    }
}
