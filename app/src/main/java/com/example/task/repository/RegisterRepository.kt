package com.example.task.repository

import android.net.Uri
import com.example.task.firebase.getRandomCode
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterRepository{

    private val auth = FirebaseAuth.getInstance()

    fun signUpUser(userEmail:String,userPwd:String, success: (result:FirebaseUser?) -> Unit, error: (message:String?) -> Unit){
        auth.createUserWithEmailAndPassword(userEmail,userPwd)
            .addOnSuccessListener{
                success(it.user)
            }.addOnFailureListener{
                error(it.message)

            }
    }

    fun getRandomCoding() = UUID.randomUUID().toString()

    fun upLoadPhotoOnFirebase(uriImage:Uri,success: (uriDownload:Uri) -> Unit, error: () -> Unit){
        val filename = getRandomCoding()
        val ref = FirebaseStorage.getInstance().getReference("/images/" + filename)
        ref.putFile(uriImage)
            .addOnSuccessListener{
                ref.downloadUrl.addOnSuccessListener {
                    success(it)
                }
                    .addOnFailureListener {

                    }
            }
            .addOnFailureListener{

            }
    }

    fun user(success: () -> Unit, error: () -> Unit){
        auth.createUserWithEmailAndPassword("sadas", "dasdas@asda.com")
            .addOnSuccessListener{
                success()
            }.addOnFailureListener{
                error()
            }
    }

}