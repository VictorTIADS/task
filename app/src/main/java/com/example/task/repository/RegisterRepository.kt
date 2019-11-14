package com.example.task.repository

import android.net.Uri
import android.util.Log
import com.example.task.model.MyUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
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

    fun createClassOnFireStore(newUser:MyUser,success: () -> Unit, error: () -> Unit){
        auth.uid?.let { id ->
            fire.collection("users")
                .document(id)
                .set(newUser)
                .addOnSuccessListener {
                    success()

                }
                .addOnFailureListener {
                    error()
                }
        }
    }

    fun getRandomCoding() = UUID.randomUUID().toString()

    fun signInUser(email:String,password:String,success: (FirebaseUser?) -> Unit, error: (String?) -> Unit){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    success(it.result?.user)
                }

            }
            .addOnFailureListener {
                error(it.message)


            }


    }

    fun documentoUser(userId:String,success: (doc:DocumentSnapshot) -> Unit, error: (String?) -> Unit){
        fire.collection("users")
            .document(userId!!)
            .get()
            .addOnSuccessListener {
                success(it)
            }
            .addOnFailureListener {
                error(it.message)
            }

    }



}