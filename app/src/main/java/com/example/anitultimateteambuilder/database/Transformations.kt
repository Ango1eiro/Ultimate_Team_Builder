package com.example.anitultimateteambuilder.database

import androidx.room.TypeConverter
import com.example.anitultimateteambuilder.PlayerRarity
import com.example.anitultimateteambuilder.domain.GameResult
import com.example.anitultimateteambuilder.domain.GameResultLight
import com.example.anitultimateteambuilder.domain.Player
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.sql.Date


fun DataBasePlayer.transform() = Player(
    name = this.name,
    stats = this.stats,
    image = this.image,
    rarity = PlayerRarity.values()[this.rarity]
)

fun Player.transform() = DataBasePlayer(
    name = this.name,
    stats = this.stats,
    image = this.image,
    rarity = this.rarity.ordinal
)

fun GameResult.transform() = DataBaseGameResultToInsert(
    DataBaseGameResult(this.id,this.date,this.teamOneScore,this.teamTwoScore),
    teamOne = this.teamOne.map { it.name },
    teamTwo = this.teamTwo.map { it.name }
)

fun DataBaseGameResultFull.transformToLight() = GameResultLight(
    id = this.gameResult!!.id,
    date = this.gameResult!!.date,
    teamOneScore = this.gameResult!!.teamOneScore,
    teamTwoScore = this.gameResult!!.teamTwoScore,
    teamOne = this.teamOne!!.map { it.player },
    teamTwo = this.teamTwo!!.map { it.player }

)



class Converters {

    @TypeConverter
    fun fromList(value : List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}