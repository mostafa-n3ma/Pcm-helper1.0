package com.mostafan3ma.android.pcm_helper10.lines.operations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine

class LineDetailsViewModel (private val repository: PipeLinesRepository):ViewModel() {

    private lateinit var selectedLine:PipeLine
    fun getSelectedLine(selectedLine:PipeLine){
        this.selectedLine=selectedLine
    }


    val dp1=MutableLiveData<Int>()
    val dp2=MutableLiveData<Int>()
    val depth1=MutableLiveData<Int>()
    val depth2=MutableLiveData<Int>()
    val depth3=MutableLiveData<Int>()
    val current1=MutableLiveData<Int>()
    val current2=MutableLiveData<Int>()




    private val _closeBottomSheet=MutableLiveData<Boolean>()
    val closeBottomSheet:LiveData<Boolean> get() = _closeBottomSheet
    fun closeBottomSheet(){
        _closeBottomSheet.value=true
    }
    fun closeBottomSheetCompleted(){
        _closeBottomSheet.value=false
    }

    private val _openBottomSheet=MutableLiveData<Boolean>()
    val openBottomSheet:LiveData<Boolean>get() = _openBottomSheet
    fun openBottomSheet(){
        _openBottomSheet.value=true
    }
    fun openBottomSheetCompleted(){
        _openBottomSheet.value=false
    }



    init {
        dp1.value=3
        dp2.value=0
        depth1.value=1
        depth2.value=0
        depth3.value=0
        current1.value=1000
        current2.value=1000
        _closeBottomSheet.value=false
        _openBottomSheet.value=false
    }






    @Suppress("UNCHECKED_CAST")
    class LineDetailsViewModelFactory(private val repository: PipeLinesRepository)
        :ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (LineDetailsViewModel(repository)as T)
        }
    }
}