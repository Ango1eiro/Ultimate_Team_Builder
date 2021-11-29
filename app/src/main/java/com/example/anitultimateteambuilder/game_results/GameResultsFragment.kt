package com.example.anitultimateteambuilder.game_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.database.AppDatabase
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.databinding.GameResultsFragmentBinding
import com.example.anitultimateteambuilder.players.PlayersListAdapter

class GameResultsFragment : Fragment() {

    private lateinit var viewModel: GameResultsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()
        val binding = GameResultsFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).DatabaseDao
        val viewModelFactory = GameResultsViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameResultsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.listViewGameResults.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        binding.listViewGameResults.adapter = GameResultsAdapter(binding.viewModel!!,context,binding.viewModel!!.getInitialList(),
            R.layout.game_result_item)

        viewModel.dataBaseGameResults.observe(viewLifecycleOwner){
            viewModel.updateGameResultsLight()
        }

        viewModel.gameResultsLight.observe(viewLifecycleOwner){
            (binding.listViewGameResults.adapter as GameResultsAdapter).listOfGameResultsLight = it
            binding.listViewGameResults.adapter!!.notifyDataSetChanged()
        }

        viewModel.navigateToGameResult.observe(viewLifecycleOwner,
            Observer<Boolean> { shouldNavigate ->
                if (shouldNavigate == true) {
                    navController.navigate(R.id.action_gameResultsFragment_to_gameResultFragment,bundleOf("game_result_id" to viewModel.navigateToGameResultId))
                    viewModel.onNavigatedToGameResult()
                }
            })

        return binding.root
    }

}