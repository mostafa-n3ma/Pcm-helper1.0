package com.mostafan3ma.android.pcm_helper10.lines.operations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddLineViewModel:ViewModel() {

    val name=MutableLiveData<String>()
    val ogm=MutableLiveData<String>()
    val type=MutableLiveData<String>()
    val length=MutableLiveData<String>()
    val work_date=MutableLiveData<String>()


    init {
        name.value=""
        ogm.value=""
        work_date.value=""
        length.value=""
        type.value=""
    }

}