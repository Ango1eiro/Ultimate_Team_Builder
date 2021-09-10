package com.example.anitultimateteambuilder.team_builder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anitultimateteambuilder.LIST_TYPE
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.players.PlayersViewModel
import com.example.anitultimateteambuilder.repository.Repository

class TeamBuilderViewModel : ViewModel() {

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


//    val playersToDistribute = mutableListOf<Player>()
//    val teamOne = mutableListOf<Player>()
//    val teamTwo = mutableListOf<Player>()
//    val teamThree = mutableListOf<Player>()


    fun initialiseList() {

//        playersToDistribute.clear()
//        playersToDistribute.addAll(Repository.getPlayersList())

        _playersToDistribute.value = Repository.getPlayersList()
    }

    fun updatePlayersList(listType : LIST_TYPE, newList: List<Player>) {

        when(listType) {
            LIST_TYPE.LIST_PLAYERS_TO_DISTRIBUTE -> _playersToDistribute.postValue(newList)
            LIST_TYPE.LIST_TEAM_ONE -> _teamOne.postValue(newList)
            LIST_TYPE.LIST_TEAM_TWO -> _teamTwo.postValue(newList)
            LIST_TYPE.LIST_TEAM_THREE -> _teamThree.postValue(newList)

        }

    }

    fun getPlayersList(listType : LIST_TYPE) : List<Player> {

        return when(listType) {
            LIST_TYPE.LIST_PLAYERS_TO_DISTRIBUTE -> _playersToDistribute.value ?: emptyList<Player>()
            LIST_TYPE.LIST_TEAM_ONE -> _teamOne.value ?: emptyList<Player>()
            LIST_TYPE.LIST_TEAM_TWO -> _teamTwo.value ?: emptyList<Player>()
            LIST_TYPE.LIST_TEAM_THREE -> _teamThree.value ?: emptyList<Player>()
        }
    }

}