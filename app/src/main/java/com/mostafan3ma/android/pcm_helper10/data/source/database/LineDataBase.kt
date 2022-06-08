package com.mostafan3ma.android.pcm_helper10.data.source.database

import androidx.room.*

@Database(entities = [PipeLine::class], version = 3, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class LineDataBase :RoomDatabase(){
    abstract fun lineDao(): LineDao

}




//    companion object{
//        @Volatile
//        private var INSTANCE:LineDataBase?=null
//
//        fun getInstance(context: Context):LineDataBase{
//            synchronized(this){
//                var instance= INSTANCE
//                if (instance==null){
//                    instance=Room.databaseBuilder(
//                        context,
//                        LineDataBase::class.java,
//                        "PipeLineDataBase"
//                    ).fallbackToDestructiveMigration().build()
//                    INSTANCE=instance
//                }
//                return instance
//            }
//        }
//    }