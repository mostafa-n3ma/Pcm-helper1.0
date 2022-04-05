package com.mostafan3ma.android.pcm_helper10.data.source.database

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mostafan3ma.android.pcm_helper10.R
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "table_Lines")
data class PipeLine(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
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
):Parcelable


@Parcelize
data class DamagePoint(
    @PrimaryKey(autoGenerate = true)
    var no:Int=1,
    var db: String? = null,
    var depth: String? = null,
    var current1: String? = null,
    var current2: String? = null,
    var gps_x: String? = null,
    var gps_y: String? = null
):Parcelable

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


