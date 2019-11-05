package com.example.task.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.task.R
import com.example.task.constants.TaskConstants
import com.example.task.firebase.createFireUser
import com.example.task.firebase.isValid
import com.example.task.firebase.setError
import com.example.task.firebase.upLoadPhotoToFirebaseStorage
import com.example.task.util.ValidationException
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_resister_acticity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast
import org.jetbrains.anko.indeterminateProgressDialog as indeterminateProgressDialog1

class ResisterActicity : AppCompatActivity() {


    lateinit var uriImage:Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resister_acticity)
        txtCadastroPassword.transformationMethod = PasswordTransformationMethod()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        controlEnable(false)

        btnBack.setOnClickListener {
            finish()

        }
        btnCadastrar.setOnClickListener {

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
                    controlEnable(true)


                }
            }
        }
    }


    private fun SignUpUser(){
        try {

            val nameUser = txtNome.text.toString()
            val emailUser = txtEmail.text.toString()
            val passwordUser = txtCadastroPassword.text.toString()
            createFireUser(emailUser,passwordUser,uriImage)






        }catch (e:ValidationException){
            throw e
        }

    }

    private fun selectImageFromGalery(){
        val intentCallGalery = Intent(Intent.ACTION_PICK)
        intentCallGalery.type = ("image/*")
        startActivityForResult(intentCallGalery,TaskConstants.REQUEST.CODE_PICTURE)
    }
    private fun setImageWithPicasso(uri:Uri){
        Picasso.get().load(uri).centerCrop().resize(500,500).into(userImage)
    }


    private fun controlEnable(control:Boolean){
        txtNome.isEnabled = control
        txtEmail.isEnabled = control
        txtCadastroPassword.isEnabled = control
        btnCadastrar.isEnabled = control
        if (control) {txtNome.requestFocus()}

    }





}
