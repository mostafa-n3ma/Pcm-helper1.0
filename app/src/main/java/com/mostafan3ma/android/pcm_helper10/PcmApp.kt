package com.mostafan3ma.android.pcm_helper10

import android.app.Application
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository

class PcmApp:Application() {

    val repository:PipeLinesRepository
    get()=ServiceLocator.provideRepository(this)

    override fun onCreate() {
        super.onCreate()

    }

}