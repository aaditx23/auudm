package com.aaditx23.auudm.data.local.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListInt(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toListInt(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }
}
