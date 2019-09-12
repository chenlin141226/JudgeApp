package com.judge.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.judge.db.bean.HistoryTopicBean
import com.judge.db.dao.HistoryTopicDao

@Database(entities = [HistoryTopicBean::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyTopicDao(): HistoryTopicDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /**
         * ！！！数据库没有做用户区分！！！
         */
        private fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, "AppDatabase").build()
        }
    }
}