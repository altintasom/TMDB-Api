package com.altintasomer.etscase.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altintasomer.etscase.R
import com.altintasomer.etscase.model.RepoImp
import com.altintasomer.etscase.model.network.Result
import com.altintasomer.etscase.utils.Event
import com.altintasomer.etscase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val context: Context, private val repoImp: RepoImp) : ViewModel() {


     private val _results = MutableLiveData<Event<Resource<List<Result>>>>()
     val results get() = _results
     val searchQuery = MutableStateFlow("")
     private val oldResultList = arrayListOf<Result>()


     fun getFilm(searchQuery : String?){
          viewModelScope.launch(Dispatchers.IO) {
               _results.postValue(Event(Resource.loading()))
               val response = try {
                    repoImp.getFilms(searchQuery)
               }catch (e : Exception){
                    e.printStackTrace()
                    _results.postValue(Event(Resource.error(context.getString(R.string.error_connection))))
                    return@launch
               }


               if (response.isSuccessful){
                    val resultList = response.body()?.results
                    resultList?.let {
                         oldResultList.addAll(it)

                    }
                    _results.postValue(Event(Resource.success(oldResultList)))
               }else{
                    _results.postValue(Event(Resource.error(context.getString(R.string.error))))
               }

          }

     }



}