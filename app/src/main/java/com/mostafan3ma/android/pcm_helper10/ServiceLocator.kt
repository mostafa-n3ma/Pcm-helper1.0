package com.mostafan3ma.android.pcm_helper10

import android.content.Context
import com.mostafan3ma.android.pcm_helper10.data.source.DefaultLocalDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.LocalDataSource.LocalDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDB
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDao
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDataBase

object ServiceLocator {
    private var lineDao: LineDao?=null

    @Volatile
    var pipeLinesRepository: PipeLinesRepository?=null


    fun provideRepository(context: Context):PipeLinesRepository{
        synchronized(this){
            return pipeLinesRepository?:createPipeLineRepository(context)
        }

    }

    private fun createPipeLineRepository(context: Context): PipeLinesRepository {
        val newRepo=PipeLinesRepository(createLocalDataSource(context))
        pipeLinesRepository=newRepo
        return newRepo
    }

    private fun createLocalDataSource(context: Context): DefaultLocalDataSource {
        val dao= lineDao?:LineDB.createLinDao(context)
        return LocalDataSource(dao)
    }
}