package com.example.anitultimateteambuilder.team_builder.players_selection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.players.PlayersViewModel

class PlayersSelectionViewModelFactory(
    private val dataSource: DatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayersSelectionViewModel::class.java)) {
            return PlayersSelectionViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}