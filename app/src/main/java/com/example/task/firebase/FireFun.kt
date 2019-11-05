package com.example.task.firebase

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.task.util.ValidationException
import com.example.task.views.ResisterActicity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.indeterminateProgressDialog
import java.lang.Exception
import java.util.*

lateinit var alert:Unit

fun createFireUser(email:String,senha:String,uri: Uri,userName:String){
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
            upLoadPhotoToFirebaseStorage(uri,userName)

        }
    })
}

fun getRandomCode() = UUID.randomUUID().toString()

private fun upLoadPhotoToFirebaseStorage(uri: Uri,userName: String) {
    val filename = getRandomCode()
    val ref = FirebaseStorage.getInstance().getReference("/images/" + filename)
    ref.putFile(uri)
        .addOnSuccessListener(object :OnSuccessListener<UploadTask.TaskSnapshot>{
            override fun onSuccess(newUri: UploadTask.TaskSnapshot?) {

                Log.i("aspk",newUri.toString())

                addingFireStoreToUser(FirebaseAuth.getInstance().uid!!,userName,newUri.toString())
            }
        })
        .addOnFailureListener(object :OnFailureListener{
            override fun onFailure(e: Exception) {
                ValidationException(e.message!!)
            }
        })

}
private fun addingFireStoreToUser(id:String,userName: String,photo: String){
    val newUser = MyUser(id,userName,photo)
    FirebaseFirestore.getInstance().collection("users")
        .add(newUser)
        .addOnSuccessListener(object :OnSuccessListener<DocumentReference>{
            override fun onSuccess(document: DocumentReference?) {
                Log.i("aspk",document?.id.toString())


            }
        })
        .addOnFailureListener(object :OnFailureListener{
            override fun onFailure(e: Exception) {
                Log.i("aspk",e.toString())

            }
        })
}



fun TextView.isValid():Boolean{
    return this.text.isNullOrEmpty() || this.text.isNullOrBlank()}

fun TextInputLayout.setError(e:String){
    this.error = e
}