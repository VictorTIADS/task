package com.example.task.model

import android.net.Uri

data class ValidationCredentialState(val status: STATE,val error:ERROR?,val email:String?,val password:String?,val name:String?,val photo: Uri?){
    companion object {
        enum class STATE {
            SUCCESS,ERROR
        }
        enum class ERROR{
            FALSE,EMAIL,SENHA,NAME,PHOTO,ALL
        }
    }
}