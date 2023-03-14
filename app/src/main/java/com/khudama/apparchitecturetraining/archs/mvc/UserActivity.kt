package com.khudama.apparchitecturetraining.archs.mvc

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.widget.doAfterTextChanged

// Activity: instantiates and interacts with the Controller and View
class UserActivity : ComponentActivity(), UserView {
    private lateinit var controller: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Model
        val userModel = UserModel()

        // Set up the View
        setContentView(R.layout.activity_user)

        // Set up the Controller
        controller = UserController(userModel, this)

        // Set up event handlers for user input
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        nameEditText.doAfterTextChanged { text ->
            controller.setUserName(text.toString())
        }
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        emailEditText.doAfterTextChanged { text ->
            controller.setUserEmail(text.toString())
        }
    }

    override fun onStart() {
        super.onStart()

        // Get the initial data and display it in the View
        controller.getUserData()
    }

    override fun showUserData(user: UserModel) {
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        nameTextView.text = user.userName
        emailTextView.text = user.userEmail
    }
}

// Controller: acts as an intermediary between the Model and View
class UserController(private val userModel: UserModel, private val userView: UserView) {
    fun getUserData() {
        userView.showUserData(userModel)
    }

    fun setUserName(name: String) {
        userModel.userName = name
    }

    fun setUserEmail(email: String) {
        userModel.userEmail = email
    }
}