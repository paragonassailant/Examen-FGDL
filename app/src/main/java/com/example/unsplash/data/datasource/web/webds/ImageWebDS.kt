package com.example.unsplash.data.datasource.web.webds

import androidx.lifecycle.Observer
import com.example.unsplash.data.datasource.web.api.WebServices
import com.example.unsplash.data.datasource.web.response.OnResponseImage
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.sys.util.Constants.Companion.RETROFIT_FAILURE
import com.example.unsplash.sys.util.ErrorObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ImageWebDS @Inject constructor(private val webServices: WebServices) {

    fun requestImages(query:String, clientId:String,page:Int,per_page:Int, observer: Observer<List<UImages>>, error: Observer<ErrorObserver>){
        webServices.searchPhotos(query,clientId,page,per_page).enqueue(object : Callback<OnResponseImage> {
            override fun onResponse(call: Call<OnResponseImage>, response: Response<OnResponseImage>) {
                CoroutineScope(Dispatchers.IO).launch {
                    when(response.code()){
                        200->{
                            if (response.body()?.result != null){
                                observer.onChanged(response.body()!!.result)
                            }else{
                                error.onChanged(ErrorObserver(RETROFIT_FAILURE, response.message()))
                            }
                        }
                        400->{
                            error.onChanged(ErrorObserver(RETROFIT_FAILURE, response.message()))
                        }
                        404->{
                            error.onChanged(ErrorObserver(RETROFIT_FAILURE, response.message()))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OnResponseImage>, t: Throwable) {
                error.onChanged(ErrorObserver(RETROFIT_FAILURE, t.message))
            }

        })
    }

    fun getImages(query: String, clientId: String, page: Int, perPage: Int): List<UImages> {
        return try {
            val response = webServices.searchPhotos(query, clientId, page, perPage).execute()
            if (response.isSuccessful && response.body()?.result != null) {
                response.body()!!.result
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}