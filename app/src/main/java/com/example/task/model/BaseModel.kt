package com.example.task.model

data class BaseModel<T>(var data: T?, val status: STATUS) {
    companion object {
        enum class STATUS {
            LOADING, SUCCESS, ERROR
        }
    }
}