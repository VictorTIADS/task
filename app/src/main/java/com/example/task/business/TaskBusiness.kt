package com.example.task.business

import android.content.Context
import com.example.task.entities.TaskEntity
import com.example.task.repository.TaskRepository

class TaskBusiness(context: Context){
    private  val mTaskRepository:TaskRepository = TaskRepository.getInstance(context)

    fun getList(userId:Int,taskFilter:Int):MutableList<TaskEntity> = mTaskRepository.getList(userId,taskFilter)

    fun insert(taskEntity: TaskEntity) = mTaskRepository.insert(taskEntity)
}