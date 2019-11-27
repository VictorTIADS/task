package com.example.task.util

import android.widget.EditText

class CredencialValidator {
    companion object {
        fun validateEmail(email: String): Boolean = email.isNotEmpty() || email.isNotBlank()
        fun validatePassword(password: String): Boolean = password.isNotEmpty() || password.isNotBlank()
        fun validateSharedPreferencesData(userId:String?,userName:String?,userEmail:String?,userProfile:String?):Boolean{
            return !(userId!!.isEmpty()||userName!!.isEmpty()|| userEmail!!.isEmpty()|| userProfile!!.isEmpty())
        }
    }
}

fun EditText.setMessageOfError(text: String) {
    this.error = text
}