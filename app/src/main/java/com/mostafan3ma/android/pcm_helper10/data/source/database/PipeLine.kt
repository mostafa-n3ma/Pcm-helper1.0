package com.mostafan3ma.android.pcm_helper10.data.source.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "table_Lines")
data class PipeLine(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var name: String?,
    var ogm: String?,
    var length: String?,
    var type: String?,
    var i_start: String?,
    var i_end: String?,
    var start_point: String?,
    var end_point: String?,
    var work_date:String?,
    var points: MutableList<DamagePoint>
)



data class DamagePoint(
    var db: String? = null,
    var Depth: String? = null,
    var current1: String? = null,
    var current2: String? = null,
    var gps_x: String? = null,
    var gps_y: String? = null
)

class DataConverter {
    @TypeConverter
    fun fromPoints(points: MutableList<DamagePoint>?): String? {
        if (points == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<MutableList<DamagePoint>?>() {}.type
        return gson.toJson(points, type)
    }

    @TypeConverter
    fun toPoints(points: String?): MutableList<DamagePoint>? {
        if (points == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<MutableList<DamagePoint>?>() {}.type
        return gson.fromJson<MutableList<DamagePoint>>(points, type)
    }
}

