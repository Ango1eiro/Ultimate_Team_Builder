package com.example.anitultimateteambuilder.team_builder.players_selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.repository.Repository

class PlayersSelectionViewModel (
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _selectionDone = MutableLiveData<Boolean>()
    val selectionDone: LiveData<Boolean>
        get() = _selectionDone

    val players = database.getAllPlayers()

    fun getInitialList() : List<Player> {
        return if (players.value == null) {
            emptyList<Player>()
        } else {
            players.value!!.map { dbp -> dbp.transform() }
        }


    }

    fun onFabClicked() {
        _selectionDone.value = true
    }

    fun onSelectionDone() {
        _selectionDone.value = false
    }

}