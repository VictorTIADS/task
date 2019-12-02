package com.example.task.util

import android.net.Uri
import android.widget.EditText
import com.example.task.model.StateLog

class CredencialValidator {
    companion object {
        fun validateEmail(email: String): Boolean = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun validateName(name: String): Boolean = name.isNotEmpty() || name.isNotBlank()
        fun validateUri(uri: Uri): Boolean = uri != Uri.EMPTY
        fun validatePassword(password: String): Boolean = password.isNotEmpty() && (password.length > 6)

        fun validateSharedPreferencesData(
            userId: String?,
            userName: String?,
            userEmail: String?,
            userProfile: String?
        ): Boolean {
            return !(userId!!.isEmpty() || userName!!.isEmpty() || userEmail!!.isEmpty() || userProfile!!.isEmpty())
        }
    }
}

fun EditText.setMessageOfError(text: String) {
    this.error = text
}