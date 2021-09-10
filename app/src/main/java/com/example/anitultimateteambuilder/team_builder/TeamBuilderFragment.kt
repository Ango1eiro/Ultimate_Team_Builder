package com.example.anitultimateteambuilder.team_builder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anitultimateteambuilder.LIST_TYPE
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.databinding.PlayersFragmentBinding
import com.example.anitultimateteambuilder.databinding.TeamBuilderFragmentBinding
import com.example.anitultimateteambuilder.players.PlayersListAdapter
import com.example.anitultimateteambuilder.players.PlayersListAdapterDragAndDrop
import com.example.anitultimateteambuilder.players.PlayersListDragListener
import com.example.anitultimateteambuilder.players.PlayersViewModel

class TeamBuilderFragment : Fragment() {

    private lateinit var viewModel: TeamBuilderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = TeamBuilderFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(TeamBuilderViewModel::class.java)
        binding.viewModel = viewModel

        binding.rvPlayersToDistribute.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvPlayersToDistribute.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small,LIST_TYPE.LIST_PLAYERS_TO_DISTRIBUTE)
        binding.rvPlayersToDistribute.setOnDragListener(PlayersListDragListener())

        binding.rvTeamOne.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvTeamOne.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small,LIST_TYPE.LIST_TEAM_ONE)
        binding.rvTeamOne.setOnDragListener(PlayersListDragListener())

        binding.rvTeamTwo.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvTeamTwo.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small,LIST_TYPE.LIST_TEAM_TWO)
        binding.rvTeamTwo.setOnDragListener(PlayersListDragListener())

        binding.rvTeamThree.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvTeamThree.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small, LIST_TYPE.LIST_TEAM_THREE)
        binding.rvTeamThree.setOnDragListener(PlayersListDragListener())

        viewModel.playersToDistribute.observe(viewLifecycleOwner,{
            binding.rvPlayersToDistribute.adapter!!.notifyDataSetChanged()
        })

        viewModel.teamOne.observe(viewLifecycleOwner,{
            binding.rvTeamOne.adapter!!.notifyDataSetChanged()
        })

        viewModel.teamTwo.observe(viewLifecycleOwner,{
            binding.rvTeamTwo.adapter!!.notifyDataSetChanged()
        })

        viewModel.teamThree.observe(viewLifecycleOwner,{
            binding.rvTeamThree.adapter!!.notifyDataSetChanged()
        })

        viewModel.initialiseList()


        return binding.root
    }

}