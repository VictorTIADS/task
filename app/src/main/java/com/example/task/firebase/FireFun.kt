package com.example.task.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.TextView
import com.example.task.model.MyUser
import com.example.task.util.ValidationException
import com.example.task.views.ResisterActicity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*




fun createFireUser(email:String,senha:String,uri: Uri,userName:String,success:(mUser:FirebaseUser?)->Unit){
    ResisterActicity.LOAD.updateDialogMessage("Cadastrando...")
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(object :OnCompleteListener<AuthResult>{
        override fun onComplete(p0: Task<AuthResult>) {
            success(p0.result?.user)
        }

    }).addOnFailureListener(object :OnFailureListener{
        override fun onFailure(e: Exception) {
            ValidationException(e.message!!)
        }
    }).addOnSuccessListener(object :OnSuccessListener<AuthResult>{
        override fun onSuccess(p0: AuthResult?) {
            Log.i("aspk",p0?.user?.email.toString())
        }
    })
}

fun getRandomCode() = UUID.randomUUID().toString()

fun upLoadPhotoToFirebaseStorage(uri: Uri,userName: String,email: String,context: Context,success:(uid:String,uriDownload:String)->Unit) {
    ResisterActicity.LOAD.updateDialogMessage("Uploading Foto...")
    val filename = getRandomCode()
    val ref = FirebaseStorage.getInstance().getReference("/images/" + filename)
    ref.putFile(uri)
        .addOnSuccessListener(object :OnSuccessListener<UploadTask.TaskSnapshot>{
            override fun onSuccess(newUri: UploadTask.TaskSnapshot?) {
                ref.downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                    override fun onSuccess(uriDownload: Uri?) {
                        Log.i("aspk",uriDownload.toString())
                        ResisterActicity.LOAD.updateDialogMessage("Upload Sucessfull")
                        success(FirebaseAuth.getInstance().uid!!,uriDownload.toString())
                    }
                })
            }
        })
        .addOnFailureListener(object :OnFailureListener{
            override fun onFailure(e: Exception) {
                ValidationException(e.message!!)
            }
        })

}
 fun addingFireStoreToUser(id:String,userName: String,photo: String,email: String,context: Context,success:(user: MyUser)->Unit){
    ResisterActicity.LOAD.updateDialogMessage("Finalizando Cadastro...")
    val newUser = MyUser(id, userName, email, photo)
    FirebaseFirestore.getInstance().collection("users")
        .add(newUser)
        .addOnSuccessListener(object :OnSuccessListener<DocumentReference>{
            override fun onSuccess(document: DocumentReference?) {
                Log.i("aspk",document?.id.toString())
                ResisterActicity.LOAD.stopLoadingDialog()
                success(newUser)
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

fun verifyAuthentication():Boolean{
   return  FirebaseAuth.getInstance().uid!=null
}

fun signOutFirebase(){
    FirebaseAuth.getInstance().signOut()
}