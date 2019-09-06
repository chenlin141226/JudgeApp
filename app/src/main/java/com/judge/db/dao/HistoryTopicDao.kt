package com.judge.db.dao

import androidx.room.*
import com.judge.db.bean.HistoryTopicBean
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface HistoryTopicDao {

    @Query("select * from history_topics")
    fun getAllTopics(): Observable<List<HistoryTopicBean>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryTopic(topic: HistoryTopicBean): Single<Long>

    @Delete
    fun deleteHistoryTopic(topic: HistoryTopicBean)

    @Query("delete from history_topics")
    fun deleteAllHistoryTopics()
}