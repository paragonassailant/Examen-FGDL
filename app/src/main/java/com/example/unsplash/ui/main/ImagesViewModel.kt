package com.example.unsplash.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplash.data.entities.UImages
import com.example.unsplash.domain.ImageRepository
import com.example.unsplash.sys.util.Constants.Companion.API_KEY
import com.example.unsplash.sys.util.Constants.Companion.PER_PAGE
import com.example.unsplash.sys.util.ErrorObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImagesViewModel @Inject constructor(private val repository: ImageRepository) :ViewModel() {

    val onSuccess = MutableLiveData<List<UImages>>()
    val onError by lazy { MutableLiveData<ErrorObserver>() }

    fun getImages(query:String,page:Int){
        repository.requestImages(query,API_KEY,page, PER_PAGE, buildResponse1(),buildError())
    }

    private fun buildResponse1(): Observer<List<UImages>> {
        return Observer {
            if (it.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    onSuccess.postValue(it)
                }
            }
        }
    }

    private fun buildError(): Observer<ErrorObserver> {
        return Observer {
            onError.postValue(it)
        }
    }
    fun getImagesPaging(query: String): Flow<PagingData<UImages>> {
        return repository.getImagesPaging(query, API_KEY).cachedIn(viewModelScope)
    }
}