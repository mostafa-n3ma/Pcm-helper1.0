package com.mostafan3ma.android.pcm_helper10

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.mostafan3ma.android.pcm_helper10.data.source.DefaultLocalDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.LocalDataSource.LocalDataSource
import com.mostafan3ma.android.pcm_helper10.data.source.PipeLinesRepository
import com.mostafan3ma.android.pcm_helper10.data.source.database.LineDataBase
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private val lock = Any()
    private var database:LineDataBase?=null

    @Volatile
    var pipeLinesRepository: PipeLinesRepository?=null
    @VisibleForTesting set


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
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.lineDao())
    }

    private fun createDataBase(context: Context): LineDataBase {
        val result= Room.databaseBuilder(
            context.applicationContext,
            LineDataBase::class.java,
            "Lins"
        ).fallbackToDestructiveMigration().build()
        database=result
        return result
    }


    @VisibleForTesting
    fun resetRepo(){
        synchronized(lock){
            runBlocking { pipeLinesRepository?.clearAllLines() }
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            pipeLinesRepository = null
        }
    }

}