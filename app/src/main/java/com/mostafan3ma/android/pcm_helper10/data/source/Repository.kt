package com.mostafan3ma.android.pcm_helper10.data.source

import android.content.Context
import androidx.lifecycle.LiveData
import java.text.FieldPosition

class Repository(private val context: Context) {
    private val database=LineDB.createLinDao(context)


    suspend fun getAllLines(): LiveData<List<PipeLine>> {
        return database.getAllLines()
    }

    suspend fun clearAllLines(){
        return database.clear()
    }

    suspend fun insertLine(line: PipeLine){
        database.insertLine(line)
    }

    suspend fun updateLine(line: PipeLine){
        database.updateLine(line)
    }





    //Line points
     fun getLinePointAt(position: Int,pointList: MutableList<DamagePoint>):DamagePoint{
        return pointList[position]
    }
     fun updateLinePointAt(position: Int,pointList: MutableList<DamagePoint>,newPoint:DamagePoint){
        pointList[position] = newPoint
    }















}