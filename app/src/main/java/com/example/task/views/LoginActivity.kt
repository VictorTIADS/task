package com.example.task.views

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.task.R
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.viewmodel.LoginViewModel
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_resister_acticity.*

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

    private fun setObservable() {
        viewModel.safeState.observe(this, Observer {
            when (it.status) {
                StateLog.Companion.STATE.LOGDED -> {
                    Log.i("aspk", "USUÁRIO LOGADO")
                    callMainactivity()


                }
                StateLog.Companion.STATE.NOTLOGED -> {
                    Log.i("aspk", "USUÁRIO NÃO LOGADO")
                }
            }
        })
        viewModel.currentUser.observe(this, Observer {
            when (it.status) {
                BaseModel.Companion.STATUS.LOADING -> {
                    controlVisible(BaseModel.Companion.STATUS.LOADING)
                }
                BaseModel.Companion.STATUS.SUCCESS -> {
                    controlVisible(BaseModel.Companion.STATUS.LOADING)
                    callMainactivity()
                }
                BaseModel.Companion.STATUS.ERROR -> {
                    controlVisible(BaseModel.Companion.STATUS.ERROR)

                }
            }
        })
    }

    private fun controlVisible(status: BaseModel.Companion.STATUS) {
        when (status) {
            BaseModel.Companion.STATUS.LOADING -> {
                btnLogin.text = null
                lblCadastrar.isEnabled = false
                btnLogin.isEnabled = false
                loadingSignIn.visibility = View.VISIBLE
            }
            BaseModel.Companion.STATUS.SUCCESS -> {
                btnLogin.text = "LOGIN"
                btnLogin.isEnabled = true
                loadingSignIn.visibility = View.GONE
            }
            BaseModel.Companion.STATUS.ERROR -> {
                btnLogin.text = "LOGIN"
                btnLogin.isEnabled = true
                lblCadastrar.isEnabled = true
                loadingSignIn.visibility = View.GONE
            }


        }


    }

    private fun callMainactivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun configureTextInputPassword() {
        //txtLoginPassword.inputType = TYPE_TEXT_VARIATION_PASSWORD
        txtLoginPassword.transformationMethod = PasswordTransformationMethod()
    }


    fun CallCadastro(view: View) {
        val intentCadastro = Intent(this, ResisterActicity::class.java)
        startActivity(intentCadastro)
    }


    private fun handleLogin() {
        val email = txtLoginEmail.text.toString()
        val password = txtLoginPassword.text.toString()
        viewModel.signIn(email, password)
    }


}
