package com.chanhue.nbk_assignment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    val query = MutableLiveData<String>()

    private val _images = MutableLiveData<List<Image>?>()
    val images: MutableLiveData<List<Image>?> get() = _images

    private val _favoriteImages = MutableLiveData<List<Image>>()
    val favoriteImages: LiveData<List<Image>> get() = _favoriteImages

    init {
        query.value = repository.getLastQuery()
        _favoriteImages.value = emptyList()
    }

    fun searchImages() {


        val currentQuery = this.query.value ?: return
        repository.saveLastQuery(currentQuery)

        viewModelScope.launch {
            try {

                val result = repository.searchImages(currentQuery).take(80)
                val formattedResult = result.map { image ->
                    image.copy(datetime = formatDateTime(image.datetime))

                }
                Log.d("뷰모델", "검색결과 호출 확인 $formattedResult")


                _images.postValue(formattedResult)
            } catch (e: Exception) {

                Log.e("뷰모델", "에러코드", e)
                _images.postValue(emptyList())
            }
        }
    }

    private fun formatDateTime(datetime: String): String {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return try {
            val date = inputFormat.parse(datetime)
            outputFormat.format(date)
        } catch (e: Exception) {
            datetime
        }
    }

    fun toggleHeart(image: Image) {
        val updatedImages = _images.value?.map {
            if (it.thumbnailUrl == image.thumbnailUrl) it.copy(isLiked = !it.isLiked) else it
        }
        _images.value = updatedImages

        val updatedImage = updatedImages?.find { it.thumbnailUrl == image.thumbnailUrl }
        val currentFavorites = _favoriteImages.value ?: listOf()

        if (updatedImage?.isLiked == true) {
            _favoriteImages.value = currentFavorites + updatedImage
        } else {
            _favoriteImages.value = currentFavorites.filter { it.thumbnailUrl != image.thumbnailUrl }
        }
    }


    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ImageViewModel(ImageRepository(context)) as T
            }
            throw IllegalArgumentException("뷰모델 오류 예외 발생")
        }
    }
}
