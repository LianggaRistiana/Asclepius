package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.ClassificationResultRepository
import com.dicoding.asclepius.data.local.room.ClassificationResultDatabase
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): ClassificationResultRepository {
        val apiService = ApiConfig.getApiService()
        val database = ClassificationResultDatabase.getInstance(context)
        val dao = database.resultDao()
        return ClassificationResultRepository.getInstance(dao, apiService)
    }
}