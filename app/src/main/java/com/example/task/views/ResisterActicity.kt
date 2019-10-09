package com.example.task.views

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.task.R
import com.example.task.business.UserBusiness
import com.example.task.util.ValidationException
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_resister_acticity.*
import org.jetbrains.anko.design.snackbar
import java.lang.Exception

class ResisterActicity : AppCompatActivity() {


    private lateinit var mUserBusiness: UserBusiness


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resister_acticity)
        txtCadastroPassword.transformationMethod = PasswordTransformationMethod()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        //EVENTOS
        mUserBusiness = UserBusiness(this)


        btnBack.setOnClickListener {
            finish()

        }

        btnCadastrar.setOnClickListener {
            handleSave()
        }


    }


    private fun handleSave() {



        try {

            val name = txtNome.text.toString()
            val email = txtEmail.text.toString()
            val password = txtCadastroPassword.text.toString()

            //faz a inserção do usuario
            mUserBusiness.insert(name, email, password)
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
            finish()
        } catch (e: ValidationException) {

            btnCadastrar.snackbar(e.message.toString())

        }

        catch (e: Exception) {
            Toast.makeText(this, "Algo de errado aconteceu", Toast.LENGTH_LONG).show()
        }


    }


}
