package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.ClassificationResultRepository

class HistoryViewModel(private val classificationResultRepository: ClassificationResultRepository) :
    ViewModel() {


    fun getAllResult() = classificationResultRepository.getAllResult()
}