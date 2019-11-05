package com.example.task.views

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import com.example.task.constants.TaskConstants
import com.example.task.util.ValidationException
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_resister_acticity.*
import com.example.task.R
import com.example.task.firebase.*
import com.example.task.util.SecurityPreferences
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast


class ResisterActicity : AppCompatActivity() {


    lateinit var uriImage:Uri
    lateinit var mSharedPreferences: SecurityPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resister_acticity)
        txtCadastroPassword.transformationMethod = PasswordTransformationMethod()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initSharedPreferences(this)

        controlEnableComponents(false)




        btnBack.setOnClickListener {
            finish()

        }
        btnCadastrar.setOnClickListener {
            LOAD.startDialog("Por Favor, Aguarde...",this)
            SignUpUser()


        }
        userImage.setOnClickListener {
            selectImageFromGalery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            TaskConstants.REQUEST.CODE_PICTURE->{
                if(data?.data!=null){
                    setImageWithPicasso(data?.data!!)
                    uriImage = data?.data!!
                    controlEnableComponents(true)


                }
            }
        }
    }


    private fun SignUpUser(){
        try {

            val nameUser = txtNome.text.toString()
            val emailUser = txtEmail.text.toString()
            val passwordUser = txtCadastroPassword.text.toString()
            createFireUser(emailUser,passwordUser,uriImage,nameUser,this){
                upLoadPhotoToFirebaseStorage(uriImage,nameUser,emailUser,this){uid,uri->
                    addingFireStoreToUser(uid,nameUser,uri,emailUser,this){
                        storeStringsOnSharedPreferences(it)
                        callMainActivity()
                    }
                }
            }







        }catch (e:ValidationException){
            throw e
        }

    }
    private fun callMainActivity(){
        val callMain = Intent(this,MainActivity::class.java)
        startActivity(callMain)
        finish()

    }

    private fun selectImageFromGalery(){
        val intentCallGalery = Intent(Intent.ACTION_PICK)
        intentCallGalery.type = ("image/*")
        startActivityForResult(intentCallGalery,TaskConstants.REQUEST.CODE_PICTURE)
    }
    private fun setImageWithPicasso(uri:Uri){
        Picasso.get().load(uri).centerCrop().resize(500,500).into(userImage)
    }


    private fun controlEnableComponents(control:Boolean){
        txtNome.isEnabled = control
        txtEmail.isEnabled = control
        txtCadastroPassword.isEnabled = control
        btnCadastrar.isEnabled = control
        if (control) {txtNome.requestFocus()}

    }
    private fun initSharedPreferences(context: Context){
        mSharedPreferences = SecurityPreferences(context)
    }
    private fun storeStringsOnSharedPreferences(user:MyUser){

        mSharedPreferences.storeString(TaskConstants.KEY.USER_ID,user.userId)
        mSharedPreferences.storeString(TaskConstants.KEY.USER_NAME,user.userName)
        mSharedPreferences.storeString(TaskConstants.KEY.USER_EMAIL,user.userEmail)
        mSharedPreferences.storeString(TaskConstants.KEY.USER_PROFILE,user.userProfile)

    }

    object LOAD{

        lateinit var pd:ProgressDialog


        fun startDialog(message:String,context: Context){
            pd = ProgressDialog(context)
            pd.setMessage(message)
            pd.setCancelable(false)
            pd.show()
        }
        fun updateDialogMessage(message: String){
            pd.setMessage(message)

        }

        fun stopLoadingDialog(){
            pd.cancel()
        }




    }









}

