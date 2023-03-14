package com.khudama.apparchitecturetraining.archs.mvvm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var userListAdapter: UserListAdapter

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var addUserButton: Button
    private lateinit var nameEditText: TextView
    private lateinit var emailEditText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userListAdapter = UserListAdapter(listOf()) // initialize empty adapter
        userRecyclerView.adapter = userListAdapter

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.userList.observe(this) {
            userListAdapter.updateUserList(it)
        }

        addUserButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            viewModel.addUser(name, email)
        }
    }
}