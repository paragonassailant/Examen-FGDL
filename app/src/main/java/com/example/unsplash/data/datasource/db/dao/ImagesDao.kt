
package com.example.unsplash.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.unsplash.data.entities.IUrls
import com.example.unsplash.data.entities.UImageWithUrls
import com.example.unsplash.data.entities.UImages

@Dao
interface ImagesDao {

    @Transaction
    @Query("SELECT * FROM UImages")
    fun getImagesWithUrls(): List<UImageWithUrls>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: IUrls)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: UImages)

    @Query("DELETE FROM UImages")
    fun truncate()

    @Query("DELETE FROM Urls")
    fun truncateUrl()

    @Transaction
    fun insertAllData(uImages: UImages) {
        insert(uImages)
        insert(uImages.urls!!)
    }
}
