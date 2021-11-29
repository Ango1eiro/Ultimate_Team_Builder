package com.example.anitultimateteambuilder.game_results.game_result

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anitultimateteambuilder.*
import com.example.anitultimateteambuilder.database.AppDatabase
import com.example.anitultimateteambuilder.databinding.GameResultFragmentBinding
import com.example.anitultimateteambuilder.databinding.PlayerFragmentBinding
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.players.PlayersListAdapterDragAndDrop
import com.example.anitultimateteambuilder.players.PlayersListDragListener
import com.example.anitultimateteambuilder.players.player.PlayerFragmentArgs
import com.example.anitultimateteambuilder.players.player.PlayerViewModel
import com.example.anitultimateteambuilder.players.player.PlayerViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class GameResultFragment : Fragment() {

    private lateinit var viewModel: GameResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navController = findNavController()
        val binding = GameResultFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).DatabaseDao
        val viewModelFactory = GameResultViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameResultViewModel::class.java)

        binding.viewModel = viewModel

        val arguments = GameResultFragmentArgs.fromBundle(requireArguments())
        if (arguments.gameResultId == -1L) {
            // Nothing happens
        } else {
            viewModel.getGameResultData(arguments.gameResultId)
        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            viewModel.dateInUtcMillis = it
            binding.tvDate.setText(Date(it).toHealthyString())
        }

        binding.rvTeamOne.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        binding.rvTeamOne.adapter = GameResultAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small_v_v3, ListType.LIST_TEAM_ONE)
        binding.rvTeamOne.setOnDragListener(GameResultDragListener())

        binding.rvTeamTwo.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)
        binding.rvTeamTwo.adapter = GameResultAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small_v_v3, ListType.LIST_TEAM_TWO)
        binding.rvTeamTwo.setOnDragListener(GameResultDragListener())

        viewModel.teamOne.observe(viewLifecycleOwner,{
            binding.rvTeamOne.adapter!!.notifyDataSetChanged()
        })

        viewModel.teamTwo.observe(viewLifecycleOwner,{
            binding.rvTeamTwo.adapter!!.notifyDataSetChanged()
        })

        binding.fab.setOnClickListener {
            GlobalScope.launch {
                viewModel.saveGameResultData(binding.tvTeamOneScore.text.toString(),binding.tvTeamTwoScore.text.toString())
            }

            navController.popBackStack()
        }

        binding.fabDelete.setOnClickListener {
            GlobalScope.launch {
                viewModel.deleteGameResultData()
            }
            navController.popBackStack()
        }

        binding.fabTeamOneAdd.setOnClickListener {
            viewModel.currentTeam = 1
            viewModel.setNavigateToSelection()
        }

        binding.fabTeamTwoAdd.setOnClickListener {
            viewModel.currentTeam = 2
            viewModel.setNavigateToSelection()
        }

        viewModel.navigateToSelection.observe(viewLifecycleOwner){
            if (it) {
                navController.navigate(R.id.action_gameResultFragment_to_playersSelectionFragment)
                viewModel.onNavigatedToSelection()
            }
        }

        viewModel.gameResult.observe(viewLifecycleOwner){
            binding.tvTeamOneScore.setText(it.teamOneScore.toString())
            binding.tvTeamTwoScore.setText(it.teamTwoScore.toString())
            binding.tvDate.setText(it.date.toHealthyString())
        }

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<ArrayList<String>>("usersArray")
            ?.observe(viewLifecycleOwner,
                Observer { result ->
                    run {
                        viewModel.initialiseTeam(result)
                        navController.currentBackStackEntry?.savedStateHandle?.remove<ArrayList<String>>("usersArray")
                    }
                })



        binding.tvDate.setOnClickListener { datePicker.show(parentFragmentManager, "tag") }

        return binding.root
    }
}