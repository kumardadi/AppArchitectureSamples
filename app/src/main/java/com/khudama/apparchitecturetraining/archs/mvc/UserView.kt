package com.khudama.apparchitecturetraining.archs.mvc

import java.util.Observable
import java.util.Observer

// View: displays the data and interacts with the user
interface UserView : Observer {
    fun showUserData(user: UserModel)
    override fun update(o: Observable?, arg: Any?) {
        if (o is UserModel) {
            showUserData(o)
        }
    }
}