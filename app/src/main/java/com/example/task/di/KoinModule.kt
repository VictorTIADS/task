package com.example.task.di

import com.example.task.repository.RegisterRepository
import com.example.task.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val injection = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ResisterViewModel(get()) }
    viewModel { TaskFormViewModel(get()) }
    viewModel { TaskListFragmentViewModel(get()) }
    factory { RegisterRepository(get()) }
    single { FirebaseAuth.getInstance() }

}







