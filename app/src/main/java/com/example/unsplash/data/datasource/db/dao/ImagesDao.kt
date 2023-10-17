package com.example.unsplash.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.unsplash.data.entities.IUrls
import com.example.unsplash.data.entities.UImages

@Dao
abstract class ImagesDao {


    @Transaction
    @Query("SELECT * FROM UImages GROUP BY id")
    abstract fun getImage(): List<UImages>

    fun getImagesAndUrls(): List<UImages> {
        val images = getImage()
        for (u: UImages in images) {
            u.urls = getUrlsById(u.idRoom)
        }
        return images
    }

    @Query("SELECT DISTINCT *  FROM Urls WHERE id=:id")
    abstract fun getUrlsById(id: Int): IUrls


    @Query("DELETE FROM UImages")
    abstract fun truncate()

    @Query("DELETE FROM urls")
    abstract fun truncateUrl()


    fun insertAllData(uImages: UImages) {
        insert(uImages)
        insert(uImages.urls!!)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: IUrls)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: UImages)

}