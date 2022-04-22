package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import androidx.lifecycle.*
import com.mostafan3ma.android.pcm_helper10.Utils.CoordinateConversion
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.launch

class LineDetailsViewModel(private val repository: PipeLinesRepository,  val selectedLine: PipeLine) : ViewModel() {
    private var converter: CoordinateConversion = CoordinateConversion()




    val name = MutableLiveData<String>()
    val ogm = MutableLiveData<String>()
    val type = MutableLiveData<String>()
    val length = MutableLiveData<String>()
    val work_date = MutableLiveData<String>()
    val i_start = MutableLiveData<String>()
    val startPoint = MutableLiveData<String>()
    val startPoint_x = MutableLiveData<String>()
    val startPoint_y = MutableLiveData<String>()
    val endPoint = MutableLiveData<String>()
    val endPoint_x = MutableLiveData<String>()
    val endPoint_y = MutableLiveData<String>()
    val i_end=MutableLiveData<String>()

    val work_team=MutableLiveData<String>()
    val input=MutableLiveData<String>()
    val extra_note=MutableLiveData<String>()



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
    private val _openBendBottomSheet = MutableLiveData<Boolean>()
    val openBendBottomSheet: LiveData<Boolean> get() = _openBendBottomSheet
    fun openBendBottomSheet() {
        _openBendBottomSheet.value = true
    }
    fun openBendBottomSheetCompleted() {
        _openBendBottomSheet.value = false
    }
    //////// ////////////////////////
    private val _closeBendBottomSheet = MutableLiveData<Boolean>()
    val closeBendBottomSheet: LiveData<Boolean> get() = _closeBendBottomSheet
    fun closeBendBottomSheet() {
        _closeBendBottomSheet.value = true
    }
    fun closeBendBottomSheetCompleted() {
        _closeBendBottomSheet.value = false
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




    //End Point Events
    private val _openEndPointBottomSheet=MutableLiveData<Boolean>()
    val openEndPointBottomSheet:LiveData<Boolean>get() = _openEndPointBottomSheet
    fun openEndPointBottomSheet(){
        _openEndPointBottomSheet.value=true
    }
    fun openEndPointBottomSheetCompleted(){
        _openEndPointBottomSheet.value=false
    }
    /////////////////////////
    private val _closeEndPointBottomSheet=MutableLiveData<Boolean>()
    val closeEndPointBottomSheet:LiveData<Boolean>get() = _closeEndPointBottomSheet
    fun closeEndPointBottomSheet(){
        _closeEndPointBottomSheet.value=true
    }
    fun closeEndPointBottomSheetCompleted(){
        _closeEndPointBottomSheet.value=false
    }
    ///////////////////
    private val _addEndPointButtonClicked=MutableLiveData<Boolean>()
    val addEndPointButtonClicked:LiveData<Boolean>get() = _addEndPointButtonClicked
    fun addEndPointButtonClicked(){
        _addEndPointButtonClicked.value=true
        updateLinesEndPointAndEndCurrent()
    }

    fun addEndPointButtonClickedCompleted(){
        _addEndPointButtonClicked.value=false
    }

    private fun updateLinesEndPointAndEndCurrent() {
        endPoint_x.value=gpsX.value
        endPoint_y.value=gpsY.value
        endPoint.value=gpsX_Y.value
        selectedLine.end_point_x=endPoint_x.value
        selectedLine.end_point_y=endPoint_y.value
        selectedLine.i_end=i_end.value
        viewModelScope.launch {
            repository.updateLine(selectedLine)
        }
    }




    init {
        name.value = ""
        ogm.value = ""
        work_date.value = ""
        length.value = ""
        type.value = ""
        i_start.value = ""
        startPoint.value = "${selectedLine.start_point_x};${selectedLine.start_point_y}"
        startPoint_x.value="${selectedLine.start_point_x!!}"
        startPoint_y.value="${selectedLine.start_point_y!!}"
        endPoint.value="${selectedLine.end_point_x};${selectedLine.end_point_x}"
        endPoint_x.value="${selectedLine.end_point_x!!}"
        endPoint_y.value="${selectedLine.end_point_y!!}"
        work_team.value=""
        input.value=""
        extra_note.value=""
        i_end.value= selectedLine.i_end!!



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




        _openEndPointBottomSheet.value=false
        _closeEndPointBottomSheet.value=false
        _addEndPointButtonClicked.value=false


    }


    @Suppress("UNCHECKED_CAST")
    class LineDetailsViewModelFactory(private val repository: PipeLinesRepository,private val selectedLine: PipeLine) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (LineDetailsViewModel(repository,selectedLine) as T)
        }
    }
}