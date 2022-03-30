package com.mostafan3ma.android.pcm_helper10.lines.operations

import androidx.lifecycle.*
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import com.mostafan3ma.android.pcm_helper10.lines.LinesViewModel
import kotlinx.coroutines.launch

class AddLineViewModel(private val repository: PipeLinesRepository):ViewModel() {

    val name=MutableLiveData<String>()
    val ogm=MutableLiveData<String>()
    val type=MutableLiveData<String>()
    val length=MutableLiveData<String>()
    val work_date=MutableLiveData<String>()
    val i_start=MutableLiveData<String>()
    val startPoint=MutableLiveData<String>()


    private val _navigateToDetails=MutableLiveData<PipeLine?>()
    val navigateToDetails:LiveData<PipeLine?> get() = _navigateToDetails







    init {
        name.value=""
        ogm.value=""
        work_date.value=""
        length.value=""
        type.value=""
        i_start.value=""
        startPoint.value=""
        _navigateToDetails.value=null
    }

    fun addLineToDatabaseAndNavigateToDetails(){
        viewModelScope.launch {
            repository.insertLine(
                PipeLine(
                    name = name.value,
                    ogm = ogm.value,
                    type = type.value,
                    length = length.value,
                    work_date = work_date.value,
                    start_point = startPoint.value,
                    i_start = i_start.value,
                    end_point = "",
                    i_end = "",
                    points = mutableListOf()
                )
            )

        val lastPipe: PipeLine? =repository.getLastPipeLine()
        _navigateToDetails.value=lastPipe


        }
    }
    fun navigateToDetailsComplete(){
        _navigateToDetails.value=null
    }




    @Suppress("UNCHECKED_CAST")
    class AddLineViewModelFactory(
        private val repository: PipeLinesRepository
        ):ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>)
        = (AddLineViewModel(repository) as T)
    }




}