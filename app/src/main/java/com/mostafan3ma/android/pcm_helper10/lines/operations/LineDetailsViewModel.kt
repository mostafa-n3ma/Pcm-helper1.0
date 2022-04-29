package com.mostafan3ma.android.pcm_helper10.lines.operations

import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mostafan3ma.android.pcm_helper10.Utils.CoordinateConversion
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LineDetailsViewModel(private val repository: PipeLinesRepository,  val selectedLine: PipeLine) : ViewModel() {
    private var converter: CoordinateConversion = CoordinateConversion()


    val finalLine=MutableLiveData<PipeLine>()
    val comingOgm=MutableLiveData<String>()
    val comingType=MutableLiveData<String>()
    val comingInput=MutableLiveData<String>()
    fun updateFinalLine(){
        viewModelScope.launch {
            finalLine.value= repository.getPipeLine(selectedLine.id)
        }
    }

    val name = MutableLiveData<String>()
    val ogm = MutableLiveData<String>()
    val type = MutableLiveData<String>()
    val length = MutableLiveData<String>()
    val start_work_date = MutableLiveData<String>()
    val end_work_date = MutableLiveData<String>()
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
            updateFinalLine()
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

    private fun reInitPointsLiveValues(){
        dp.value=""
        depth.value=""
        current1.value=""
        current2.value=""
    }
    fun addNewPointToPipeList() {
        selectedLine.points.add(getNewPointInfo())
        viewModelScope.launch {
            repository.updatePointsList(selectedLine.id, selectedLine.points)
            updateFinalLine()
            reInitPointsLiveValues()
        }







    }
     fun addNewBendToPipeList(){
        val newBend=DamagePoint(no = 0, gps_x = gpsX.value, gps_y = gpsY.value, is_point = false)
        selectedLine.points.add(newBend)
        viewModelScope.launch {
            repository.updatePointsList(selectedLine.id, selectedLine.points)
            updateFinalLine()
        }
    }


    //Point sheet events
    val pointBottomSheetState=MutableLiveData<Boolean>()
    fun openPointSheet(){
        pointBottomSheetState.value=true
    }
    fun closePointSheet(){
        pointBottomSheetState.value=false
    }

    private val _addPointButtonClicked = MutableLiveData<Boolean>()
    val addPointButtonClicked: LiveData<Boolean> get() = _addPointButtonClicked
    fun addPointButtonClicked() {
        _addPointButtonClicked.value = true
    }
    fun addPointButtonClickedComplete() {
        _addPointButtonClicked.value = false
    }
///////////////




    //Bend Sheet Events
    val bendSheetState=MutableLiveData<Boolean>()
    fun openBendSheet(){ bendSheetState.value=true }
    fun closeBendSheet(){ bendSheetState.value=false }
    //
    private val _addBendButtonClicked = MutableLiveData<Boolean>()
    val addBendButtonClicked: LiveData<Boolean> get() = _addBendButtonClicked
    fun addBendButtonClicked() {
        _addBendButtonClicked.value = true
    }
    fun addBendButtonClickedComplete() {
        _addBendButtonClicked.value = false
    }
    //////////////////////



    //Finish Bottom Sheet Events
    val finishSheetState=MutableLiveData<Boolean>()
    fun openFinishSheet(){
        finishSheetState.value=true
    }
    fun closeFinishSheet(){
        finishSheetState.value=false
    }
    ///////////////////
    private val _finishButtonClicked=MutableLiveData<Boolean>()
    val finishButtonClicked:LiveData<Boolean>get() = _finishButtonClicked
    fun finishButtonClicked(){
        _finishButtonClicked.value=true
        updateLineWithFinishingData()
    }
    fun finishButtonClickedCompleted(){
        _finishButtonClicked.value=false
    }
    private fun updateLineWithFinishingData() {
        endPoint_x.value=gpsX.value
        endPoint_y.value=gpsY.value
        endPoint.value=gpsX_Y.value
        selectedLine.end_point_x=endPoint_x.value
        selectedLine.end_point_y=endPoint_y.value
        selectedLine.i_end=i_end.value
        selectedLine.end_work_date=end_work_date.value
        viewModelScope.launch {
            repository.updateLine(selectedLine)
            updateFinalLine()
        }
    }




    ///extra note events
    val noteSheetState=MutableLiveData<Boolean>()
    fun openNoteSheet(){
        noteSheetState.value=true
    }
    fun closeNoteSheet(){
        noteSheetState.value=false
    }
    /////////
    private val _addNoteClicked=MutableLiveData<Boolean>()
    val addNoteClicked:LiveData<Boolean>get() = _addNoteClicked
    fun addNoteClicked(){
        _addNoteClicked.value=true
        updateLinesNote()
    }
    fun addNoteClickedComplete(){
        _addNoteClicked.value=false
    }
    fun updateLinesNote(){
        selectedLine.extra_note=extra_note.value
        viewModelScope.launch {
            repository.updateLine(selectedLine)
            updateFinalLine()
        }
    }



    //edit bottom sheet
    val editSheetState=MutableLiveData<Boolean>()
    fun openEditSheet(){
        editSheetState.value=true
    }
    fun closeEditSheet(){
        editSheetState.value=false
    }
    /////////
    private val _editSaveButtonClicked=MutableLiveData<Boolean>()
    val editSaveButtonClicked:LiveData<Boolean>get() = _editSaveButtonClicked
    fun editSaveButtonClicked(){
        _editSaveButtonClicked.value=true
        editLine()
    }
    fun editSaveButtonClickedComplete(){
        _editSaveButtonClicked.value=false
    }
    private fun editLine() {
        selectedLine.let {
            it.name=name.value
            it.length=length.value
            it.start_work_date=start_work_date.value
            it.end_work_date=end_work_date.value
            it.i_start=i_start.value
            it.i_end=i_end.value
            it.work_team=work_team.value
            it.extra_note=extra_note.value
            if (comingOgm.value!!.isNotEmpty())it.ogm=comingOgm.value
            if (comingType.value!!.isNotEmpty())it.type=comingType.value
            if (comingInput.value!!.isNotEmpty())it.input=comingInput.value

        }
        viewModelScope.launch {
            repository.updateLine(selectedLine)
            updateFinalLine()
        }
    }

    fun closeBottomSheetWithDelay(bottomSheet: BottomSheetBehavior<LinearLayout>) {
        //since the keyboard was anyone me and stopping the bottom sheet from full collapsing and stopping on the medal
//        don't know way actually but wat I know that hiding the keyboard and collapsing the bottom sheet together
//        will cause a kind of lacking in the bottom sheet
//        so I decided to delay the bottom sheet collapsing until the keyboard is hidden
        viewModelScope.launch {
            delay(100).let {
                bottomSheet.state=BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }






    init {
        finalLine.value=selectedLine
        name.value = selectedLine.name?:""
        ogm.value =selectedLine.ogm?:""
        start_work_date.value =selectedLine.start_work_date?:""
        end_work_date.value=selectedLine.end_work_date?:""
        length.value =selectedLine.length?:""
        type.value =selectedLine.type?:""
        i_start.value =selectedLine.i_start?:""
        startPoint.value = "${selectedLine.start_point_x};${selectedLine.start_point_y}"
        startPoint_x.value="${selectedLine.start_point_x!!}"
        startPoint_y.value="${selectedLine.start_point_y!!}"
        endPoint.value="${selectedLine.end_point_x};${selectedLine.end_point_x}"
        endPoint_x.value="${selectedLine.end_point_x!!}"
        endPoint_y.value="${selectedLine.end_point_y!!}"
        work_team.value=selectedLine.work_team?:""
        input.value=selectedLine.input?:""
        extra_note.value=selectedLine.extra_note?:""
        i_end.value= selectedLine.i_end!!



        dp.value = ""
        depth.value = ""
        current1.value = ""
        current2.value = ""
        _addPointButtonClicked.value = false
        gpsX.value=""
        gpsY.value=""
        gpsX_Y.value=""

        accuracy.value=""
        progressVisibility.value= View.VISIBLE


        _addBendButtonClicked.value=false
        bendSheetState.value=false


        _finishButtonClicked.value=false
        finishSheetState.value=false






        noteSheetState.value=false
        _addNoteClicked.value=false





        editSheetState.value=false
        _editSaveButtonClicked.value=false




        comingInput.value=""
        comingOgm.value=""
        comingType.value=""




        pointBottomSheetState.value=false

    }


    @Suppress("UNCHECKED_CAST")
    class LineDetailsViewModelFactory(private val repository: PipeLinesRepository,private val selectedLine: PipeLine) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (LineDetailsViewModel(repository,selectedLine) as T)
        }
    }
}