package com.example.task.model

data class BaseModel<T>(var data: T?, val status: STATUS,val message:String?) {
    companion object {
        enum class STATUS {
            LOADING, SUCCESS, ERROR
        }
    }
}