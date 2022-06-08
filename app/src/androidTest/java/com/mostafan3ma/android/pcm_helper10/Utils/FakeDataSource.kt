package com.mostafan3ma.android.pcm_helper10.Utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mostafan3ma.android.pcm_helper10.data.source.DefaultLocalDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine

class FakeDataSource(var pipeList:MutableList<PipeLine>): DefaultLocalDataSource {
    private val mutableLiveLinesList=MutableLiveData<List<PipeLine>>()
    private val liveLineList:LiveData<List<PipeLine>> get() = mutableLiveLinesList

    init {
        mutableLiveLinesList.value=pipeList.toList()
    }



    override fun getAllLines(): LiveData<List<PipeLine>> {
        return liveLineList
    }

    override suspend fun getPipeLine(id: Int): PipeLine? {
        return pipeList.find { it.id == id }
    }

    override suspend fun getLastPipeLine(): PipeLine? {
        return pipeList[pipeList.lastIndex]?:null
    }

    override suspend fun clearAllLines() {
        pipeList.clear()
    }

    override suspend fun deleteLine(id: Int) {
        val pipeLine: PipeLine? =pipeList.find { it.id == id }
        pipeList.remove(pipeLine)
    }

    override suspend fun insertLine(line: PipeLine) {
        pipeList.add(line)
    }

    override suspend fun updateLine(line: PipeLine) {
        val pipeLine: PipeLine? =pipeList.find { it.id == line.id }
        pipeList.set(pipeList.indexOf(pipeLine),line)
    }

    override suspend fun updatePointsList(pipeId: Int, points: MutableList<DamagePoint>) {
        val pipeLine=pipeList.find { it.id==pipeId }
        pipeLine?.points=points
        pipeList[pipeList.indexOf(pipeLine)] = pipeLine!!
    }

}