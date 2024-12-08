package com.dicoding.asclepius.view

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.ClassificationResultRepository
import com.dicoding.asclepius.data.local.entity.ClassificationResultEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val classificationResultRepository: ClassificationResultRepository) :
    ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri
    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun getURI() = imageUri.value

    //    fun getAllResult() = classificationResultRepository.getAllResult()
    fun insertResult(imageUri: String, result: String, confidence: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            classificationResultRepository.insertResult(
                ClassificationResultEntity(
                    imageUri = imageUri,
                    result = result,
                    confidence = confidence
                )
            )
        }

    }
}