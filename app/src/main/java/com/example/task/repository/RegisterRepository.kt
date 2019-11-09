package com.example.task.repository

import android.net.Uri
import com.example.task.model.MyUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterRepository{

    private val auth = FirebaseAuth.getInstance()
    private val fire = FirebaseFirestore.getInstance()

    fun signUpUser(userEmail:String,userPwd:String, success: (result:FirebaseUser?) -> Unit, error: (message:String?) -> Unit){
        auth.createUserWithEmailAndPassword(userEmail,userPwd)
            .addOnSuccessListener{
                success(it.user)
            }.addOnFailureListener{
                error(it.message)

            }
    }

    fun upLoadPhotoOnFirebase(uriImage:Uri,success: (uriDownload:Uri) -> Unit, error: () -> Unit){
        val filename = getRandomCoding()
        val ref = FirebaseStorage.getInstance().getReference("/images/" + filename)
        ref.putFile(uriImage)
            .addOnSuccessListener{
                ref.downloadUrl.addOnSuccessListener {
                    success(it)
                }
                    .addOnFailureListener {
                        error()
                    }
            }
            .addOnFailureListener{

            }
    }

    fun createClassOnFireStore(newUser:MyUser,success: (id:DocumentReference) -> Unit, error: () -> Unit){
        fire.collection("users")
            .add(newUser)
            .addOnSuccessListener {
                success(it)

            }
            .addOnFailureListener {
                error()
            }
    }
    fun getRandomCoding() = UUID.randomUUID().toString()

}