package com.example.anitultimateteambuilder.team_builder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.anitultimateteambuilder.ListType
import com.example.anitultimateteambuilder.MyApplication
import com.example.anitultimateteambuilder.ShuffleAlgorithm
import com.example.anitultimateteambuilder.TeamStat
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.domain.DivisionResult
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.domain.Team
import com.example.anitultimateteambuilder.players.PlayersViewModel
import com.example.anitultimateteambuilder.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class TeamBuilderViewModel(
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _amountOfTeams = MutableLiveData<Int>(3)
    val amountOfTeams: LiveData<Int>
        get() = _amountOfTeams

    fun setAmountOfTeams(value: Int) {
        _amountOfTeams.value = value
    }

    private val _playersToDistribute = MutableLiveData<List<Player>>()
    val playersToDistribute : LiveData<List<Player>>
        get() = _playersToDistribute

    private val _teamOne = MutableLiveData<List<Player>>()
    val teamOne: LiveData<List<Player>>
        get() = _teamOne

    private val _teamTwo = MutableLiveData<List<Player>>()
    val teamTwo: LiveData<List<Player>>
        get() = _teamTwo

    private val _teamThree = MutableLiveData<List<Player>>()
    val teamThree: LiveData<List<Player>>
        get() = _teamThree

    private val _navigateToSelection = MutableLiveData<Boolean>()
    val navigateToSelection: LiveData<Boolean>
        get() = _navigateToSelection

    fun setNavigateToSelection() {
        _navigateToSelection.value = true
    }

    fun onNavigatedToSelection() {
        _navigateToSelection.value = false
    }

    fun initialisePlayersToDistribute(namesArray: ArrayList<String>) {
        GlobalScope.launch {
            _playersToDistribute.postValue(database.getPlayersByNames(namesArray).map { it.transform() })
        }
    }

    fun updatePlayersList(listType : ListType, newList: List<Player>) {

        when(listType) {
            ListType.LIST_PLAYERS_TO_DISTRIBUTE -> _playersToDistribute.postValue(newList)
            ListType.LIST_TEAM_ONE -> _teamOne.postValue(newList)
            ListType.LIST_TEAM_TWO -> _teamTwo.postValue(newList)
            ListType.LIST_TEAM_THREE -> _teamThree.postValue(newList)

        }

    }

    fun getPlayersList(listType : ListType) : List<Player> {

        return when(listType) {
            ListType.LIST_PLAYERS_TO_DISTRIBUTE -> _playersToDistribute.value ?: emptyList<Player>()
            ListType.LIST_TEAM_ONE -> _teamOne.value ?: emptyList<Player>()
            ListType.LIST_TEAM_TWO -> _teamTwo.value ?: emptyList<Player>()
            ListType.LIST_TEAM_THREE -> _teamThree.value ?: emptyList<Player>()
        }
    }

    fun cleanTeam(listType : ListType) {

        val players = mutableListOf<Player>()
        players.addAll(getPlayersList(listType))
        players.addAll(getPlayersList(ListType.LIST_PLAYERS_TO_DISTRIBUTE))

        updatePlayersList(listType, listOf())
        updatePlayersList(ListType.LIST_PLAYERS_TO_DISTRIBUTE, players)

    }

    fun shufflePlayers(sa: ShuffleAlgorithm) {
            val allPlayers = getAllPlayers()
            when (sa) {
                ShuffleAlgorithm.SHUFFLE_ALGORITHM_UNIFORM -> shufflePlayersUniformly(allPlayers)
                ShuffleAlgorithm.SHUFFLE_ALGORITHM_RANDOM -> shufflePlayersRandomly(allPlayers)
                ShuffleAlgorithm.SHUFFLE_ALGORITHM_RANDOM_BEST_RESULT -> shufflePlayersRandomlyBestResult(allPlayers)
            }
    }

    fun gatherPlayers() {
        val emptyListOfPlayer = listOf<Player>()
        val allPlayers  = mutableListOf<Player>()

        _playersToDistribute.value?.let { allPlayers.addAll(it) }

        _teamOne.value?.let { allPlayers.addAll(it) }
        _teamOne.postValue(emptyListOfPlayer)

        _teamTwo.value?.let { allPlayers.addAll(it) }
        _teamTwo.postValue(emptyListOfPlayer)

        _teamThree.value?.let { allPlayers.addAll(it) }
        _teamThree.postValue(emptyListOfPlayer)

        _playersToDistribute.postValue(allPlayers)
    }

    private fun getAllPlayers() : List<Player> {
        val allPlayers  = mutableListOf<Player>()
        _playersToDistribute.value?.let { allPlayers.addAll(it) }
        _teamOne.value?.let { allPlayers.addAll(it) }
        _teamTwo.value?.let { allPlayers.addAll(it) }
        _teamThree.value?.let { allPlayers.addAll(it) }

        return allPlayers
    }

    private fun shufflePlayersUniformly(allPlayers : List<Player>){

        val playersToShuffle = allPlayers.sortedByDescending { player: Player -> player.stats }

        val teams = List(amountOfTeams.value!!){ Team(mutableListOf()) }

        playersToShuffle.forEach {
            var nextTeamEligible = false
            var nextTeam = -1
            while (!nextTeamEligible) {
                nextTeam = Random.nextInt(0, amountOfTeams.value!!)
                nextTeamEligible = isNextTeamEligibleByNumber(nextTeam,teams)
            }

            teams[nextTeam].add(it)

        }

        updateTeamsAfterShuffle(teams)

    }

    private fun updateTeamsAfterShuffle(teams: List<Team>) {

        _teamOne.postValue(teams[0].players())
        _teamTwo.postValue(teams[1].players())
        if (amountOfTeams.value!! == 3) {
            _teamThree.postValue(teams[2].players())
        }

        _playersToDistribute.postValue(mutableListOf())

    }

    private fun shufflePlayersRandomly(allPlayers : List<Player>) {

        val playersToShuffle = allPlayers.toMutableList()

        val teams = List(amountOfTeams.value!!){ Team(mutableListOf()) }

        val plOnFieldPerTeam = PreferenceManager.getDefaultSharedPreferences(getApplication<MyApplication>().applicationContext).getString("playersOnFieldPerTeam","5")!!.toInt()

        var remPlayers = allPlayers.size-1

        while (remPlayers >= 0) {
            val cPlayer = playersToShuffle[Random.nextInt(0, remPlayers+1)]

            var nextTeamEligible = false
            var nextTeam = -1
            while (!nextTeamEligible) {
                nextTeam = Random.nextInt(0, amountOfTeams.value!!)
                nextTeamEligible = isNextTeamEligibleByNumber(nextTeam,teams)
            }

            teams[nextTeam].add(cPlayer)
            playersToShuffle.remove(cPlayer)

            remPlayers--
        }

        updateTeamsAfterShuffle(teams)

    }

    private fun shufflePlayersRandomlyBestResult(allPlayers : List<Player>) {

        val plOnFieldPerTeam = PreferenceManager.getDefaultSharedPreferences(getApplication<MyApplication>().applicationContext).getString("playersOnFieldPerTeam","5")!!.toInt()
        val avPlayers = allPlayers.size / amountOfTeams.value!!
        val teamStat = if (avPlayers >= plOnFieldPerTeam) {
            TeamStat.TEAM_STAT_AVERAGE
        } else {
            TeamStat.TEAM_STAT_OVERALL
        }

        val calcNumber = PreferenceManager.getDefaultSharedPreferences(getApplication<MyApplication>().applicationContext).getString("numberOfCalculations","20")!!.toInt()
        val divisionResults = mutableListOf<DivisionResult>()

        for (i in 0..calcNumber) {
            val playersToShuffle = allPlayers.toMutableList()
            val teams = List(amountOfTeams.value!!){ Team(mutableListOf()) }
            var remPlayers = allPlayers.size-1

            while (remPlayers >= 0) {
                val cPlayer = playersToShuffle[Random.nextInt(0, remPlayers+1)]

                var nextTeamEligible = false
                var nextTeam = -1
                while (!nextTeamEligible) {
                    nextTeam = Random.nextInt(0, amountOfTeams.value!!)
                    nextTeamEligible = isNextTeamEligibleByNumber(nextTeam,teams)
                }

                teams[nextTeam].add(cPlayer)
                playersToShuffle.remove(cPlayer)

                remPlayers--
            }

            divisionResults.add(DivisionResult(teams))

        }

        divisionResults.sortBy { div : DivisionResult -> div.statsDif(teamStat)}

        updateTeamsAfterShuffle(divisionResults[0].teams())

    }

    private fun isNextTeamEligibleByNumber(nextTeam:Int, teams: List<Team>) : Boolean {

        val currentTeam = teams[nextTeam]
        val otherTeams = teams.minus(currentTeam)

        var eligible = true

        for (team in otherTeams) {
            if (currentTeam.size() > team.size()) {
                eligible = false
            }
        }

        return eligible

    }

}