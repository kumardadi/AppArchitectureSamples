package com.khudama.apparchitecturetraining.archs.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository = UserRepository(application)

    val userList: LiveData<List<User>> = userRepository.getUserList()

    fun addUser(name: String, email: String) {
        val user = User(0, name, email) // id is auto-generated in the database
        userRepository.insertUser(user)
    }
}