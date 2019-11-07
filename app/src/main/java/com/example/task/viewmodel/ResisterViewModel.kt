package com.example.task.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.repository.RegisterRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.auth.User
import java.util.*

class ResisterViewModel : ViewModel() {

    val savePhoto = MutableLiveData<BaseModel<Uri>>()
    val mUser = MutableLiveData<BaseModel<MyUser>>()
    val mMyUser = MutableLiveData<MyUser>()
    val service = RegisterRepository()


    fun signUpUser(userEmail: String, userPwd: String) {
        mUser.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING)
        service.signUpUser(userEmail, userPwd, {
            mUser.value = BaseModel(MyUser(it), BaseModel.Companion.STATUS.SUCCESS)
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

    fun getRandomCode() = UUID.randomUUID().toString()

}