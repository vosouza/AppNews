package com.evosouza.news.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    val email: String,
    val userName: String,
    val password: String,
    val photo: String?
    //pesquisar tipo foto
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}