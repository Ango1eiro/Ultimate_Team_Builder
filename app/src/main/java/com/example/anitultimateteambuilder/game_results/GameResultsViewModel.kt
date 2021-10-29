package com.example.anitultimateteambuilder.game_results

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.anitultimateteambuilder.database.DataBaseGameResult
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.domain.GameResult
import com.example.anitultimateteambuilder.domain.GameResultLight
import com.example.anitultimateteambuilder.domain.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GameResultsViewModel(
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val gameResults = database.getFullGameResults()
//    val gameResultsTeams = database.getAllGameResultTeams()
//    val gameResultsFullLight = MutableLiveData<List<GameResultLight>>()

    fun onFabClicked() {

    }

    fun updateGameResultsFull() {
            val b = 2
//        val lGR = gameResults.value
//        val lGRT = gameResultsTeams.value
//        val lGRF = mutableListOf<GameResultLight>()
//        if (lGR != null && lGRT != null){
//            lGR.forEach { cGR ->
//                val teamOne = lGRT.filter { it.gameResultId == cGR.id && it.teamNumber == 1 }.map { it.player }
//                val teamTwo = lGRT.filter { it.gameResultId == cGR.id && it.teamNumber == 2 }.map { it.player }
//
//                lGRF.add(GameResultLight(id = cGR.id, date = cGR.date, teamOne = teamOne, teamTwo = teamTwo,
//                    teamOneScore = cGR.teamOneScore, teamTwoScore = cGR.teamTwoScore))
//
//            }
//        }
//        gameResultsFullLight.postValue(lGRF)
    }

}