package com.example.task.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.StateLog
import com.example.task.util.SecurityPreferences

class LoginViewModel : ViewModel() {

    val safeState = MutableLiveData<StateLog>()
    lateinit var mSharedPreferences: SecurityPreferences






    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
    }


     fun isLogedIn() {
        if (mSharedPreferences.getStoreString(TaskConstants.KEY.USER_ID) != "") {
            safeState.value = StateLog(StateLog.Companion.STATE.LOGDED)
        } else {
            safeState.value = StateLog(StateLog.Companion.STATE.NOTLOGED)
        }

    }


}