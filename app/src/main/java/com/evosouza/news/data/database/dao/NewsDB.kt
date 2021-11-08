package com.evosouza.news.data.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.evosouza.news.data.database.Converters
import com.evosouza.news.data.database.NewsDAO
import com.evosouza.news.data.model.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDB: RoomDatabase(){

    abstract fun newsDao(): NewsDAO
    abstract fun userDAO(): UserDAO

    companion object{
        @Volatile
        private var instance: NewsDB? = null
        private var lock = Any()

        private val MIGRATION_1_2: Migration = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                //implement migration
            }
        }

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: createDB(context).also{
                instance = it
            }
        }

        private fun createDB(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NewsDB::class.java,
            "news_db.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

}