package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import androidx.lifecycle.*
import com.mostafan3ma.android.pcm_helper10.Utils.CoordinateConversion
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.launch

class LineDetailsViewModel(private val repository: PipeLinesRepository) : ViewModel() {
    private var converter: CoordinateConversion = CoordinateConversion()

    lateinit var selectedLine: PipeLine
    fun getSelectedLine(selectedLine: PipeLine) {
        this.selectedLine = selectedLine
    }




    //point info
    val dp = MutableLiveData<String>()
    val depth = MutableLiveData<String>()
    val current1 = MutableLiveData<String>()
    val current2 = MutableLiveData<String>()
    val gpsX = MutableLiveData<String>()
    val gpsY = MutableLiveData<String>()



    val gpsX_Y = MutableLiveData<String>()
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
            val updatedList=selectedLine.points
            if (point.is_point){
                updatedList.map {
                    if (it.no>point.no){
                        it.no-=1
                    }
                }
            }
            repository.updatePointsList(selectedLine.id,updatedList)
        }
    }
    private fun getPointsNo():Int{
        var pointNO:Int=0
        selectedLine.points.map { point->
            if (point.is_point){
                pointNO+=1
            }
        }
        return pointNO
    }
    private fun getNextPointNo():Int{
        return getPointsNo()+1
    }
    private fun getNewPointInfo(): DamagePoint {
        return DamagePoint(
            getNextPointNo(), dp.value, depth.value, current1.value, current2.value,
            gpsX.value, gpsY.value
        )
    }

    fun addNewPointToPipeList() {
        selectedLine.points.add(getNewPointInfo())
        viewModelScope.launch {
            repository.updatePointsList(selectedLine.id, selectedLine.points)
        }

    }
     fun addNewBendToPipeList(){
        val newBend=DamagePoint(no = 0, gps_x = gpsX.value, gps_y = gpsY.value, is_point = false)
        selectedLine.points.add(newBend)
        viewModelScope.launch {
            repository.updatePointsList(selectedLine.id, selectedLine.points)
        }
    }


    //Point events
    private val _closePointBottomSheet = MutableLiveData<Boolean>()
    val closePointBottomSheet: LiveData<Boolean> get() = _closePointBottomSheet
    fun closePointBottomSheet() {
        _closePointBottomSheet.value = true
    }
    fun closePointBottomSheetCompleted() {
        _closePointBottomSheet.value = false
    }
    ////////////////////////
    private val _openPointBottomSheet = MutableLiveData<Boolean>()
    val openPointBottomSheet: LiveData<Boolean> get() = _openPointBottomSheet
    fun openPointBottomSheet() {
        _openPointBottomSheet.value = true
    }
    fun openPointBottomSheetCompleted() {
        _openPointBottomSheet.value = false
    }
/////////////////////////////
    private val _addPointButtonClicked = MutableLiveData<Boolean>()
    val addPointButtonClicked: LiveData<Boolean> get() = _addPointButtonClicked
    fun addPointButtonClicked() {
        _addPointButtonClicked.value = true
    }
    fun addPointButtonClickedComplete() {
        _addPointButtonClicked.value = false
    }
///////////////




    //Bend Events
    private val _closeBendBottomSheet = MutableLiveData<Boolean>()
    val closeBendBottomSheet: LiveData<Boolean> get() = _closeBendBottomSheet
    fun closeBendBottomSheet() {
        _closeBendBottomSheet.value = true
    }
    fun closeBendBottomSheetCompleted() {
        _closeBendBottomSheet.value = false
    }
    ////////////////////////
    private val _openBendBottomSheet = MutableLiveData<Boolean>()
    val openBendBottomSheet: LiveData<Boolean> get() = _openBendBottomSheet
    fun openBendBottomSheet() {
        _openBendBottomSheet.value = true
    }
    fun openBendBottomSheetCompleted() {
        _openBendBottomSheet.value = false
    }
    /////////////////////////////
    private val _addBendButtonClicked = MutableLiveData<Boolean>()
    val addBendButtonClicked: LiveData<Boolean> get() = _addBendButtonClicked
    fun addBendButtonClicked() {
        _addBendButtonClicked.value = true
    }
    fun addBendButtonClickedComplete() {
        _addBendButtonClicked.value = false
    }




    init {
        dp.value = ""
        depth.value = ""
        current1.value = ""
        current2.value = ""
        _closePointBottomSheet.value = false
        _openPointBottomSheet.value = false
        _addPointButtonClicked.value = false
        gpsX.value=""
        gpsY.value=""
        gpsX_Y.value=""

        accuracy.value=""
        progressVisibility.value= View.VISIBLE

        _closeBendBottomSheet.value=false
        _openBendBottomSheet.value=false
        _addBendButtonClicked.value=false




    }


    @Suppress("UNCHECKED_CAST")
    class LineDetailsViewModelFactory(private val repository: PipeLinesRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (LineDetailsViewModel(repository) as T)
        }
    }
}