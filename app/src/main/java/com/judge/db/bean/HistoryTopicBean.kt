package com.judge.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity(tableName = "history_topics")
data class HistoryTopicBean(
    @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
    var topicTitle: String?,
    var topicAuthor: String?,
    var surfedTime: String
)