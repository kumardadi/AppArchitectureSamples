package com.khudama.apparchitecturetraining.archs.mvp

// Presenter: acts as an intermediary between the Model and View
class UserPresenter(private val userView: UserView) {
    private val userModel = UserModel()
    fun getUserData() {
        userView.showUserData(userModel.userName, userModel.userEmail)
    }

    fun setUserName(name: String) {
        userModel.userName = name
    }

    fun setUserEmail(email: String) {
        userModel.userEmail = email
    }
}