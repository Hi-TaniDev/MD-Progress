package com.example.hitani.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hitani.data.PlantReport
import com.example.hitani.data.PlantReportDatabase
import com.example.hitani.data.PlantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlantReportViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<PlantReport>>
    private val repo: PlantRepository

    init {
        val plantReportDao = PlantReportDatabase.getDb(application).laporan()
        repo = PlantRepository(plantReportDao)
        readAllData = repo.readAllData
    }

    fun addLaporan(plantReport: PlantReport){
        viewModelScope.launch(Dispatchers.IO){
            repo.addLaporan(plantReport)
        }
    }

    fun deleteLaporan(plantReport: PlantReport){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteLaporan(plantReport)
        }
    }
}