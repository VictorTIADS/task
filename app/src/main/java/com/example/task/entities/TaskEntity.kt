package com.example.task.entities

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.Serializable

data class TaskEntity(var taskId:String = "",val userId:String = "", val priorityId: Int = 0, var title:String = "", var description: String = "", var dueDate: String = "", var complete: Boolean = false):Serializable{
    constructor(mTask:TaskEntity?):this(mTask?.taskId?:"",mTask?.userId?:"",mTask?.priorityId?:0,mTask?.title?:"",mTask?.description?:"",mTask?.dueDate?:"",mTask?.complete?:false)
}


/*1 CRITICAL , 2 HIGH, 3 MEDIUM, 4 LOW*/