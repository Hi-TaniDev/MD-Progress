package com.example.hitani.data

import androidx.lifecycle.LiveData

class PlantRepository(private val plantReportDao: PlantReportDao) {
    val readAllData: LiveData<List<PlantReport>> = plantReportDao.readAllData()

    suspend fun addLaporan(plantReport: PlantReport){
        plantReportDao.addPlantReport(plantReport)
    }

    suspend fun deleteLaporan(plantReport: PlantReport){
        plantReportDao.deleteLaporan(plantReport)
    }
}