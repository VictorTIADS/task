package com.example.task.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.model.StateLog
import com.example.task.model.ValidationCredentialState
import com.example.task.repository.RegisterRepository
import com.example.task.util.CredencialValidator
import com.example.task.util.SecurityPreferences

class ResisterViewModel(val service:RegisterRepository) : ViewModel() {

    val savePhoto = MutableLiveData<BaseModel<Uri>>()
    val mUser = MutableLiveData<BaseModel<MyUser>>()
    val finalUser = MutableLiveData<BaseModel<MyUser>>()
    val stateCredentials = MutableLiveData<ValidationCredentialState>()
    lateinit var mSharedPreferences: SecurityPreferences

    fun validateUserOnResister(
        userEmail: String,
        userPwd: String,
        userName: String
    ) {
        when{
            CredencialValidator.validateEmail(userEmail) && CredencialValidator.validateName(userName) && CredencialValidator.validatePassword(userPwd)-> {
                stateCredentials.value = ValidationCredentialState(ValidationCredentialState.Companion.STATE.SUCCESS,ValidationCredentialState.Companion.ERROR.FALSE,userEmail,userPwd,userName,null)
            }
            !CredencialValidator.validateEmail(userEmail) && !CredencialValidator.validateName(userName) && !CredencialValidator.validatePassword(userPwd)-> {
                stateCredentials.value = ValidationCredentialState(ValidationCredentialState.Companion.STATE.ERROR,ValidationCredentialState.Companion.ERROR.ALL,null,null,null,null)
            }
            !CredencialValidator.validateName(userName) -> {
                stateCredentials.value = ValidationCredentialState(ValidationCredentialState.Companion.STATE.ERROR,ValidationCredentialState.Companion.ERROR.NAME,null,null,null,null)
            }
            !CredencialValidator.validateEmail(userEmail) -> {
                stateCredentials.value = ValidationCredentialState(ValidationCredentialState.Companion.STATE.ERROR,ValidationCredentialState.Companion.ERROR.EMAIL,null,null,null,null)
            }

            !CredencialValidator.validatePassword(userPwd)-> {
                stateCredentials.value = ValidationCredentialState(ValidationCredentialState.Companion.STATE.ERROR,ValidationCredentialState.Companion.ERROR.SENHA,null,null,null,null)
            }
        }




    }




    fun signUpUser(userEmail: String, userPwd: String, userName: String) {
        mUser.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING, null)
        service.signUpUser(userEmail, userPwd, {
            mUser.value = BaseModel(MyUser(it), BaseModel.Companion.STATUS.SUCCESS, null)
            mUser?.value?.data?.userName = userName
        }, {
            mUser.value = BaseModel(null, BaseModel.Companion.STATUS.SUCCESS, it)
        })
    }

    fun upLoadPhoto(uriImage: Uri) {
        savePhoto.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING, null)
        service.upLoadPhotoOnFirebase(uriImage, {
            mUser.value?.data?.userProfile = it.toString()
            savePhoto.value = BaseModel(it, BaseModel.Companion.STATUS.SUCCESS, null)
        }, {
            savePhoto.value = BaseModel(null, BaseModel.Companion.STATUS.SUCCESS, it)
        })
    }

    fun resisterMyUserOnFireStore() {
        finalUser.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING, null)
        service.createClassOnFireStore(mUser.value?.data!!, {
            finalUser.value = BaseModel(mUser?.value?.data, BaseModel.Companion.STATUS.SUCCESS, null)
            storeStringsOnSharedPreferences(finalUser.value!!.data!!)

        }, {
            finalUser.value = BaseModel(null, BaseModel.Companion.STATUS.SUCCESS, it)
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