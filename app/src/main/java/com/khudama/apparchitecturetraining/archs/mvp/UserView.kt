package com.khudama.apparchitecturetraining.archs.mvp

// View: displays the data and interacts with the user
interface UserView {
    fun showUserData(userName: String, email: String)
}