package com.example.anitultimateteambuilder.players.player

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anitultimateteambuilder.database.DataBaseGameResultFull
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.database.transformToLight
import com.example.anitultimateteambuilder.domain.GameResultLight
import com.example.anitultimateteambuilder.domain.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlayerViewModel (
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player>
        get() = _player

    var image_uri : Uri? = null
    var image_ba : ByteArray? = null

    var navigateToGameResultId = MutableLiveData(-1L)

    val dataBaseGameResults = MutableLiveData<List<DataBaseGameResultFull>>()

    val gameResultsLight = MutableLiveData<List<GameResultLight>>()

    fun updateGameResultsLight() {

        val mDatabaseGameResults =  dataBaseGameResults.value
        if (mDatabaseGameResults !== null) {
            val mGameResultsLight = mutableListOf<GameResultLight>()
            mDatabaseGameResults.forEach { it -> mGameResultsLight.add(it.transformToLight()) }
            gameResultsLight.postValue(mGameResultsLight)
        }

    }

    fun getInitialList() : List<GameResultLight> {
        return if (gameResultsLight.value == null) {
            emptyList<GameResultLight>()
        } else {
            gameResultsLight.value!!
        }
    }

    fun getPlayerData(playerName: String) {
        GlobalScope.launch {
            _player.postValue(database.get(playerName)!!.transform())
            dataBaseGameResults.postValue(database.getFullGameResultsOfPlayer(playerName))
        }
    }

    fun savePlayerData(player: Player) {
        GlobalScope.launch {
            database.insert(player.transform())
        }
    }

    fun deletePlayerData(name: String) {
        GlobalScope.launch {
            database.deletePlayer(name)
        }
    }

    fun getNextPlayerName(next: Boolean) :String? {
        var nextPlayerName: String? = null
        runBlocking {
            val job = launch(Dispatchers.Default) { val cPlayer = player.value
                if (cPlayer != null) {
                    if (next) {
                        nextPlayerName = database.getNextPlayer(cPlayer.name)?.name
                    } else {
                        nextPlayerName = database.getPreviousPlayer(cPlayer.name)?.name
                    }
                } else {
                    null
                }
            }
            job.join()

        }
        return nextPlayerName
    }

}