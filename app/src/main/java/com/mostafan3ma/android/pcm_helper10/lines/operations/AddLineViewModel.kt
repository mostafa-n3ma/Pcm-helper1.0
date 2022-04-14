package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import androidx.lifecycle.*
import com.mostafan3ma.android.pcm_helper10.Utils.CoordinateConversion
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import com.mostafan3ma.android.pcm_helper10.lines.LinesViewModel
import kotlinx.coroutines.launch

class AddLineViewModel(private val repository: PipeLinesRepository) : ViewModel() {
   private lateinit var converter:CoordinateConversion



    val name = MutableLiveData<String>()
    val ogm = MutableLiveData<String>()
    val type = MutableLiveData<String>()
    val length = MutableLiveData<String>()
    val work_date = MutableLiveData<String>()
    val i_start = MutableLiveData<String>()
    val startPoint = MutableLiveData<String>()
    val work_team=MutableLiveData<String>()
    val input=MutableLiveData<String>()
    val extra_note=MutableLiveData<String>()



    val accuracy=MutableLiveData<String>()



    val progressVisibility=MutableLiveData<Int>()

    fun getGpsLabels(x: Double,y:Double, accuracy: String) {
        val gpsX:String= converter.latLon2UTM_x(x,y)
        val gpsY:String= converter.latLon2UTM_y(x,y)
        startPoint.value = "$gpsX;$gpsY"
        this.accuracy.value="accuracy: $accuracy"
        progressVisibility.value=View.GONE
    }


    private val _navigateToDetails = MutableLiveData<PipeLine?>()
    val navigateToDetails: LiveData<PipeLine?> get() = _navigateToDetails


    init {
        converter=CoordinateConversion()
        name.value = ""
        ogm.value = ""
        work_date.value = ""
        length.value = ""
        type.value = ""
        i_start.value = ""
        startPoint.value = ""
        work_team.value=""
        input.value=""
        extra_note.value=""
        _navigateToDetails.value = null
        progressVisibility.value=View.VISIBLE
        accuracy.value=""

    }

    fun addLineToDatabaseAndNavigateToDetails() {
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
                    work_team=work_team.value,
                    input = input.value,
                    extra_note=extra_note.value,
                    points = mutableListOf()
                )
            )

            val lastPipe: PipeLine? = repository.getLastPipeLine()
            _navigateToDetails.value = lastPipe


        }
    }

    fun navigateToDetailsComplete() {
        _navigateToDetails.value = null
    }


    @Suppress("UNCHECKED_CAST")
    class AddLineViewModelFactory(
        private val repository: PipeLinesRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            (AddLineViewModel(repository) as T)
    }


}