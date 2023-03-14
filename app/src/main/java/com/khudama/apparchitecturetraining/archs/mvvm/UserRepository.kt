package com.khudama.apparchitecturetraining.archs.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

class UserRepository(context: Context) {
    private val userDao: UserDao = LocalDatabase.getDatabase(context).userDao()

    fun getUserList(): LiveData<List<User>> {
        return userDao.getUserList()
    }

    fun insertUser(user: User) {
        userDao.insert(user)
    }
}

//UserDao.kt
@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM users")
    fun getUserList(): LiveData<List<User>>
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