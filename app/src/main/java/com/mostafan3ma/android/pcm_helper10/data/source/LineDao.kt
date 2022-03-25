package com.mostafan3ma.android.pcm_helper10.data.source

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertLine(line:PipeLine)

    @Update
    suspend fun updateLine(line: PipeLine)

    @Query("select * FROM table_Lines")
     fun getAllLines():LiveData<List<PipeLine>>


    @Query("DELETE FROM table_Lines")
    suspend fun clear()




}