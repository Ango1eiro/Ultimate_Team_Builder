package com.example.anitultimateteambuilder.players.player

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.domain.Player
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlayerViewModel (
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player>
        get() = _player

    var image_uri : Uri? = null
    var image_ba : ByteArray? = null

    fun getPlayerData(playerName: String) {
        GlobalScope.launch {
            _player.postValue(database.get(playerName)!!.transform())
        }
    }

    fun savePlayerData(player: Player) {
        GlobalScope.launch {
            database.insert(player.transform())
        }
    }

}