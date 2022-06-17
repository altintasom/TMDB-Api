package com.altintasomer.etscase.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altintasomer.etscase.model.RepoImp
import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.utils.Event
import com.altintasomer.etscase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "DetailViewModel"
@HiltViewModel
class DetailViewModel @Inject constructor(
   private val repoImp: RepoImp
)  : ViewModel(){

    private val _filmDetail = MutableLiveData<Event<Resource<FilmDetail>>>()
    val filmDetail get() = _filmDetail

    fun getFilmDetail(id : String){

        viewModelScope.launch(Dispatchers.IO) {

            _filmDetail.postValue(Event(Resource.loading()))

            val response = try {
                repoImp.getFilmDetail(id)
            }catch (e: Exception){
                e.printStackTrace()
                return@launch
            }

            if (response.isSuccessful){
                Log.d(TAG, "getFilmDetail: ${response.body()}")
                _filmDetail.postValue(Event(Resource.success(response.body())))
            }else{
                Log.d(TAG, "getFilmDetail: error")
                _filmDetail.postValue(Event(Resource.error("Error")))
            }
        }
    }
}