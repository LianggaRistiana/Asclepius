package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.local.entity.ClassificationResultEntity
import com.dicoding.asclepius.data.local.room.ClassificationResultDao
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class ClassificationResultRepository private constructor(
    private val classificationResultDao: ClassificationResultDao,
    private val apiService: ApiService
) {
    fun getAllResult(): LiveData<List<ClassificationResultEntity>> {
        return classificationResultDao.getAllResult()
    }

    suspend fun insertResult(classificationResult: ClassificationResultEntity) {
        classificationResultDao.insertResult(classificationResult)
    }

    fun getNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(language = "en", apiKey = "844962855ded47d982cb19d4d84373dc", category = "health", query = "cancer")
            val articles = response.articles?.filterNotNull()
            if (articles != null) {
                emit(Result.Success(articles))
            } else {
                emit(Result.Error("No Data"))
            }

        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ClassificationResultRepository? = null

        fun getInstance(
            classificationResultDao: ClassificationResultDao,
            apiService: ApiService
        ): ClassificationResultRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ClassificationResultRepository(
                    classificationResultDao,
                    apiService
                ).also { INSTANCE = it }
            }
    }
}