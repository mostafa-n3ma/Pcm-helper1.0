package com.mostafan3ma.android.pcm_helper10.data.source

import android.content.Context
import androidx.room.Room

object LineDB {

    fun createLinDao(context: Context):LineDao{
        return Room.databaseBuilder(
            context.applicationContext,
            LineDataBase::class.java,
            "Lins"
        ).build().getLineDao()
    }

}




