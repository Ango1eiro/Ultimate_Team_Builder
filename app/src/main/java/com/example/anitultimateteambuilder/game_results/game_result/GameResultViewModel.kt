package com.example.anitultimateteambuilder.game_results.game_result

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anitultimateteambuilder.ListType
import com.example.anitultimateteambuilder.database.*
import com.example.anitultimateteambuilder.domain.GameResult
import com.example.anitultimateteambuilder.domain.Player
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Date
import kotlin.collections.ArrayList

class GameResultViewModel (
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _teamOne = MutableLiveData<List<Player>>()
    val teamOne: LiveData<List<Player>>
        get() = _teamOne

    private val _teamTwo = MutableLiveData<List<Player>>()
    val teamTwo: LiveData<List<Player>>
        get() = _teamTwo

    var currentTeam = 0
    var dateInUtcMillis = 0L

    private val _navigateToSelection = MutableLiveData<Boolean>()
    val navigateToSelection: LiveData<Boolean>
        get() = _navigateToSelection

    fun setNavigateToSelection() {
        _navigateToSelection.value = true
    }

    fun onNavigatedToSelection() {
        _navigateToSelection.value = false
    }

    fun getGameResultData(gameResultId: Long) {
        GlobalScope.launch {
            val gameResultLight = database.getGameResultFull(gameResultId)!!.transformToLight()
            val mTeamOne = database.getPlayersByNames(gameResultLight.teamOne).map { dbp -> dbp.transform() }
            val mTeamTwo = database.getPlayersByNames(gameResultLight.teamTwo).map { dbp -> dbp.transform() }
            val mGameResult = GameResult(gameResultLight.id,gameResultLight.date, mTeamOne, gameResultLight.teamOneScore,mTeamTwo, gameResultLight.teamTwoScore)
            _gameResult.postValue(mGameResult)

            dateInUtcMillis = mGameResult.date.time
            _teamOne.postValue(mGameResult.teamOne)
            _teamTwo.postValue(mGameResult.teamTwo)
        }
    }

    fun saveGameResultData(teamOneScore: String, teamTwoScore: String) {
        GlobalScope.launch {
            val gameResultToSave = GameResult(gameResult.value?.id ?: 0, Date(dateInUtcMillis),teamOne.value!!,teamOneScore.toInt(),teamTwo.value!!,teamTwoScore.toInt())
            database.insert(gameResultToSave.transform())
        }
    }

    fun deleteGameResultData() {
        val gameId = gameResult.value?.id ?: 0
        if (gameId != 0L)
        {
            database.deleteGameResult(gameId)
        }
    }

    fun updatePlayersList(listType : ListType, newList: List<Player>) {


        when(listType) {
            ListType.LIST_TEAM_ONE -> _teamOne.postValue(newList)
            ListType.LIST_TEAM_TWO -> _teamTwo.postValue(newList)
            else -> {}
        }

    }

    fun getPlayersList(listType : ListType) : List<Player> {

        return when(listType) {
            ListType.LIST_TEAM_ONE -> _teamOne.value ?: emptyList<Player>()
            ListType.LIST_TEAM_TWO -> _teamTwo.value ?: emptyList<Player>()
            else -> listOf()
        }
    }

    fun initialiseTeam(namesArray: ArrayList<String>) {
        GlobalScope.launch {
            when (currentTeam) {
                1 -> _teamOne.postValue(database.getPlayersByNames(namesArray).map { it.transform() })
                2 -> _teamTwo.postValue(database.getPlayersByNames(namesArray).map { it.transform() })
            }

        }
    }

}