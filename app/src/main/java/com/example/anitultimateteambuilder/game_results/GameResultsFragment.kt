package com.example.anitultimateteambuilder.game_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

        binding.listViewGameResults.layoutManager = GridLayoutManager(context, 2)
//        binding.listViewGameResults.adapter = GameResultsAdapter(context,binding.viewModel!!.getInitialList(),
//            R.layout.player_item_cv) {
//            binding.root.findNavController().navigate(
//                (R.id.action_playersFragment_to_playerFragment),
//                bundleOf("player_name" to it)
//            )
//        }

        viewModel.gameResults.observe(viewLifecycleOwner){
            viewModel.updateGameResultsFull()
//            (binding.listViewGameResults.adapter as GameResultsAdapter).listOfGameResults = it.map { gr -> gr.transform() }
//            binding.listViewGameResults.adapter!!.notifyDataSetChanged()
        }

//        viewModel.gameResultsTeams.observe(viewLifecycleOwner){
//            viewModel.updateGameResultsFull()
//            (binding.listViewGameResults.adapter as GameResultsAdapter).listOfGameResults = it.map { gr -> gr.transform() }
//            binding.listViewGameResults.adapter!!.notifyDataSetChanged()
//        }

        return binding.root
    }

}