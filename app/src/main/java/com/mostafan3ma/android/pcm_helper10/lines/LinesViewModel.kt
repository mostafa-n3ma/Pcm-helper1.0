package com.mostafan3ma.android.pcm_helper10.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine

class LinesViewModel(private val repository: PipeLinesRepository) : ViewModel() {

    val  linesList=repository.getAllLines()

    private val _navigateToSelectedLine=MutableLiveData<PipeLine?>()
    val navigateToSelectedLine:LiveData<PipeLine?> get() = _navigateToSelectedLine
    fun navigateToSelectedLine(selectedLine: PipeLine){
        _navigateToSelectedLine.value=selectedLine
    }
    fun navigateToSelectedLineComplete(){
        _navigateToSelectedLine.value=null
    }

    private  val _navigateToAddLineFragment=MutableLiveData<Boolean>()
    val navigateToAddLineFragment:LiveData<Boolean>get() = _navigateToAddLineFragment
    fun navigateToAddLineFragment(){
        _navigateToAddLineFragment.value=true
    }
    fun navigateToAddLineFragmentCompleted(){
        _navigateToAddLineFragment.value=false
    }
    init {
        _navigateToSelectedLine.value=null
        _navigateToAddLineFragment.value=false

    }

}

//viewModelScope.launch {
//    repository.insertLine(
//        PipeLine(
//            name = "ADR2by2-5-5H",
//            ogm = "OGM0",
//            type = "OIL",
//            length = "1824m",
//            work_date = "27/3/2022",
//            start_point = "562348;3598441",
//            i_start = "960mA",
//            end_point = "552146;3592158",
//            i_end = "120mA",
//            points = mutableListOf(
//                DamagePoint(db = "50", depth = "1.3", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "45", depth = "1.0", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "32", depth = "1.1", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "42", depth = "1", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "40", depth = "1", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "56", depth = "1.1", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "61", depth = "1.9", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "37", depth = "1.8", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "45", depth = "1.3", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "48", depth = "1.8", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "46", depth = "1.7", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "40", depth = "1.4", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "40", depth = "1.5", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116"),
//                DamagePoint(db = "29", depth = "1.5", current1 = "800mA", current2 = "26mA", gps_x = "554877", gps_y = "3592116")
//            )
//        )
//    )
//}

//viewModelScope.launch {
//    val line=repository.getPipeLine(6)
//    line?.points?.add(
//        DamagePoint(
//            no = 4,
//            db = "77",
//            depth = "7.7",
//            gps_x = "777777",
//            gps_y = "7777777",
//            current1 = "777",
//            current2 = "777"
//        )
//    )
//    if (line != null) {
//        repository.updatePointsList(6,line.points)
//    }
//
//}