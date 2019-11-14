package com.example.task.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.model.StateLog
import com.example.task.repository.RegisterRepository
import com.example.task.util.SecurityPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainViewModel : ViewModel(){

    lateinit var mSharedPreferences: SecurityPreferences
    val user = MutableLiveData<BaseModel<MyUser>>()
    private val service = RegisterRepository()
    val auth = FirebaseAuth.getInstance()
    val userDocumentLoaded = MutableLiveData<StateLog>()

    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
        isDocumentUserLoaded()
    }

    fun isDocumentUserLoaded() {
        if (mSharedPreferences.getStoreString(TaskConstants.KEY.USER_PROFILE) != "") {
            userDocumentLoaded.value = StateLog(StateLog.Companion.STATE.LOADED)
        } else {
            userDocumentLoaded.value = StateLog(StateLog.Companion.STATE.NOTLOADED)
        }
    }

    fun getUserData(){
        user.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING)
        service.documentoUser(getIdCurrentUser()!!,{
            user.value = BaseModel(MyUser(auth.currentUser,it.toObject(MyUser::class.java)), BaseModel.Companion.STATUS.SUCCESS)
            if(userDocumentLoaded.value?.status==StateLog.Companion.STATE.NOTLOADED){
                storeStringsOnSharedPreferences(user.value?.data)
            }
        },{
            user.value = BaseModel(null, BaseModel.Companion.STATUS.ERROR)
        })
    }
    private fun storeStringsOnSharedPreferences(user: MyUser?) {
        user?.let {
            mSharedPreferences.storeString(TaskConstants.KEY.USER_NAME, user.userName)
            mSharedPreferences.storeString(TaskConstants.KEY.USER_PROFILE, user.userProfile)
        }
    }

    fun clearSharedPreferences(){
        mSharedPreferences.clear()
    }

    fun getIdCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_ID)
    fun getNameCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_NAME)
    fun getEmailCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_EMAIL)
    fun getPhotoCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_PROFILE)
    fun getCurrentStateUser() = user.value?.status

}