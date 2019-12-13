package com.example.task.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.entities.TaskEntity
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.repository.RegisterRepository

class TaskListFragmentViewModel(val service:RegisterRepository):ViewModel(){

    val mTaskLists = MutableLiveData<BaseModel<MutableList<TaskEntity>>>()
    val stateTask = MutableLiveData<StateLog>()

    fun getListOfTasks(){
        mTaskLists.value = BaseModel(null,BaseModel.Companion.STATUS.LOADING,null)
        service.getUserTasksOnFirebase({
            mTaskLists.value = BaseModel(it,BaseModel.Companion.STATUS.SUCCESS,null)
            if(it.isEmpty()){
                mTaskLists.value = BaseModel(it,BaseModel.Companion.STATUS.SUCCESS,"EMPTY_TASKS")
            }
        },{
            mTaskLists.value = BaseModel(null,BaseModel.Companion.STATUS.ERROR,it)
        })
    }

    fun removeItemFromTheList(taskid:String,userId:String){
        stateTask.value = StateLog(StateLog.Companion.STATE.LOADING)
        service.removeTaskFromFirestore(taskid,userId,{
            stateTask.value = StateLog(StateLog.Companion.STATE.SUCCESS)
        }){
            stateTask.value = StateLog(StateLog.Companion.STATE.ERROR)
        }
    }
}