package com.example.unsplash.data.datasource.db.declaration

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplash.data.datasource.db.dao.ImagesDao
import com.example.unsplash.data.entities.IUrls
import com.example.unsplash.data.entities.UImages


@Database(entities = [UImages::class,IUrls::class], version = 1, exportSchema = false)
abstract class AppDataBase:RoomDatabase() {

    abstract fun getImages():ImagesDao
}