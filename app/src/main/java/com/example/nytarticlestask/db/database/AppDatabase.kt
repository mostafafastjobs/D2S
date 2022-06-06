package com.example.nytarticlestask.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nytarticlestask.db.dao.ArticlesDataDao
import com.example.nytarticlestask.db.entity.ArticlesTable
import org.koin.core.component.KoinComponent


@Database(
    entities = [
        ArticlesTable::class,
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase(), KoinComponent {

    abstract fun articlesDataDao(): ArticlesDataDao

    companion object {

        private const val DATABASE_NAME: String = "com.example.nytarticlestask.db.database.AppDatabase"
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {

                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                }
            }
            return INSTANCE as AppDatabase

        }

    }
}