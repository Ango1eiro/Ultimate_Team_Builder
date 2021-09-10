package com.example.anitultimateteambuilder.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.databinding.HomeFragmentBinding
import com.example.anitultimateteambuilder.databinding.PlayersFragmentBinding
import com.example.anitultimateteambuilder.home.HomeViewModel

class PlayersFragment : Fragment() {

    private lateinit var viewModel: PlayersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = PlayersFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.initialiseList()

        binding.listViewPlayers.layoutManager = GridLayoutManager(context, 2)
        binding.listViewPlayers.adapter = PlayersListAdapter(context,binding.viewModel!!.playersList,R.layout.player_item_cv)

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        val sleepTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        binding.setLifecycleOwner(this)

        return binding.root
    }

}