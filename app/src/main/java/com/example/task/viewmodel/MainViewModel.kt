package com.example.task.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.repository.RegisterRepository
import com.example.task.util.SecurityPreferences

class MainViewModel : ViewModel(){

    lateinit var mSharedPreferences: SecurityPreferences
    var currentUser = MutableLiveData<BaseModel<MyUser>>()
    val service = RegisterRepository()

    init {
        currentUser.value = LoginViewModel().getCurrentUser()
    }



    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
    }
    fun clearSharedPreferences(){
        mSharedPreferences.clear()
    }
    //TODO criar forma de salvar usuario no shared preference prq ta vindo nulo

    fun getNameCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_NAME)
    fun getEmailCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_EMAIL)
    fun getPhotoCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_PROFILE)

}