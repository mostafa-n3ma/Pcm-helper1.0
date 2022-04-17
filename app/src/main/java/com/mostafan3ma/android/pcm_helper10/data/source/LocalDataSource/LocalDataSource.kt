package com.mostafan3ma.android.pcm_helper10.data.source.LocalDataSource

import androidx.lifecycle.LiveData
import com.mostafan3ma.android.pcm_helper10.data.source.DefaultLocalDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDao
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDataBase
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine

class LocalDataSource(private val dao: LineDao) : DefaultLocalDataSource {
    override  fun getAllLines(): LiveData<List<PipeLine>> {
        return dao.getAllLines()
    }

    override suspend fun getPipeLine(id: Int) :PipeLine?{
         return dao.getPipeLine(id)
    }

    override suspend fun getLastPipeLine(): PipeLine? {
        return dao.getLastPipeLine()
    }

    override suspend fun clearAllLines() {
        dao.clear()
    }

    override suspend fun deleteLine(id: Int) {
        dao.deleteLine(id)
    }


    override suspend fun insertLine(line: PipeLine) {
        dao.insertLine(line)
    }

    override suspend fun updateLine(line: PipeLine) {
        dao.updateLine(line)
    }

    override suspend fun updatePointsList(pipeId: Int, points: MutableList<DamagePoint>) {
        dao.updatePointsList(pipeId,points)
    }

}