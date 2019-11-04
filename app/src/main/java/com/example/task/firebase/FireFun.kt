package com.example.task.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*

fun createFireUser(email:String,senha:String){
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(object :OnCompleteListener<AuthResult>{
        override fun onComplete(task: Task<AuthResult>) {
            Log.i("aspk",task.result?.user?.uid.toString())
        }
    }).addOnFailureListener(object :OnFailureListener{
        override fun onFailure(e: Exception) {
            Log.i("aspk",e.toString())
        }
    }).addOnSuccessListener(object :OnSuccessListener<AuthResult>{
        override fun onSuccess(p0: AuthResult?) {
            Log.i("aspk",p0?.user?.email.toString())
        }
    })
}

fun getRandomCode() = UUID.randomUUID().toString()

fun upLoadPhotoToFirebaseStorage(uri: Uri) {
    val fielname = getRandomCode()
    val ref = FirebaseStorage.getInstance().getReference("/images/" + fielname)
    ref.putFile(uri)
}
