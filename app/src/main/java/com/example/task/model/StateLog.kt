package com.example.task.model

data class StateLog(val status: STATE){
    companion object {
        enum class STATE {
            LOGDED, NOTLOGED
        }
    }
}