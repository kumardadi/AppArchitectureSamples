package com.khudama.apparchitecturetraining.samples.separationofconcerns

import android.content.Context
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
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

//MainActivity.kt
class MainActivity: ComponentActivity() {

    private var user: User? = null

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = UserRepository(this)

        //View Handling to propagate values to above string variables

        //OnView click populate user with the data from input views
        CoroutineScope(Dispatchers.IO).launch {
            user?.let {
                userRepository.insert(it)
            }
        }
    }
}

//UserRepository.kt
class UserRepository(context: Context) {

    private val apiClient = ApiClient()

    private val database: LocalDatabase = LocalDatabase.getDatabase(context)

    suspend fun insert(user: User): Boolean {
        //Initial Impl
        val response = apiClient.makeApiCall("https://example.com", user)
        //return response.isSuccessful
        //Updated Impl
        return database.userDao().insert(user) != 0L
    }
}

//UserEntity.kt
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String
)

//ApiClient.kt
class ApiClient {

    private val client = OkHttpClient()

    @Throws(Exception::class)
    fun makeApiCall(url: String, user: User): Response {
        val request = Request.Builder()
            .url(url)
            .post(user.toString().toRequestBody())
            .build()
        return client.newCall(request).execute()
    }
}

//UserDao.kt
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long
}

//LocalDatabase.kt
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}