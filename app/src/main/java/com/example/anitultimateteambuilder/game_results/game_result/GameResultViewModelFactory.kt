package com.example.anitultimateteambuilder.game_results.game_result

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.players.player.PlayerViewModel

class GameResultViewModelFactory (
    private val dataSource: DatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameResultViewModel::class.java)) {
            return GameResultViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}