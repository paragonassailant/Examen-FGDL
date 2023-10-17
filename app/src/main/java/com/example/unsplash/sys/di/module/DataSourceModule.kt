package com.example.unsplash.sys.di.module


import com.example.unsplash.data.datasource.db.dao.ImagesDao
import com.example.unsplash.data.datasource.db.declaration.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [DataBaseModule::class])
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    //MEMORY DS


    //DISK DS O DAO
    @Provides
    @Singleton
    fun provideArticle(appDataBase: AppDataBase): ImagesDao {
        return appDataBase.getImages()
    }

}