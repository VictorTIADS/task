package com.example.task.model

import com.google.firebase.auth.FirebaseUser

data class MyUser(val userId:String,var userName:String,val userEmail:String,var userProfile:String){

    constructor(fuser: FirebaseUser?) : this (fuser?.uid ?: "", "",fuser?.email ?:  "", "")

}