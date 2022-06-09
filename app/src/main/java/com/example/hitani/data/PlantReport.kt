package com.example.hitani.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "laporan")
data class PlantReport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val imagePath: String,
    val laporanDiagnosis: String,
    val dateCreated: String
): Parcelable
