package com.khudama.apparchitecturetraining.samples.testability

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

//MainActivity.kt
@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private var user: User? = null

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
@AndroidEntryPoint
class SearchActivity: ComponentActivity() {

    private var id: Int? = null

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View Handling to propagate values to above string variables

        //OnView click populate user with the data from input views
        CoroutineScope(Dispatchers.IO).launch {
            id?.let {
                userRepository.getUser(it)
            }
        }
    }
}

@Singleton
open class UserDataFormatter @Inject constructor()

//UserRepository.kt
@Singleton
class UserRepository @Inject constructor(context: Context, formatter: UserDataFormatter) {

    private val database: LocalDatabase = LocalDatabase.getDatabase(context)

    suspend fun insert(user: User): Boolean {
        return database.userDao().insert(user) != 0L
    }

    fun getUser(userId: Int): User {
        //apply formatter on the result
        return database.userDao().getUserById(userId)
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

//UserDao.kt
@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): User
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