package com.mostafan3ma.android.pcm_helper10.data.source

import androidx.lifecycle.LiveData
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine

interface DefaultLocalDataSource {

    suspend fun getAllLines(): LiveData<List<PipeLine>>
    suspend fun clearAllLines()
    suspend fun insertLine(line: PipeLine)
    suspend fun updateLine(line: PipeLine)

}