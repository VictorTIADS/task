package com.example.task.repository

import android.app.AuthenticationRequiredException
import android.net.Uri
import android.util.Log
import com.example.task.entities.TaskEntity
import com.example.task.model.MyUser
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.core.UserData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import java.util.*
import kotlin.collections.ArrayList

class RegisterRepository {

    private val auth = FirebaseAuth.getInstance()
    private val fire = FirebaseFirestore.getInstance()

    fun signUpUser(
        userEmail: String,
        userPwd: String,
        success: (result: FirebaseUser?) -> Unit,
        error: (message: String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(userEmail, userPwd)
            .addOnSuccessListener {
                success(it.user)
            }.addOnFailureListener {
                error(it.message)

            }

    }
    fun signOutFirebase(){
        auth.signOut()
    }
    fun isUserLogedOnFirebase():Boolean{
        return auth.currentUser!=null
    }
    fun removeTaskFromFirestore(taskid:String,userId:String,success: () -> Unit, error: (message: String?) -> Unit){
        fire.collection("users")
            .document(userId)
            .collection("Tasks")
            .document(taskid).delete()
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                error(it.message)
            }
    }


    fun upLoadPhotoOnFirebase(
        uriImage: Uri,
        success: (uriDownload: Uri) -> Unit,
        error: (message: String?) -> Unit
    ) {
        val filename = getRandomCoding()
        val ref = FirebaseStorage.getInstance().getReference("/images/" + filename)
        ref.putFile(uriImage)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    success(it)
                }
                    .addOnFailureListener {
                        error(it.message)
                    }
            }
            .addOnFailureListener {
                error(it.message)
            }
    }

    fun createClassOnFireStore(
        newUser: MyUser,
        success: () -> Unit,
        error: (message: String?) -> Unit
    ) {
        auth.uid?.let { id ->
            fire.collection("users")
                .document(id)
                .set(newUser)
                .addOnSuccessListener {
                    success()

                }
                .addOnFailureListener {
                    error(it.message)

                }
        }
    }

    fun getRandomCoding() = UUID.randomUUID().toString()

    fun signInUser(
        email: String,
        password: String,
        success: (FirebaseUser?) -> Unit,
        error: (String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success(it.result?.user)
                }

            }
            .addOnFailureListener {
                error(it.message)
                //implementar mesagem com error code em portugues
                //val a  = (it as FirebaseAuthException).errorCode
            }


    }

    fun documentoUser(
        userId: String,
        success: (doc: DocumentSnapshot) -> Unit,
        error: (String?) -> Unit
    ) {
        fire.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                success(it)
            }
            .addOnFailureListener {
                error(it.message)
            }

    }

    fun addNewTask(task: TaskEntity, success: () -> Unit, error: (message: String?) -> Unit) {


        fire.collection("users")
            .document("${task.userId}")
            .collection("Tasks")
            .add(task)
            .addOnSuccessListener {


                fire.collection("users")
                    .document("${task.userId}")
                    .collection("Tasks")
                    .document(it.id).update("taskId",it.id)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener { exception ->
                        error(exception.message)
                    }
            }
            .addOnFailureListener {
                error(it.message)


            }


    }


    fun getUserTasksOnFirebase(success: (list: MutableList<TaskEntity>) -> Unit, error: (message: String?) -> Unit) {
        fire.collection("users")
            .document("${auth.uid}")
            .collection("Tasks")
            .get()
            .addOnSuccessListener {

                success(it.toObjects(TaskEntity::class.java))
            }
            .addOnFailureListener {
                error(it.message)
            }
    }

}