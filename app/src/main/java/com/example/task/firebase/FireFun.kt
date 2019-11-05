package com.example.task.firebase

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.task.util.ValidationException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.indeterminateProgressDialog
import java.lang.Exception
import java.util.*

lateinit var alert:Unit

fun createFireUser(email:String,senha:String,uri: Uri){
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(object :OnCompleteListener<AuthResult>{
        override fun onComplete(p0: Task<AuthResult>) {

        }

    }).addOnFailureListener(object :OnFailureListener{
        override fun onFailure(e: Exception) {
            ValidationException(e.message!!)
        }
    }).addOnSuccessListener(object :OnSuccessListener<AuthResult>{
        override fun onSuccess(p0: AuthResult?) {
            Log.i("aspk",p0?.user?.email.toString())
            upLoadPhotoToFirebaseStorage(uri)

        }
    })
}

fun getRandomCode() = UUID.randomUUID().toString()

fun upLoadPhotoToFirebaseStorage(uri: Uri) {
    val filename = getRandomCode()
    val ref = FirebaseStorage.getInstance().getReference("/images/" + filename)
    ref.putFile(uri)
        .addOnSuccessListener(object :OnSuccessListener<UploadTask.TaskSnapshot>{
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {

                Log.i("aspk",p0.toString())
            }
        })
        .addOnFailureListener(object :OnFailureListener{
            override fun onFailure(e: Exception) {
                ValidationException(e.message!!)
            }
        })

}

fun TextView.isValid():Boolean{
    return this.text.isNullOrEmpty() || this.text.isNullOrBlank()}

fun TextInputLayout.setError(e:String){
    this.error = e
}