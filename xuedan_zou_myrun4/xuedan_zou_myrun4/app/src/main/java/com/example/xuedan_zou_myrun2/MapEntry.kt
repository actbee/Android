package com.example.xuedan_zou_myrun2

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.common.util.ArrayUtils.newArrayList
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "map_entry_table")
data class MapEntry(
    var speed: Float = 0F,
    var avgspeed: Float = 0F,
    var altitude: Double = 0.toDouble(),
    var time: Long = 0L,
    var distance: Float = 0F,
    var calorie:Double = 0.toDouble(),
    var location:String = " "
)
    //:Parcelable