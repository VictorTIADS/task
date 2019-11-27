package com.example.task.business

import android.content.Context
import com.example.task.constants.TaskConstants
import com.example.task.entities.UserEntity
import com.example.task.repository.UserRepository
import com.example.task.util.SecurityPreferences
import com.example.task.util.ValidationException
import java.lang.Exception

class UserBusiness(var context: Context) {

    private val mUserRepository: UserRepository = UserRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun insert(name: String, email: String, password: String) {


        try {
            checkVazio(name, email, password)
            checkIfEmailExistent(email)
            val userId = mUserRepository.insert(name, email, password)


            //SALVAR DADOS DO CABRA NO SHAREDPREFERENCES
            mSecurityPreferences.storeString(TaskConstants.KEY.USER_ID, userId.toString())
            mSecurityPreferences.storeString(TaskConstants.KEY.USER_NAME, name.toString())
            mSecurityPreferences.storeString(TaskConstants.KEY.USER_EMAIL, email.toString())

        } catch (e: Exception) {
            throw  e
        }


    }

    fun login(email:String,password: String):Boolean{

        val user:UserEntity? = mUserRepository.get(email,password)
        return if(user!=null){
            mSecurityPreferences.storeString(TaskConstants.KEY.USER_ID, user.id.toString())
            mSecurityPreferences.storeString(TaskConstants.KEY.USER_NAME, user.name.toString())
            mSecurityPreferences.storeString(TaskConstants.KEY.USER_EMAIL, user.email.toString())
            true
        }else{
             false
        }

    }


    private fun checkVazio(name: String, email: String, password: String) {
        if (name == "" || email==""|| password=="") {
            throw  ValidationException("Por favor,Informe todos os campos para continuar")
        }

    }

    private fun checkIfEmailExistent(email: String) {
        if (mUserRepository.isEmailExistent(email)) {
            throw throw  ValidationException("Este email já está em uso")

        }

    }
}