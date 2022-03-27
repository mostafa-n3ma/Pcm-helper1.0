package com.mostafan3ma.android.pcm_helper10.Lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDB
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.launch

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


    init {
        _navigateToSelectedLine.value=null
    }

}