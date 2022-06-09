package com.example.hitani.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PlantReport::class], version = 1, exportSchema = false)
abstract class PlantReportDatabase : RoomDatabase(){

    abstract fun laporan(): PlantReportDao

    companion object{
        @Volatile
        private var INSTANCE: PlantReportDatabase? = null
        fun getDb(context: Context): PlantReportDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, PlantReportDatabase::class.java, "laporan"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}