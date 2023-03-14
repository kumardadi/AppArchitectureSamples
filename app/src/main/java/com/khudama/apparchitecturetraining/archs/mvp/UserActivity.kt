package com.khudama.apparchitecturetraining.archs.mvp

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.widget.doAfterTextChanged

// Activity: instantiates and interacts with the Presenter and View
class UserActivity : ComponentActivity(), UserView {
    private lateinit var presenter: UserPresenter

    private lateinit var nameEditText: TextView
    private lateinit var emailEditText: TextView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the View

        // Set up the Presenter
        presenter = UserPresenter(this)

        // Set up event handlers for user input
        nameEditText = findViewById<EditText>(R.id.nameEditText)
        (nameEditText as EditText).doAfterTextChanged { text ->
            presenter.setUserName(text.toString())
        }
        emailEditText = findViewById<EditText>(R.id.emailEditText)
        emailEditText.doAfterTextChanged { text ->
            presenter.setUserEmail(text.toString())
        }
    }

    override fun onStart() {
        super.onStart()

        // Get the initial data and display it in the View
        presenter.getUserData()
    }

    override fun showUserData(userName: String, email: String) {
        nameTextView.text = userName
        emailTextView.text = email
    }
}