package com.example.anitultimateteambuilder.players

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.repository.Repository.Companion.getPlayersList
import kotlin.random.Random

class PlayersViewModel(
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToPlayer = MutableLiveData<Boolean>()
    val navigateToPlayer: LiveData<Boolean>
        get() = _navigateToPlayer

    val playersList = mutableListOf<Player>()
    val players = database.getAllPlayers()

    fun initialiseList() {

        playersList.clear()
        playersList.addAll(getPlayersList())

    }

    fun getInitialList() : List<Player> {
        return if (players.value == null) {
            emptyList<Player>()
        } else {
            players.value!!.map { dbp -> dbp.transform() }
        }


    }

    fun onFabClicked() {
        _navigateToPlayer.value = true
    }

    fun onNavigatedToPlayer() {
        _navigateToPlayer.value = false
    }


}