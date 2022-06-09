package com.example.hitani.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PlantReportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlantReport(plantReport: PlantReport)

    @Query("SELECT * FROM laporan ORDER BY id ASC")
    fun readAllData(): LiveData<List<PlantReport>>

    @Delete
    suspend fun deleteLaporan(plantReport: PlantReport)
}