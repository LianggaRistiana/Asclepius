package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.ClassificationResultRepository

class ResultViewModel(private val classificationResultRepository: ClassificationResultRepository) :
    ViewModel() {
    fun getNews() = classificationResultRepository.getNews()
}