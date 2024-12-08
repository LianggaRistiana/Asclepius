package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.ClassificationResultEntity


@Database(entities = [ClassificationResultEntity::class], version = 1, exportSchema = false)
abstract class ClassificationResultDatabase : RoomDatabase() {
    abstract fun resultDao(): ClassificationResultDao

    companion object {
        @Volatile
        private var INSTANCE: ClassificationResultDatabase? = null

        fun getInstance(context: Context): ClassificationResultDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ClassificationResultDatabase::class.java,
                    "ClassificationResult.db"
                ).build().also { INSTANCE = it }
            }
    }
}