package com.example.anitultimateteambuilder.database

import android.os.FileObserver.DELETE
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

    @Query("SELECT * from game_results WHERE id = :key")
    fun getGameResultFull(key: Long): DataBaseGameResultFull?

    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllPlayers(): LiveData<List<DataBasePlayer>>

    @Query("SELECT * FROM players WHERE name IN (:names) ORDER BY name ASC")
    fun getPlayersByNames(names: List<String>): List<DataBasePlayer>

    @Query("DELETE FROM players")
    fun clear()

    @Query("SELECT * from players  where name > :key ORDER BY name ASC LIMIT 1")
    fun getNextPlayer(key: String) : DataBasePlayer?

    @Query("SELECT * from players  where name < :key ORDER BY name DESC LIMIT 1")
    fun getPreviousPlayer(key: String) : DataBasePlayer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameResult: DataBaseGameResult): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameResultTeam: DataBaseGameResultTeamOne)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gameResultTeam: DataBaseGameResultTeamTwo)

    @Transaction
    fun insert(gameResultTeamFull: DataBaseGameResultToInsert){
        val id = insert(gameResultTeamFull.gameResult!!)
        gameResultTeamFull.teamOne.forEach {
            insert(DataBaseGameResultTeamOne(0,id.toInt(),it))
        }
        gameResultTeamFull.teamTwo.forEach {
            insert(DataBaseGameResultTeamTwo(0,id.toInt(),it))
        }
    }

    @Query("SELECT * FROM game_results")
    fun getAllGameResults() : LiveData<List<DataBaseGameResult>>

    @Query("SELECT * FROM game_results")
    fun getFullGameResults() : LiveData<List<DataBaseGameResultFull>>

    @Query("SELECT * FROM game_results WHERE ID IN (SELECT gameResultId from game_results_teams_one where player = :player UNION SELECT gameResultId from game_results_teams_two where player = :player) ")
    fun getFullGameResultsOfPlayer(player: String) : List<DataBaseGameResultFull>

    @Query("DELETE FROM game_results WHERE id = :gameResultId")
    fun deleteGameResult(gameResultId:Long)



}