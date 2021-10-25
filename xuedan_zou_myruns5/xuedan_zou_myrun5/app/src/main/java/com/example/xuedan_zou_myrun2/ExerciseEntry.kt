package com.example.xuedan_zou_myrun2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.common.util.ArrayUtils.newArrayList
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "exercise_entry_table")
data class ExerciseEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "inputType")
    var inputType:Int = 0,

    @ColumnInfo(name = "activityType")
    var activityType:Int = 0,

    @ColumnInfo(name = "dateTime")
    var dateTime:String = "",

    @ColumnInfo(name = "duration")
    var duration:Int = 0,

    @ColumnInfo(name = "distance")
    var distance:Float = 0F,

    @ColumnInfo(name = "avgPace")
    var avgPace:Float = 0F,

    @ColumnInfo(name = "avgSpeed")
    var avgSpeed:Float = 0F,

    @ColumnInfo(name = "calorie")
    var calorie:Float = 0F,

    @ColumnInfo(name = "climb")
    var climb:Float = 0F,

    @ColumnInfo(name = "heartRate")
    var heartRate:Int = 0,

    @ColumnInfo(name = "comment")
    var comment:String = "",

    @ColumnInfo(name = "locationlist")
    var locationlist: String = ""
)
