package com.example.anitultimateteambuilder.domain

import com.example.anitultimateteambuilder.PlayerRarity
import com.example.anitultimateteambuilder.TeamStat
import java.sql.Date
import com.squareup.moshi.JsonClass

data class Player(val name: String, val stats: Int, val image: ByteArray?, val rarity: PlayerRarity)
data class GameResult(val id: Long, val date: Date, val teamOne: List<Player>, val teamOneScore: Int, val teamTwo: List<Player>, val teamTwoScore: Int)
data class GameResultLight(val id: Long, val date: Date, val teamOne: List<String>, val teamOneScore: Int, val teamTwo: List<String>, val teamTwoScore: Int)

class Team(private val players: MutableList<Player>){

    fun averageStats() : Float = overallStats() / size()
    fun overallStats() = players.sumOf { it.stats }.toFloat()
    fun size() = players.size
    fun add(p:Player) = players.add(p)
    fun players() = players

}

class DivisionResult(private val teams: List<Team>) {

    fun statsDif(teamStat: TeamStat) : Float {
        var sResult = 1000F
        var bResult = 0F
        for (team in teams) {

            val cResult = when (teamStat){
                TeamStat.TEAM_STAT_AVERAGE -> team.averageStats()
                TeamStat.TEAM_STAT_OVERALL -> team.overallStats()
            }

            if (cResult < sResult) sResult = cResult
            if (cResult > bResult) bResult = cResult


        }

        return bResult - sResult
    }

    fun teams() = teams

}



//@JsonClass(generateAdapter = true)
data class NewsApiAnswer(val status: String, val totalResults: Int, val articles: List<NewsApiArticle>)

//@JsonClass(generateAdapter = true)
data class NewsApiArticle (val author: String, val title: String, val description: String,
                           val url: String, val urlToImage: String, val publishedAt: String)