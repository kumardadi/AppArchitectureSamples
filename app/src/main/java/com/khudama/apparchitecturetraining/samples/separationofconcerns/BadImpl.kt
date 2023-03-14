package com.khudama.apparchitecturetraining.samples.separationofconcerns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class MainActivity1: ComponentActivity() {

    private var name: String = ""
    private var email: String = ""
    private var password: String = ""

    private lateinit var database: LocalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //View Handling to propagate values to above string variables

        CoroutineScope(Dispatchers.IO).launch {
            database = Room.databaseBuilder(
                applicationContext,
                LocalDatabase::class.java, "my-database"
            ).build()
        }

        //OnView click
        //Initial Impl
        GlobalScope.launch(Dispatchers.IO) {
            val response = makeApiCall("https://example.com")
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                // Do something with the response body (e.g. parse JSON)
            } else {
                // Handle the error
            }
        }
        //Updated Impl
        database.userDao().insert(User(name = name, email = email, password = password))
    }

    @Throws(IOException::class)
    private fun makeApiCall(url: String): Response {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .post(
                User(
                    name = name,
                    email = email,
                    password = password
                ).toString().toRequestBody())
            .build()

        return client.newCall(request).execute()
    }

    @Database(entities = [User::class], version = 1, exportSchema = false)
    abstract class LocalDatabase : RoomDatabase() {

        abstract fun userDao(): UserDao
    }

    @Dao
    interface UserDao {
        @Insert
        fun insert(user: User)
    }

    @Entity(tableName = "users")
    data class User(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val email: String,
        val password: String
    )
}