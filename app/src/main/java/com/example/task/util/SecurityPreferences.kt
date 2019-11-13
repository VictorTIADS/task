package com.example.task.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

class SecurityPreferences(context: Context) {


    private  val mSharedPreferences : SharedPreferences = context.getSharedPreferences("tasks",Context.MODE_PRIVATE)


    fun storeString(key:String,value:String) = mSharedPreferences.edit().putString(key,value).apply()

    fun getStoreString(key:String) = mSharedPreferences.getString(key,"")

    fun removeStoredString(key:String) = mSharedPreferences.edit().remove(key).apply()

    fun clear(){
        mSharedPreferences.edit().clear().apply()
    }




}