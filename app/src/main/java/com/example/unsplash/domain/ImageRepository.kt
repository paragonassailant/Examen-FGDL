package com.example.unsplash.domain

import android.content.Context
import androidx.lifecycle.Observer
import com.biaani.couture.sgivpp.sys.util.Connection
import com.example.unsplash.data.datasource.db.dao.ImagesDao
import com.example.unsplash.data.datasource.web.webds.ImageWebDS
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.sys.util.Constants.Companion.RETROFIT_FAILURE
import com.example.unsplash.sys.util.ErrorObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ImageRepository @Inject constructor(val webDS: ImageWebDS, @ApplicationContext private var context: Context, private val dao: ImagesDao) {

    fun requestImages(query:String, clientId:String,per_page:Int, observer: Observer<List<UImages>>, error: Observer<ErrorObserver>){
        if (Connection.connection(context)){
            webDS.requestImages(query, clientId,per_page, buildRequest(observer,error), error)
            CoroutineScope(Dispatchers.IO).launch {
                dao.truncate()
            }
        }
    }

    private fun buildRequest(observer: Observer<List<UImages>>, error: Observer<ErrorObserver>): Observer<List<UImages>> {
        return Observer {
            if (it.isEmpty()){
                error.onChanged(ErrorObserver(RETROFIT_FAILURE))
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    for (i:UImages in it){
                        dao.insertAllData(i)
                    }
                    getLocalData(observer)
                }
            }
        }
    }

    private fun getLocalData(observer: Observer<List<UImages>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val images = dao.getImagesAndUrls()
            observer.onChanged(images)
        }
    }
}