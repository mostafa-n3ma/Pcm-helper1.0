package com.mostafan3ma.android.pcm_helper10.data.source.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertLine(line: PipeLine)

    @Update
    suspend fun updateLine(line: PipeLine)

    @Query("select * FROM table_Lines")
     fun getAllLines():LiveData<List<PipeLine>>

     @Query("SELECT * FROM table_Lines WHERE id=:id")
     suspend fun getPipeLine(id:Int):PipeLine?


     @Query("SELECT * FROM table_Lines WHERE id=(SELECT MAX(id)FROM table_Lines) LIMIT 1")
     suspend fun getLastPipeLine():PipeLine?

    @Query("DELETE FROM table_Lines")
    suspend fun clear()

   @Query("UPDATE table_Lines SET points=:points WHERE id=:id")
   fun updatePointsList(id:Int,points:MutableList<DamagePoint>)

}