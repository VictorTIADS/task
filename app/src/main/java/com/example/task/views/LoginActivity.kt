package com.example.task.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.task.R
import com.example.task.business.UserBusiness
import com.example.task.constants.TaskConstants
import com.example.task.model.StateLog
import com.example.task.repository.callActivityFinishingthis
import com.example.task.util.SecurityPreferences
import com.example.task.viewmodel.LoginViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.initSharedPreferences(this)
        FirebaseApp.initializeApp(this)
        configureTextInputPassword()
        setObservable()
        viewModel.isLogedIn()
        btnLogin.setOnClickListener {
            handleLogin()
        }
    }
    private fun setObservable(){
        viewModel.safeState.observe(this, Observer {
            when(it.status){
                StateLog.Companion.STATE.LOGDED ->{
                    Log.i("aspk","USUÁRIO LOGADO")
                    callMainactivity()


                }
                StateLog.Companion.STATE.NOTLOGED ->{
                    Log.i("aspk","USUÁRIO NÃO LOGADO")
                }
            }
        })
    }

    private fun callMainactivity(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }



    private fun configureTextInputPassword(){
        //txtLoginPassword.inputType = TYPE_TEXT_VARIATION_PASSWORD
        txtLoginPassword.transformationMethod = PasswordTransformationMethod()
    }


    fun CallCadastro(view: View) {
        val intentCadastro = Intent(this, ResisterActicity::class.java)
        startActivity(intentCadastro)
    }


    private fun handleLogin() {
        val email = txtLoginEmail.text.toString()
        val senha = txtLoginPassword.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
                finish()
            }
            .addOnFailureListener {

            }
    }


}
