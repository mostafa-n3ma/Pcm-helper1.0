package com.mostafan3ma.android.pcm_helper10.data.source

import android.content.Context
import androidx.lifecycle.LiveData
import com.mostafan3ma.android.pcm_helper10.data.source.database.DamagePoint
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDB
import com.mostafan3ma.android.pcm_helper10.data.source.database.PipeLine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PipeLinesRepository(
    private val remoteDataSource: DefaultRemoteDataSource,
    private val localDataSource: DefaultLocalDataSource,
    private val dispatcher:CoroutineDispatcher=Dispatchers.IO
) {

    suspend fun getAllLines(): LiveData<List<PipeLine>> {
        return withContext(dispatcher){
            localDataSource.getAllLines()
        }
    }

    suspend fun clearAllLines(){
        withContext(dispatcher){
            localDataSource.clearAllLines()
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





    //Line points
     fun getLinePointAt(position: Int,pointList: MutableList<DamagePoint>): DamagePoint {
        return pointList[position]
    }
     fun updateLinePointAt(position: Int, pointList: MutableList<DamagePoint>, newPoint: DamagePoint){
        pointList[position] = newPoint
    }


}