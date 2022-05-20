package com.mostafan3ma.android.pcm_helper10.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.launch

class LinesViewModel(private val repository: PipeLinesRepository) : ViewModel() {

    private var _longClickedLine:PipeLine?=null
    fun getLongClickedLine(line: PipeLine){
        _longClickedLine=line
    }
    fun deleteLongClickedLine(){
        viewModelScope.launch {
            repository.deleteLine(_longClickedLine!!.id)
            _undo.value=true
        }
    }
    fun undoDeleting() {
        viewModelScope.launch {
//            _longClickedLine?.let { repository.insertLine(it) }
            repository.insertLine(_longClickedLine!!)
        }
    }

    private val _undo=MutableLiveData<Boolean>()
    val undo:LiveData<Boolean>get() = _undo
    fun undoCompleted(){
        _undo.value=false
    }


    val  linesList: LiveData<List<PipeLine>> =repository.getAllLines()

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
        _undo.value=false
    }

}
