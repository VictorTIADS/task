package com.example.task.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.model.StateLog
import com.example.task.repository.RegisterRepository
import com.example.task.util.SecurityPreferences
import com.example.task.views.mContext
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldPath
import org.jetbrains.anko.design.snackbar

class LoginViewModel : ViewModel() {

    val safeState = MutableLiveData<StateLog>()
    lateinit var mSharedPreferences: SecurityPreferences
    val currentUser = MutableLiveData<BaseModel<MyUser>>()
    private val service = RegisterRepository()





    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
    }

    fun testing():Int{
        return 1
    }


     fun isLogedIn() {
        if (mSharedPreferences.getStoreString(TaskConstants.KEY.USER_ID) != "") {
            safeState.value = StateLog(StateLog.Companion.STATE.LOGDED)
        } else {
            safeState.value = StateLog(StateLog.Companion.STATE.NOTLOGED)
        }

    }

    fun signIn(email:String,password:String){
        currentUser.value = BaseModel(null,BaseModel.Companion.STATUS.LOADING)
        service.signInUser(email,password,{
            currentUser.value = BaseModel(MyUser(it),BaseModel.Companion.STATUS.SUCCESS)
            storeStringsOnSharedPreferences(currentUser.value?.data!!)
        },{

            currentUser.value = BaseModel(null,BaseModel.Companion.STATUS.ERROR)
            Log.i("aspk","ERROR: $it")


        })

    }
    private fun storeStringsOnSharedPreferences(user: MyUser?) {
        user?.let {
            mSharedPreferences.storeString(TaskConstants.KEY.USER_ID, user.userId)
            mSharedPreferences.storeString(TaskConstants.KEY.USER_NAME, user.userName)
            mSharedPreferences.storeString(TaskConstants.KEY.USER_EMAIL, user.userEmail)
            mSharedPreferences.storeString(TaskConstants.KEY.USER_PROFILE, user.userProfile)
        }
    }

}