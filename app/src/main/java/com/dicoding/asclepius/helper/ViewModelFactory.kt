package com.dicoding.asclepius.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.ClassificationResultRepository
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.view.HistoryViewModel
import com.dicoding.asclepius.view.MainViewModel
import com.dicoding.asclepius.view.ResultViewModel

class ViewModelFactory private constructor(private val eventRepository: ClassificationResultRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(eventRepository) as T
        }
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(eventRepository) as T
        }
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)

//        return super.create(modelClass)
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { INSTANCE = it }
    }
}