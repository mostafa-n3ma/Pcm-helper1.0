package com.mostafan3ma.android.pcm_helper10.data.source

import androidx.lifecycle.LiveData
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PipeLinesRepository(
    private val localDataSource: DefaultLocalDataSource,
    private val dispatcher:CoroutineDispatcher=Dispatchers.IO
) {

     fun getAllLines(): LiveData<List<PipeLine>> {
           return localDataSource.getAllLines()
    }
    suspend fun getPipeLine(id:Int):PipeLine?{
       return localDataSource.getPipeLine(id)
    }
    suspend fun getLastPipeLine():PipeLine?{
        return localDataSource.getLastPipeLine()
    }

    suspend fun clearAllLines(){
        withContext(dispatcher){
            localDataSource.clearAllLines()
        }
    }
    suspend fun deleteLine(id: Int){
        withContext(dispatcher){
            localDataSource.deleteLine(id)
        }
    }

    suspend fun insertLine(line: PipeLine){
        withContext(dispatcher){
            localDataSource.insertLine(line)
        }
    }

    suspend fun updateLine(line: PipeLine){
        withContext(dispatcher){
            localDataSource.updateLine(line)
        }
    }

    suspend fun updatePointsList(pipeID:Int,points:MutableList<DamagePoint>){
        withContext(dispatcher){
            localDataSource.updatePointsList(pipeID,points)
        }
    }





}