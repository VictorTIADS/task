package com.example.task.model

data class ValidationCredentialState(val status: STATE,val error:ERROR?,var email:String,val password:String){
    companion object {
        enum class STATE {
            SUCCESS,ERROR
        }
        enum class ERROR{
            FALSE,EMAIL,SENHA,ALL
        }
    }
}