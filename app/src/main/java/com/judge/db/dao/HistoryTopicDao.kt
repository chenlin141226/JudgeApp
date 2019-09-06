package com.judge.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.judge.db.bean.HistoryTopicBean
import io.reactivex.Observable

@Dao
interface HistoryTopicDao {

    @Query("select * from history_topics")
    fun getAllTopics(): Observable<List<HistoryTopicBean>>

    @Insert
    fun insertHistoryTopic(topic: HistoryTopicBean)

    @Delete
    fun deleteHistoryTopic(topic: HistoryTopicBean)
}