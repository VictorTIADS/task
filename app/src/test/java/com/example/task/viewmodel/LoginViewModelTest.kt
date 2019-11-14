package com.example.task.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.task.model.BaseModel
import com.example.task.model.MyUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()



    @Test
    fun `when viewmodel signin then it should call the repository`(){





    }

    fun viewModelInstance() = LoginViewModel()


}