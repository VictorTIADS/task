package com.example.task.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.constants.TaskConstants
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.model.StateLog
import com.example.task.repository.RegisterRepository
import com.example.task.util.SecurityPreferences
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldPath

class LoginViewModel : ViewModel() {

    val safeState = MutableLiveData<StateLog>()
    lateinit var mSharedPreferences: SecurityPreferences
    val currentUser = MutableLiveData<BaseModel<MyUser>>()
    val service = RegisterRepository()







    fun initSharedPreferences(context: Context) {
        mSharedPreferences = SecurityPreferences(context)
        //TODO Configurar nome email e foto no hearder
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
            getDocumentUserOnFireStore(it)



        },{

            currentUser.value = BaseModel(null,BaseModel.Companion.STATUS.ERROR)

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
    private fun getDocumentUserOnFireStore(fireUser:FirebaseUser?){

        service.documentoUser(fireUser,{
            Log.i("aspk",it.data?.keys.toString())
            currentUser.value?.data = MyUser(fireUser, it.toObject(MyUser::class.java))
            storeStringsOnSharedPreferences(currentUser.value?.data!!)
        },{

        })
    }

    fun getCurrentUser() = currentUser.value
//ASDASDASDASDASDASDASDASD@asdASDASDASLJHLKDFGLKSAGDLKHFGASDLKHFGASJLDGFSADGLFGASDHFA.COMDSAJFHADSH.CO

}