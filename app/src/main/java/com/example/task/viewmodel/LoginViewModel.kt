package com.example.task.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.model.StateLog
import com.example.task.model.ValidationCredentialState
import com.example.task.repository.RegisterRepository
import com.example.task.util.CredencialValidator.Companion.validateEmail
import com.example.task.util.CredencialValidator.Companion.validatePassword
import com.example.task.util.SecurityPreferences

class LoginViewModel : ViewModel() {

    val isUserLoged = MutableLiveData<StateLog>()
    lateinit var mSharedPreferences: SecurityPreferences
    val currentUser = MutableLiveData<BaseModel<MyUser>>()
    private val service = RegisterRepository()
    val stateCredentials = MutableLiveData<ValidationCredentialState>()


    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
    }


    fun isLogedIn() {
        if (service.isUserLogedOnFirebase()) {
            isUserLoged.value = StateLog(StateLog.Companion.STATE.TRUE)
        } else {
            isUserLoged.value = StateLog(StateLog.Companion.STATE.FALSE)
        }

    }

    fun validateCredential(email: String, password: String) {

        when {
            validateEmail(email) && validatePassword(password) -> {
                stateCredentials.value = ValidationCredentialState(
                    ValidationCredentialState.Companion.STATE.SUCCESS,
                    ValidationCredentialState.Companion.ERROR.FALSE,
                    email,
                    password,
                    null,
                    null
                )
            }
            !validateEmail(email) && !validatePassword(password) -> {
                stateCredentials.value = ValidationCredentialState(
                    ValidationCredentialState.Companion.STATE.ERROR,
                    ValidationCredentialState.Companion.ERROR.ALL,
                    email,
                    password,
                    null,
                    null
                )

            }
            !validateEmail(email) -> {
                stateCredentials.value = ValidationCredentialState(
                    ValidationCredentialState.Companion.STATE.ERROR,
                    ValidationCredentialState.Companion.ERROR.EMAIL,
                    email,
                    password,
                    null,
                    null

                )

            }
            !validatePassword(password) -> {
                stateCredentials.value = ValidationCredentialState(
                    ValidationCredentialState.Companion.STATE.ERROR,
                    ValidationCredentialState.Companion.ERROR.SENHA,
                    email,
                    password,
                    null,
                    null
                )

            }


        }
    }

    fun signIn(email: String, password: String) {
        currentUser.value = BaseModel(null, BaseModel.Companion.STATUS.LOADING,null)
        service.signInUser(email, password, {
            currentUser.value = BaseModel(MyUser(it), BaseModel.Companion.STATUS.SUCCESS,null)
            storeStringsOnSharedPreferences(currentUser.value?.data!!)
        }, {

            currentUser.value = BaseModel(null, BaseModel.Companion.STATUS.ERROR,it)
            Log.i("aspk", "ERROR: $it")


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