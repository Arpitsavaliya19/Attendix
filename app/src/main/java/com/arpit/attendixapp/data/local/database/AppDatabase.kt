package com.arpit.attendixapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arpit.attendixapp.data.local.entity.*
import com.arpit.attendixapp.data.local.dao.*

@Database(
    entities = [SubjectEntity::class, LabEntity::class, StudentEntity::class, StaffEntity::class, UserEntity::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun labDao(): LabDao
    abstract fun studentDao(): StudentDao
    abstract fun staffDao(): StaffDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "attendix_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}