package com.example.anitultimateteambuilder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class DataBasePlayer (

    @PrimaryKey
    var name: String = "",

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null,

    var stats: Int = 0

)