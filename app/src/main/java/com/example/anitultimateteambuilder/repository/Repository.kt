package com.example.anitultimateteambuilder.repository

import com.example.anitultimateteambuilder.PlayerRarity
import com.example.anitultimateteambuilder.domain.Player
import kotlin.random.Random

class Repository {

    companion object {

        fun getPlayersList(): MutableList<Player> {
            return generatePlayersList()
        }

        private fun generatePlayersList(): MutableList<Player> {

            val playersList = mutableListOf<Player>()

            for (i in 1..17) {
                playersList.add(Player(i.toString(), Random.nextInt(0, 100),null, PlayerRarity.PLAYER_RARITY_COMMON))
            }

            return playersList

        }
    }

}