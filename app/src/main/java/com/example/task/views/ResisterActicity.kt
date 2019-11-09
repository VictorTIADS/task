package com.example.task.views

import android.app.ProgressDialog
import android.content.Context
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
import com.example.task.constants.TaskConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_resister_acticity.*
import com.example.task.R
import com.example.task.animation.*
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import com.example.task.util.SecurityPreferences
import com.example.task.viewmodel.ResisterViewModel


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

        controlEnableComponents(false)

        setObservable()


        btnBack.setOnClickListener {
            finish()

        }
        btnCadastrar.setOnClickListener {
            startAnimation()
            SignUpUser()


        }
        userImage.setOnClickListener {
            selectImageFromGalery()

        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            TaskConstants.REQUEST.CODE_PICTURE -> {
                if (data?.data != null) {
                    setImageWithPicasso(data?.data!!)
                    uriImage = data?.data!!
                    controlEnableComponents(true)


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

    }

    private fun animationMaster(){
        userImage.animationMasterSlow {  }
        lblUserName.animationMasterSlow {  }
        lblUserEmail.animationMasterSlow {  }
        lblStatusLoading.animationMasterSlow {  }
        loadingPhoto.animationMasterSlow { callMainActivity() }


    }


    private fun SignUpUser() {

        userName = txtNome.text.toString()
        emailUser = txtEmail.text.toString()
        passwordUser = txtCadastroPassword.text.toString()

        viewModel.signUpUser(emailUser, passwordUser,userName)


//            createFireUser(emailUser,passwordUser,uriImage,nameUser,this){
//                upLoadPhotoToFirebaseStorage(uriImage,nameUser,emailUser,this){uid,uri->
//                    addingFireStoreToUser(uid,nameUser,uri,emailUser,this){
//                        storeStringsOnSharedPreferences(it)
//                        callMainActivity()
//                    }
//                }
//            }


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


    private fun controlEnableComponents(control: Boolean) {
        txtNome.isEnabled = control
        txtEmail.isEnabled = control
        txtCadastroPassword.isEnabled = control
        btnCadastrar.isEnabled = control
        if (control) {
            txtNome.requestFocus()
        }

    }




    private fun hideComponents() {
        toolbar.startAniminHidingComponent {
            toolbar.visibleGone()
            laytxtNome.startAniminHidingComponent {
                laytxtNome.visibleGone()
            }
            laytxtEmail.startAniminHidingComponent {
                laytxtEmail.visibleGone()
            }
            laytxtPassword.startAniminHidingComponent {
                laytxtPassword.visibleGone()
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

    object LOAD {

        lateinit var pd: ProgressDialog


        fun startDialog(message: String, context: Context) {
            pd = ProgressDialog(context)
            pd.setMessage(message)
            pd.setCancelable(false)
            pd.show()
        }

        fun updateDialogMessage(message: String) {
            pd.setMessage(message)

        }

        fun stopLoadingDialog() {
            pd.cancel()
        }


    }



}

