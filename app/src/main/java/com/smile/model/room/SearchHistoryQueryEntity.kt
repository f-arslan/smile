package com.smile.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smile.util.Constants.DB_TABLE_SEARCH_HISTORY_QUERY

@Entity(tableName = DB_TABLE_SEARCH_HISTORY_QUERY)
data class SearchHistoryQueryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String = "",
    val timeStamp: Long = 0L,
)
