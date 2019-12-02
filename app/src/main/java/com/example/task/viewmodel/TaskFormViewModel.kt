package com.example.task.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.entities.TaskEntity
import com.example.task.model.BaseModel
import com.example.task.repository.RegisterRepository

class TaskFormViewModel() : ViewModel() {


    private val service = RegisterRepository()
    val mTask = MutableLiveData<BaseModel<TaskEntity>>()

    fun addNewTask(task: TaskEntity) {
        mTask.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING, null)
        service.addNewTask(task, {
            mTask.value = BaseModel(task, BaseModel.Companion.STATUS.SUCCESS, null)
        }, {
            mTask.value = BaseModel(null, BaseModel.Companion.STATUS.SUCCESS, it)
        })
    }






}