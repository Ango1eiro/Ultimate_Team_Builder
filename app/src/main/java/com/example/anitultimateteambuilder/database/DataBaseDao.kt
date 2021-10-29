package com.example.anitultimateteambuilder.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: DataBasePlayer)

    @Update
    fun update(player: DataBasePlayer)

    @Query("SELECT * from players WHERE name = :key")
    fun get(key: String): DataBasePlayer?

    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllPlayers(): LiveData<List<DataBasePlayer>>

    @Query("SELECT * FROM players WHERE name IN (:names) ORDER BY name ASC")
    fun getPlayersByNames(names: List<String>): List<DataBasePlayer>

    @Query("DELETE FROM players")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameResult: DataBaseGameResult): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameResultTeam: DataBaseGameResultTeamOne)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameResultTeam: DataBaseGameResultTeamTwo)

    @Transaction
    fun insert(gameResultTeamFull: DataBaseGameResultFull){
        val id = insert(gameResultTeamFull.gameResult!!)
        gameResultTeamFull.teamOne!!.forEach {
            insert(DataBaseGameResultTeamOne(0,id.toInt(),it.player))
        }
        gameResultTeamFull.teamTwo!!.forEach {
            insert(DataBaseGameResultTeamTwo(0,id.toInt(),it.player))
        }
    }

    @Query("SELECT * FROM game_results")
    fun getAllGameResults() : LiveData<List<DataBaseGameResult>>

    @Query("SELECT * FROM game_results")
    fun getFullGameResults() : LiveData<List<DataBaseGameResultFull>>



}