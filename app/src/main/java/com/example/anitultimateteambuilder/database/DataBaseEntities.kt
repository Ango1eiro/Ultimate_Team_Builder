package com.example.anitultimateteambuilder.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION
import com.example.anitultimateteambuilder.PlayerRarity
import java.lang.NumberFormatException
import java.sql.Date

@Entity(tableName = "players")
data class DataBasePlayer (

    @PrimaryKey
    var name: String = "",

    var stats: Int = 0,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null,

    var rarity: Int = 0

)

@Entity(tableName = "game_results")
data class DataBaseGameResult(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var date: Date = Date(0),

    @ColumnInfo(defaultValue = "0")
    var teamOneScore: Int = 0,

    @ColumnInfo(defaultValue = "0")
    var teamTwoScore: Int = 0

)

@Entity(tableName = "game_results_teams_one", foreignKeys = arrayOf (
    ForeignKey(
        entity = DataBaseGameResult::class,
        parentColumns = ["id"],
        childColumns = ["gameResultId"],
        onDelete = CASCADE
    ),
    ForeignKey(
        entity = DataBasePlayer::class,
        parentColumns = ["name"],
        childColumns = ["player"],
        onDelete = NO_ACTION
    )
))
data class DataBaseGameResultTeamOne(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var gameResultId: Int = 0,
    var player: String = ""

)

@Entity(tableName = "game_results_teams_two", foreignKeys = arrayOf (
    ForeignKey(
        entity = DataBaseGameResult::class,
        parentColumns = ["id"],
        childColumns = ["gameResultId"],
        onDelete = CASCADE
    ),
    ForeignKey(
        entity = DataBasePlayer::class,
        parentColumns = ["name"],
        childColumns = ["player"],
        onDelete = NO_ACTION
    )
))
data class DataBaseGameResultTeamTwo(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var gameResultId: Int = 0,
    var player: String = ""

)

data class DataBaseGameResultFull(
    @Embedded
    var gameResult: DataBaseGameResult? = null,
    @Relation(parentColumn = "id", entityColumn = "gameResultId")
    var teamOne : List<DataBaseGameResultTeamOne>? = listOf(DataBaseGameResultTeamOne()),
    @Relation(parentColumn = "id", entityColumn = "gameResultId")
    var teamTwo : List<DataBaseGameResultTeamTwo>? = listOf(DataBaseGameResultTeamTwo())
)

data class DataBaseGameResultToInsert(
    @Embedded
    var gameResult: DataBaseGameResult? = null,
    var teamOne : List<String>,
    var teamTwo : List<String>
)

data class DataBaseGameResultTeamTwoFull(
    @Embedded
    var gameResultTeam: DataBaseGameResultTeamTwo? = null,
    @Relation(parentColumn = "player", entityColumn = "name")
    var playerFull: DataBasePlayer
)

data class DataBaseGameResultTeamOneFull(
    @Embedded
    var gameResultTeam: DataBaseGameResultTeamOne? = null,
    @Relation(parentColumn = "player", entityColumn = "name")
    var playerFull: DataBasePlayer
)