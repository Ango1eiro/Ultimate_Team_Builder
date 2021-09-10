package com.example.anitultimateteambuilder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DatabaseDao {

    @Insert
    fun insert(player: DataBasePlayer)

    @Update
    fun update(player: DataBasePlayer)

    @Query("SELECT * from players WHERE name = :key")
    fun get(key: String): DataBasePlayer?

    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllNights(): LiveData<List<DataBasePlayer>>

    @Query("DELETE FROM players")
    fun clear()

}