package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "classification_result")
@Parcelize
data class ClassificationResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "image_uri")
    val imageUri: String,

    @ColumnInfo(name = "result")
    val result: String,

    @ColumnInfo(name = "confidence")
    val confidence: Float,
) : Parcelable