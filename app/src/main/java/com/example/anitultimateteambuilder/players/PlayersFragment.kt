package com.example.anitultimateteambuilder.players

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
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.database.AppDatabase
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.databinding.HomeFragmentBinding
import com.example.anitultimateteambuilder.databinding.PlayersFragmentBinding
import com.example.anitultimateteambuilder.home.HomeViewModel

class PlayersFragment : Fragment() {

    private lateinit var viewModel: PlayersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navController = findNavController()
        val binding = PlayersFragmentBinding.inflate(inflater)
//        viewModel = ViewModelProviders.of(this).get(PlayersViewModel::class.java)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).DatabaseDao
        val viewModelFactory = PlayersViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayersViewModel::class.java)

        binding.viewModel = viewModel


//        viewModel.initialiseList()

        binding.listViewPlayers.layoutManager = GridLayoutManager(context, 2)
        binding.listViewPlayers.adapter = PlayersListAdapter(context,binding.viewModel!!.getInitialList(),R.layout.player_item_cv) {
            binding.root.findNavController().navigate(
                (R.id.action_playersFragment_to_playerFragment),
                bundleOf("player_name" to it)
            )
        }

        binding.lifecycleOwner = this

        viewModel.players.observe(viewLifecycleOwner){
            (binding.listViewPlayers.adapter as PlayersListAdapter).listOfPlayers = it.map { pl -> pl.transform() }
            binding.listViewPlayers.adapter!!.notifyDataSetChanged()
        }



        viewModel.navigateToPlayer.observe(viewLifecycleOwner,
            Observer<Boolean> { shouldNavigate ->
                if (shouldNavigate == true) {
                    navController.navigate(R.id.action_playersFragment_to_playerFragment)
                    viewModel.onNavigatedToPlayer()
                }
            })

        return binding.root
    }


}