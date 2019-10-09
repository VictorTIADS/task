package com.example.task.views

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.task.R
import com.example.task.business.UserBusiness
import com.example.task.constants.TaskConstants
import com.example.task.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.longSnackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var mUserBusiness: UserBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserBusiness = UserBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)
        isLogedIn()
        setContentView(R.layout.activity_login)
        //txtLoginPassword.inputType = TYPE_TEXT_VARIATION_PASSWORD
        txtLoginPassword.transformationMethod = PasswordTransformationMethod()

        btnLogin.setOnClickListener {
            handleLogin()
        }
    }


    fun CallCadastro(view: View) {
        val intentCadastro = Intent(this, ResisterActicity::class.java)
        startActivity(intentCadastro)
    }



    private fun handleLogin(){
        val email = txtLoginEmail.text.toString()
        val senha = txtLoginPassword.text.toString()

        if(mUserBusiness.login(email,senha)){
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
            finish()
        }else{
            btnLogin.longSnackbar("Email ou Senha Inválidos")
        }

    }

    private fun isLogedIn(){
        if(mSecurityPreferences.getStoreString(TaskConstants.KEY.USER_ID)!=""){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}
