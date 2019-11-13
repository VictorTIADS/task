package com.example.task.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.repository.RegisterRepository
import com.example.task.util.SecurityPreferences

class ResisterViewModel : ViewModel() {

    val savePhoto = MutableLiveData<BaseModel<Uri>>()
    val mUser = MutableLiveData<BaseModel<MyUser>>()
    val finalUser = MutableLiveData<BaseModel<MyUser>>()
    val service = RegisterRepository()
    lateinit var mSharedPreferences: SecurityPreferences


    fun signUpUser(userEmail: String, userPwd: String,userName: String) {
        mUser.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING)
        service.signUpUser(userEmail, userPwd, {
            mUser.value = BaseModel(MyUser(it), BaseModel.Companion.STATUS.SUCCESS)
            mUser?.value?.data?.userName = userName
        }, {
            mUser.value = BaseModel(null, BaseModel.Companion.STATUS.SUCCESS)
        })
    }

    fun upLoadPhoto(uriImage:Uri) {
        savePhoto.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING)
        service.upLoadPhotoOnFirebase(uriImage,{
            mUser.value?.data?.userProfile = it.toString()
            savePhoto.value = BaseModel(it, BaseModel.Companion.STATUS.SUCCESS)
        }, {

        })
    }

    fun resisterMyUserOnFireStore() {
        finalUser.value = BaseModel(null,BaseModel.Companion.STATUS.LOADING)
        service.createClassOnFireStore(mUser.value?.data!!,{
            finalUser.value = BaseModel(mUser?.value?.data,BaseModel.Companion.STATUS.SUCCESS)
            storeStringsOnSharedPreferences(finalUser.value!!.data!!)

        },{

        })
    }

    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
    }

    private fun storeStringsOnSharedPreferences(user: MyUser) {
        mSharedPreferences.storeString(TaskConstants.KEY.USER_ID, user.userId)
        mSharedPreferences.storeString(TaskConstants.KEY.USER_NAME, user.userName)
        mSharedPreferences.storeString(TaskConstants.KEY.USER_EMAIL, user.userEmail)
        mSharedPreferences.storeString(TaskConstants.KEY.USER_PROFILE, user.userProfile)
    }



}