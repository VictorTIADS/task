package com.example.task.model

data class StateLog(val status: STATE){
    companion object {
        enum class STATE {
            LOGDED, NOTLOGED, LOADING, SUCCESS, ERROR,LOADED,NOTLOADED,LOGEDOUT,FALSE,TRUE,WANTIMAGE,DONTWANTIMAGE,IMAGESELECTED,IMAGEUNSELECTED
        }
    }
}