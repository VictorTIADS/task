package com.example.task.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.task.constants.TaskConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_resister_acticity.*
import com.example.task.R
import com.example.task.animation.*
import com.example.task.model.BaseModel
import com.example.task.model.StateLog
import com.example.task.model.ValidationCredentialState
import com.example.task.viewmodel.ResisterViewModel
import org.jetbrains.anko.design.indefiniteSnackbar
import org.jetbrains.anko.design.snackbar


class ResisterActicity : AppCompatActivity() {

    lateinit var uriImage: Uri
    lateinit var viewModel: ResisterViewModel
    lateinit var userName: String
    lateinit var emailUser: String
    lateinit var passwordUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_resister_acticity)
        txtCadastroPassword.transformationMethod = PasswordTransformationMethod()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        viewModel = ViewModelProviders.of(this).get(ResisterViewModel::class.java)
        viewModel.initSharedPreferences(this)




        setObservable()
        controlEnableComponents(StateLog.Companion.STATE.IMAGEUNSELECTED)


        btnBack.setOnClickListener {
            finish()

        }
        btnCadastrar.setOnClickListener {

            validateUserInfo()


        }
        userImage.setOnClickListener {
            selectImageFromGalery()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            TaskConstants.REQUEST.CODE_PICTURE -> {
                if (data?.data != null) {
                    setImageWithPicasso(data?.data!!)
                    uriImage = data?.data!!
                    controlEnableComponents(StateLog.Companion.STATE.IMAGESELECTED)
                }
            }
        }
    }

    fun setObservable() {

        viewModel.mUser.observe(this, Observer {
            when (it.status) {
                BaseModel.Companion.STATUS.LOADING -> {
                    Log.i("aspk", "LOADING AUTHENTICATION")
                    animStatusLoading("Carregando Autenticação...")
                }
                BaseModel.Companion.STATUS.SUCCESS -> {
                    Log.i("aspk", "SUCCESS AUTHENTICATION")
                    hideStatusLoading()
                    viewModel.upLoadPhoto(uriImage)
                }
                BaseModel.Companion.STATUS.ERROR -> {
                    Log.i("aspk", "ERROR AUTHENTICATION")
                }
            }
        })
        viewModel.savePhoto.observe(this, Observer {
            when (it.status) {
                BaseModel.Companion.STATUS.LOADING -> {
                    Log.i("aspk", "LOADING UPLOAD PHOTO")
                    animStatusLoading("Carregando Foto...")


                }
                BaseModel.Companion.STATUS.SUCCESS -> {
                    Log.i("aspk", "SUCCESS UPLOADING PHOTO")
                    hideStatusLoading()
                    viewModel.resisterMyUserOnFireStore()


                }
                BaseModel.Companion.STATUS.ERROR -> {
                    Log.i("aspk", "ERROR UPLOADING PHOTO")
                }
            }
        })
        viewModel.finalUser.observe(this, Observer {
            when (it.status) {
                BaseModel.Companion.STATUS.LOADING -> {
                    Log.i("aspk", "LOADING FIRESTORE USER")
                    animStatusLoading("Finalizado Autenticação...")

                }
                BaseModel.Companion.STATUS.SUCCESS -> {
                    Log.i("aspk", "SUCCESS FIRESTORE USER")
                    animStatusLoading("Bem Vindo(a)...")
                    animationMaster()
                }
                BaseModel.Companion.STATUS.ERROR -> {
                    Log.i("aspk", "ERROR FIRESTORE USER")
                }
            }
        })
        viewModel.stateCredentials.observe(this, Observer {
            when (it.error){
                ValidationCredentialState.Companion.ERROR.FALSE -> {
                    startAnimation()
                    viewModel.signUpUser(it.email!!,it.password!!,it.name!!)
                }
                ValidationCredentialState.Companion.ERROR.ALL -> {
                    animateView(textInputLayoutResisterName)
                    animateView(textInputLayoutResisterEmail)
                    animateView(textInputLayoutResisterPassword)
                    btnCadastrar.snackbar("Campos Obrigatórios")
                }
                ValidationCredentialState.Companion.ERROR.NAME -> {
                    animateView(textInputLayoutResisterName)
                    btnCadastrar.snackbar("Nome Obrigatório")
                }
                ValidationCredentialState.Companion.ERROR.EMAIL -> {
                    animateView(textInputLayoutResisterEmail)
                    btnCadastrar.snackbar("Preencha o Email Corretamente")
                }
                ValidationCredentialState.Companion.ERROR.SENHA -> {
                    animateView(textInputLayoutResisterPassword)
                    btnCadastrar.snackbar("Senha deve conter Min: 6 Caracteres")
                }
            }
        })
    }

    private fun animateView(view: View){
        YoYo.with(Techniques.Shake)
            .duration(500)
            .playOn(view)
    }

    private fun validateUserInfo() {
        userName = txtNome.text.toString()
        emailUser = txtEmail.text.toString()
        passwordUser = txtCadastroPassword.text.toString()
        viewModel.validateUserOnResister(emailUser,passwordUser,userName)
    }

    private fun callMainActivity() {
        val callMain = Intent(this, MainActivity::class.java)
        startActivity(callMain)
        finish()
    }

    private fun selectImageFromGalery() {
        val intentCallGalery = Intent(Intent.ACTION_PICK)
        intentCallGalery.type = ("image/*")
        startActivityForResult(intentCallGalery, TaskConstants.REQUEST.CODE_PICTURE)
    }

    private fun setImageWithPicasso(uri: Uri) {
        Picasso.get().load(uri).centerCrop().resize(500, 500).into(userImage)
    }



    private fun controlEnableComponents(control: StateLog.Companion.STATE) {
        val userPhotoAnimation = YoYo.with(Techniques.Bounce).duration(1500)
        when (control){
            StateLog.Companion.STATE.IMAGEUNSELECTED -> {
                txtNome.isEnabled = false
                txtEmail.isEnabled = false
                userImage.isEnabled = true
                txtCadastroPassword.isEnabled = false
                btnCadastrar.isEnabled = false
                btnCadastrar.indefiniteSnackbar("Clique na câmera para selecionar uma foto de perfil.")
                txtNome.requestFocus()
                userPhotoAnimation.repeat(5).playOn(userImage)
            }
            StateLog.Companion.STATE.IMAGESELECTED -> {
                txtNome.isEnabled = true
                txtEmail.isEnabled = true
                btnCadastrar.snackbar("Ok.. Agora continue colocando seus dados.")
                txtCadastroPassword.isEnabled = true
                btnCadastrar.isEnabled = true
                txtNome.requestFocus()
            }
        }



    }
    private fun hideComponents() {
        toolbar.startAniminHidingComponent {
            toolbar.visibleGone()
            textInputLayoutResisterName.startAniminHidingComponent {
                textInputLayoutResisterName.visibleGone()
            }
            textInputLayoutResisterEmail.startAniminHidingComponent {
                textInputLayoutResisterEmail.visibleGone()
            }
            textInputLayoutResisterPassword.startAniminHidingComponent {
                textInputLayoutResisterPassword.visibleGone()
            }
            lblInfoCad.startAniminHidingComponent {
                lblInfoCad.visibleGone()
            }
            btnCadastrar.startAniminHidingComponent {
                btnCadastrar.visibleGone()
            }
        }
    }

    private fun animStatusLoading(text:String){
        lblStatusLoading.text = text
        lblStatusLoading.startAnimnLoadingStatusMoving {}
    }

    private fun hideStatusLoading(){
        lblStatusLoading.startAnimnLoadingStatusHiding {  }
    }

    private fun startAnimation() {
        hideComponents()
        userImage.startAniminMovingComponent { }
        lblUserName.text = txtNome.text.toString()
        lblUserEmail.text = txtEmail.text.toString()
        lblUserName.startAniminShowingComponentLabels { }
        lblUserEmail.startAniminShowingComponentLabels { }
        loadingPhoto.startAniminShowingComponentLoader { }

    }

    private fun animationMaster(){
        animationYoyo(userImage){}
        animationYoyo(lblUserName){}
        animationYoyo(lblUserEmail){}
        animationYoyo(lblStatusLoading){}
        animationYoyo(loadingPhoto){callMainActivity()}
    }
    private fun animationYoyo(view:View,cabo:()->Unit){
        YoYo.with(Techniques.SlideOutLeft)
            .onEnd {
                cabo()
            }
            .pivotY(-200f)
            .duration(500)
            .playOn(view)
    }


}

