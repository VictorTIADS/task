package com.example.task.repository

import android.content.Context
import android.content.Intent

fun Context.callActivityFinishingthis(contextEnd:Context){
    startActivity(Intent(this,contextEnd::class.java))
}