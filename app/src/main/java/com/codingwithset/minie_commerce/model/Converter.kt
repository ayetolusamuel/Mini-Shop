package com.codingwithset.minie_commerce.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun listToJson(list: List<Int>):String = Gson().toJson(list)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()

    @TypeConverter
    fun imagesToJson(images: List<Image>):String = Gson().toJson(images)

    @TypeConverter
    fun jsonToImages(value: String) = Gson().fromJson(value, Array<Image>::class.java).toList()


}