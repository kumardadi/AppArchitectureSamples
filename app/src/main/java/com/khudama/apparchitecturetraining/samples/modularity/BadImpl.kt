package com.khudama.apparchitecturetraining.samples.modularity

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
class MainActivity1 : ComponentActivity() {

    private var name: String = ""
    private var email: String = ""
    private var password: String = ""

    private lateinit var database: MainLocalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //View Handling to propagate values to above string variables

        CoroutineScope(Dispatchers.IO).launch {
            database = Room.databaseBuilder(
                applicationContext,
                MainLocalDatabase::class.java, "my-database"
            ).build()
        }

        //OnView click
        database.userDao().insert(MainUser(name = name, email = email, password = password))
    }
}

//LocalDatabase.kt
@Database(entities = [MainUser::class], version = 1, exportSchema = false)
abstract class MainLocalDatabase : RoomDatabase() {
    abstract fun userDao(): MainUserDao
}

//UserDao.kt
@Dao
interface MainUserDao {
    @Insert
    fun insert(user: MainUser)
}

//User.kt
@Entity(tableName = "users")
data class MainUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)

//SearchActivity.kt
class SearchActivity1 : ComponentActivity() {

    private var id: Int? = null

    private lateinit var database: SearchLocalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //View Handling to propagate values to above string variables

        CoroutineScope(Dispatchers.IO).launch {
            database = Room.databaseBuilder(
                applicationContext,
                SearchLocalDatabase::class.java, "my-database"
            ).build()
        }

        //OnView click
        id?.let {
            database.userDao().getUserById(it)
        }
    }
}

//LocalDatabase.kt
@Database(entities = [SearchUser::class], version = 1, exportSchema = false)
abstract class SearchLocalDatabase : RoomDatabase() {

    abstract fun userDao(): SearchUserDao
}

//UserDao.kt
@Dao
interface SearchUserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): SearchUser
}

//User.kt
@Entity(tableName = "users")
data class SearchUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String
)