package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import androidx.lifecycle.*
import com.mostafan3ma.android.pcm_helper10.Utils.CoordinateConversion
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.launch

class LineDetailsViewModel(private val repository: PipeLinesRepository) : ViewModel() {
    private lateinit var converter: CoordinateConversion

    lateinit var selectedLine: PipeLine
    fun getSelectedLine(selectedLine: PipeLine) {
        this.selectedLine = selectedLine
    }


    val dp = MutableLiveData<String>()
    val depth = MutableLiveData<String>()
    val current1 = MutableLiveData<String>()
    val current2 = MutableLiveData<String>()
    val gpsX = MediatorLiveData<String>()
    val gpsY = MediatorLiveData<String>()



    val gpsX_Y = MediatorLiveData<String>()
    val accuracy=MutableLiveData<String>()
    val progressVisibility=MutableLiveData<Int>()
    fun getGpsCoordinates(x:Double,y:Double,acc:String){
        gpsX.value=converter.latLon2UTM_x(x,y)
        gpsY.value=converter.latLon2UTM_y(x,y)
        accuracy.value="Accuracy: $acc"
        gpsX_Y.value="${gpsX.value} : ${gpsY.value}"
        progressVisibility.value=View.GONE
    }



    fun deletePoint(point: DamagePoint) {
        viewModelScope.launch {
            selectedLine.points.remove(point)
            var updatedList: MutableList<DamagePoint> = selectedLine.points
            updatedList.map {
                if (it.no > point.no) {
                    it.no = it.no - 1
                }
            }
            repository.updatePointsList(selectedLine.id, updatedList)
        }
    }

    private fun getNewPointInfo(): DamagePoint {
        return DamagePoint(
            selectedLine.points.size + 1, dp.value, depth.value, current1.value, current2.value,
            gpsX.value, gpsY.value
        )
    }


    fun addNewPointToPipeList() {
        selectedLine.points.add(getNewPointInfo())
        viewModelScope.launch {
            repository.updatePointsList(selectedLine.id, selectedLine.points)
        }
    }


    private val _closeBottomSheet = MutableLiveData<Boolean>()
    val closeBottomSheet: LiveData<Boolean> get() = _closeBottomSheet
    fun closeBottomSheet() {
        _closeBottomSheet.value = true
    }
    fun closeBottomSheetCompleted() {
        _closeBottomSheet.value = false
    }




    private val _openBottomSheet = MutableLiveData<Boolean>()
    val openBottomSheet: LiveData<Boolean> get() = _openBottomSheet
    fun openBottomSheet() {
        _openBottomSheet.value = true
    }
    fun openBottomSheetCompleted() {
        _openBottomSheet.value = false
    }





    private val _addPointButtonClicked = MutableLiveData<Boolean>()
    val addPointButtonClicked: LiveData<Boolean> get() = _addPointButtonClicked
    fun addPointButtonClicked() {
        _addPointButtonClicked.value = true
    }
    fun addPointButtonClickedComplete() {
        _addPointButtonClicked.value = false
    }


    init {
        converter= CoordinateConversion()
        dp.value = ""
        depth.value = ""
        current1.value = ""
        current2.value = ""
        _closeBottomSheet.value = false
        _openBottomSheet.value = false
        _addPointButtonClicked.value = false
        gpsX.value=""
        gpsY.value=""
        gpsX_Y.value=""

        accuracy.value=""
        progressVisibility.value= View.VISIBLE
    }


    @Suppress("UNCHECKED_CAST")
    class LineDetailsViewModelFactory(private val repository: PipeLinesRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (LineDetailsViewModel(repository) as T)
        }
    }
}