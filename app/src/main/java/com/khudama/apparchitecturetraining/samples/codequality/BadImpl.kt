package com.khudama.apparchitecturetraining.samples.codequality

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//MainActivity.kt
class MainActivity1: ComponentActivity() {

    private var user: User1? = null

    private lateinit var userRepository: UserRepository1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = UserRepository1(this, UserDataFormatter1())

        //View Handling to propagate values to above string variables

        //OnView click populate user with the data from input views
        CoroutineScope(Dispatchers.IO).launch {
            user?.let {
                userRepository.insert(it)
            }
        }
    }
}

//SearchActivity.kt
class SearchActivity1: ComponentActivity() {

    private var id: Int? = null

    private lateinit var userRepository: UserRepository1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepository = UserRepository1(this, UserDataFormatter1())

        //View Handling to propagate values to above string variables

        //OnView click populate user with the data from input views
        CoroutineScope(Dispatchers.IO).launch {
            id?.let {
                userRepository.getUser(it)
            }
        }
    }
}

open class UserDataFormatter1

//UserRepository.kt
class UserRepository1(context: Context, formatter: UserDataFormatter1) {

    private val database: LocalDatabase1 = LocalDatabase1.getDatabase(context)

    suspend fun insert(user: User1): Boolean {
        return database.userDao().insert(user) != 0L
    }

    fun getUser(userId: Int): User1 {
        //apply formatter on the result
        return database.userDao().getUserById(userId)
    }
}

//UserEntity.kt
@Entity(tableName = "users")
data class User1(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String
)

//UserDao.kt
@Dao
interface UserDao1 {
    @Insert
    suspend fun insert(user: User1): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): User1
}

//LocalDatabase.kt
@Database(entities = [User1::class], version = 1, exportSchema = false)
abstract class LocalDatabase1 : RoomDatabase() {
    abstract fun userDao(): UserDao1

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase1? = null

        fun getDatabase(context: Context): LocalDatabase1 {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase1::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}