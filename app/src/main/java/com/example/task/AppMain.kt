package com.example.task

import android.app.Application
import com.example.task.di.koinInjector
import com.google.firebase.FirebaseApp

class AppMain: Application() {
    override fun onCreate() {
        super.onCreate()
        koinInjector()
        FirebaseApp.initializeApp(this)
    }
}