package com.example.anitultimateteambuilder.players

import androidx.lifecycle.ViewModel
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.repository.Repository.Companion.getPlayersList
import kotlin.random.Random

class PlayersViewModel : ViewModel() {

    val playersList = mutableListOf<Player>()

    fun initialiseList() {

        playersList.clear()
        playersList.addAll(getPlayersList())

    }


}