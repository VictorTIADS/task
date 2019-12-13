package com.example.task.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.model.StateLog
import com.example.task.repository.RegisterRepository
import com.example.task.util.CredencialValidator
import com.example.task.util.SecurityPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val service:RegisterRepository) : ViewModel(){

    lateinit var mSharedPreferences: SecurityPreferences
    val user = MutableLiveData<BaseModel<MyUser>>()
    val auth = FirebaseAuth.getInstance()
    val userDocumentLoaded = MutableLiveData<StateLog>()
    val userIsLoged = MutableLiveData<StateLog>()

    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
        isDocumentUserLoaded()
    }

    fun isDocumentUserLoaded() {
        if (CredencialValidator.validateSharedPreferencesData(getIdCurrentUser(),getNameCurrentUser(),getEmailCurrentUser(),getPhotoCurrentUser())) {
            userDocumentLoaded.value = StateLog(StateLog.Companion.STATE.LOADED)
        } else {
            userDocumentLoaded.value = StateLog(StateLog.Companion.STATE.NOTLOADED)
        }
    }

    fun getUserData(){
        user.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING,null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                service.documentoUser(getIdCurrentUser()!!,{
                    storeStringsOnSharedPreferences(it.toObject(MyUser::class.java))
                    user.value = BaseModel(MyUser(auth.currentUser,it.toObject(MyUser::class.java)), BaseModel.Companion.STATUS.SUCCESS,null)
                },{
                    user.value = BaseModel(null, BaseModel.Companion.STATUS.ERROR,it)
                })
            }
        }
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

    fun signOutUser(){
        service.signOutFirebase()
        mSharedPreferences.clear()
        userIsLoged.value = StateLog(StateLog.Companion.STATE.FALSE)
    }
//    viewModel.clearSharedPreferences()
//    FirebaseAuth.getInstance().signOut()
//    startActivity(Intent(baseContext, LoginActivity::class.java))
//    finish()


    fun getIdCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_ID)
    fun getNameCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_NAME)
    fun getEmailCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_EMAIL)
    fun getPhotoCurrentUser() = mSharedPreferences.getStoreString(TaskConstants.KEY.USER_PROFILE)
    fun getCurrentStateUser() = user.value?.status

}