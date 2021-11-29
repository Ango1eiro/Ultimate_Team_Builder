package com.example.anitultimateteambuilder.game_results

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anitultimateteambuilder.database.DataBaseGameResult
import com.example.anitultimateteambuilder.database.DatabaseDao
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.database.transformToLight
import com.example.anitultimateteambuilder.domain.GameResult
import com.example.anitultimateteambuilder.domain.GameResultLight
import com.example.anitultimateteambuilder.domain.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GameResultsViewModel(
    val database: DatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToGameResult = MutableLiveData<Boolean>()
    val navigateToGameResult: LiveData<Boolean>
        get() = _navigateToGameResult

    var navigateToGameResultId: Long = -1L

    val dataBaseGameResults = database.getFullGameResults()

    val gameResultsLight = MutableLiveData<List<GameResultLight>>()

    fun updateGameResultsLight() {

        val mDatabaseGameResults =  dataBaseGameResults.value
        if (mDatabaseGameResults !== null) {
            val mGameResultsLight = mutableListOf<GameResultLight>()
            mDatabaseGameResults.forEach { it -> mGameResultsLight.add(it.transformToLight()) }
            gameResultsLight.postValue(mGameResultsLight)
        }

    }

    fun onFabClicked() {
        navigateToGameResultId = -1L
        navigateToGameResult()
    }

    fun navigateToGameResult() {
        _navigateToGameResult.value = true
    }

    fun onNavigatedToGameResult() {
        _navigateToGameResult.value = false
    }

    fun getInitialList() : List<GameResultLight> {
        return if (gameResultsLight.value == null) {
            emptyList<GameResultLight>()
        } else {
            gameResultsLight.value!!
        }
    }

}