package com.example.task.views

import android.content.Intent
import android.os.Bundle
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.task.R
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.model.ValidationCredentialState
import com.example.task.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

     val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel.initSharedPreferences(this)

        configureTextInputPassword()
        setObservable()
        viewModel.isLogedIn()
        btnLogin.setOnClickListener {
            handleLogin()
        }


    }

    private fun setObservable() {
        viewModel.isUserLoged.observe(this, Observer {
            when (it.status) {
                StateLog.Companion.STATE.TRUE -> {
                    Log.i("aspk", "USUÁRIO LOGADO")
                    callMainactivity()


                }
                StateLog.Companion.STATE.FALSE -> {
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
//                    btnLogin.longSnackbar(it.message.toString())
                    supportFragmentManager.beginTransaction().show(ErroStateFragment.newInstance("")).commit()
                }
            }
        })
        viewModel.stateCredentials.observe(this, Observer {
            when (it.error){

                ValidationCredentialState.Companion.ERROR.FALSE ->{
                    it.email?.let { it1 -> it.password?.let { it2 -> viewModel.signIn(it1, it2) } }
                }
                ValidationCredentialState.Companion.ERROR.ALL ->{
                    animateView(textInputLayoutLoginEmail)
                    animateView(textInputLayoutLoginPassword)
                    btnLogin.snackbar("Email e Senha Obrigatórios")
                }
                ValidationCredentialState.Companion.ERROR.EMAIL ->{
                    animateView(textInputLayoutLoginEmail)
                    btnLogin.snackbar("Email Obrigatório")

                }
                ValidationCredentialState.Companion.ERROR.SENHA ->{
                    animateView(textInputLayoutLoginPassword)
                    btnLogin.snackbar("Senha Obrigatória")
                }


            }
        })
    }
    private fun animateView(view: View){
        YoYo.with(Techniques.Shake)
            .duration(500)
            .playOn(view)
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
        txtLoginPassword.inputType = TYPE_TEXT_VARIATION_PASSWORD
        txtLoginPassword.transformationMethod = PasswordTransformationMethod()
    }


    fun CallCadastro(view: View) {
        val intentCadastro = Intent(this, ResisterActicity::class.java)
        startActivity(intentCadastro)
    }


    private fun handleLogin() {
        val email = txtLoginEmail.text.toString()
        val password = txtLoginPassword.text.toString()
        viewModel.validateCredential(email,password)
//        viewModel.signIn(email, password)
//        when {
//            CredencialValidator.validateEmail(email) && CredencialValidator.validatePassword(password) -> {
//                toast("All Right")
//            }
//            !CredencialValidator.validateEmail(email) && !CredencialValidator.validatePassword(
//                password
//            ) -> {
//                txtLoginEmail.error = "Campo Obrigatório"
//                txtLoginPassword.error = "Campo Obrigatório"
//
//            }
//            !CredencialValidator.validateEmail(email) -> {
//                txtLoginEmail.error = "Campo Obrigatório"
//
//            }
//            !CredencialValidator.validatePassword(password) -> {
//                txtLoginPassword.error = "Campo Obrigatório"
//
//            }
//
//
//        }


    }


}
