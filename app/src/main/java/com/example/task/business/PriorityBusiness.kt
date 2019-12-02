package com.example.task.business

import android.content.Context
import com.example.task.entities.PriorityEntity
import com.example.task.repository.PriorityRepository

class PriorityBusiness(context: Context) {
    private val mPriorityRepository = PriorityRepository.getInstance(context)



    fun getList():MutableList<PriorityEntity> = mPriorityRepository.getList()
}