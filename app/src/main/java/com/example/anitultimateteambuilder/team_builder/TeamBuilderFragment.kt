package com.example.anitultimateteambuilder.team_builder

import OnSwipeTouchListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anitultimateteambuilder.*
import com.example.anitultimateteambuilder.database.AppDatabase
import com.example.anitultimateteambuilder.databinding.TeamBuilderFragmentBinding
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.players.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamBuilderFragment : Fragment() {

    private lateinit var viewModel: TeamBuilderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navController = findNavController()
        val binding = TeamBuilderFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).DatabaseDao
        val viewModelFactory = TeamBuilderViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TeamBuilderViewModel::class.java)
        binding.viewModel = viewModel

        binding.rvPlayersToDistribute.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvPlayersToDistribute.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small_v_v3,ListType.LIST_PLAYERS_TO_DISTRIBUTE)
        binding.rvPlayersToDistribute.setOnDragListener(PlayersListDragListener())

        binding.rvTeamOne.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvTeamOne.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small_h_v3,ListType.LIST_TEAM_ONE)
        binding.rvTeamOne.setOnDragListener(PlayersListDragListener())

        binding.rvTeamTwo.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvTeamTwo.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small_h_v3,ListType.LIST_TEAM_TWO)
        binding.rvTeamTwo.setOnDragListener(PlayersListDragListener())

        binding.rvTeamThree.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvTeamThree.adapter = PlayersListAdapterDragAndDrop(context,binding.viewModel!!,
            R.layout.player_item_cv_small_h_v3, ListType.LIST_TEAM_THREE)
        binding.rvTeamThree.setOnDragListener(PlayersListDragListener())
        binding.rvTeamThree.visibility = View.GONE

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

        viewModel.navigateToSelection.observe(viewLifecycleOwner){
            if (it) {
                navController.navigate(R.id.action_teamBuilderFragment_to_playersSelectionFragment)
                viewModel.onNavigatedToSelection()
            }
        }

        viewModel.amountOfTeams.observe(viewLifecycleOwner,{
            when (it) {
//                2 -> binding.rvTeamThree.visibility = View.GONE
                2 -> {
                    binding.rvTeamThree.visibility = View.GONE
                    if (viewModel.teamThree.value?.isNotEmpty() == true) {
                        viewModel.cleanTeam(ListType.LIST_TEAM_THREE)
                    }
                }
                3 -> binding.rvTeamThree.visibility = View.VISIBLE
            }
        })

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<ArrayList<String>>("usersArray")
            ?.observe(viewLifecycleOwner,
                Observer { result ->
                    run {
                            viewModel.initialisePlayersToDistribute(result)
                            navController.currentBackStackEntry?.savedStateHandle?.remove<ArrayList<String>>("usersArray")
                    }
                })


        binding.clTeamBuilder.setOnTouchListener(object : OnSwipeTouchListener(context,viewModel){
            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeLeft() {
                viewModel.setAmountOfTeams(3)
                super.onSwipeLeft()
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onSwipeRight() {
                viewModel.setAmountOfTeams(2)
                super.onSwipeRight()
            }
        })

        binding.fab.setOnClickListener {
            viewModel.shufflePlayers(ShuffleAlgorithm.SHUFFLE_ALGORITHM_RANDOM_BEST_RESULT)
        }


        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team_builder_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_players -> viewModel.setNavigateToSelection()
            R.id.clean_players -> viewModel.updatePlayersList(ListType.LIST_PLAYERS_TO_DISTRIBUTE,
                listOf())
            R.id.share_teams -> shareTeams()
            R.id.shuffle_players_u -> viewModel.shufflePlayers(ShuffleAlgorithm.SHUFFLE_ALGORITHM_UNIFORM)
            R.id.shuffle_players_r -> viewModel.shufflePlayers(ShuffleAlgorithm.SHUFFLE_ALGORITHM_RANDOM)
            R.id.shuffle_players_rbr -> viewModel.shufflePlayers(ShuffleAlgorithm.SHUFFLE_ALGORITHM_RANDOM_BEST_RESULT)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareTeams() {

        val stringOfTeamsBuilder = StringBuilder()
        if (viewModel.teamOne.value?.isNotEmpty() == true){
            stringOfTeamsBuilder.append("Team one")
            for (pl in viewModel.teamOne.value!!) {
                stringOfTeamsBuilder.append("\n")
                stringOfTeamsBuilder.append(pl.name)
            }
        }
        if (viewModel.teamTwo.value?.isNotEmpty() == true){
            stringOfTeamsBuilder.append("\n")
            stringOfTeamsBuilder.append("\n")
            stringOfTeamsBuilder.append("Team two")
            for (pl in viewModel.teamTwo.value!!) {
                stringOfTeamsBuilder.append("\n")
                stringOfTeamsBuilder.append(pl.name)
            }
        }
        if (viewModel.teamThree.value?.isNotEmpty() == true){
            stringOfTeamsBuilder.append("\n")
            stringOfTeamsBuilder.append("\n")
            stringOfTeamsBuilder.append("Team three")
            for (pl in viewModel.teamThree.value!!) {
                stringOfTeamsBuilder.append("\n")
                stringOfTeamsBuilder.append(pl.name)
            }
        }
        val stringOfTeams = stringOfTeamsBuilder.toString()



        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,stringOfTeams);
        sharingIntent.setType("text/plain");
        sharingIntent.setPackage("com.whatsapp");

            startActivity(sharingIntent);


    }


}