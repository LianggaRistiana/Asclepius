package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.ClassificationResultEntity

@Dao
interface ClassificationResultDao {
    @Query("SELECT * FROM classification_result")
    fun getAllResult(): LiveData<List<ClassificationResultEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResult(classificationResult: ClassificationResultEntity)

}