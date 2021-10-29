package com.example.anitultimateteambuilder.team_builder.players_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.database.AppDatabase
import com.example.anitultimateteambuilder.database.transform
import com.example.anitultimateteambuilder.databinding.PlayersFragmentBinding
import com.example.anitultimateteambuilder.databinding.PlayersSelectionFragmentBinding
import com.example.anitultimateteambuilder.players.PlayersListAdapter
import com.example.anitultimateteambuilder.players.PlayersViewModel
import com.example.anitultimateteambuilder.players.PlayersViewModelFactory

class PlayersSelectionFragment : Fragment() {

    private lateinit var viewModel: PlayersSelectionViewModel
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val navController = findNavController()
        val binding = PlayersSelectionFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).DatabaseDao
        val viewModelFactory = PlayersSelectionViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayersSelectionViewModel::class.java)

        val adapter = PlayersSelectionListAdapter(context,viewModel.getInitialList(),
            R.layout.player_item_cv)

        binding.listViewPlayers.adapter = adapter

        tracker = SelectionTracker.Builder<Long>(
            "selection-1",
            binding.listViewPlayers,
            StableIdKeyProvider(binding.listViewPlayers),
            MyLookup(binding.listViewPlayers),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        tracker?.addObserver(
            object: SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    val nItems:Int? = tracker?.selection?.size()
//                    Toast.makeText(context,nItems.toString(),Toast.LENGTH_LONG).show()

//                    if(nItems!=null && nItems > 0) {
//
//                        // Change title and color of action bar
//
//                        title = "$nItems items selected"
//                        supportActionBar?.setBackgroundDrawable(
//                            ColorDrawable(Color.parseColor("#ef6c00")))
//                    } else {
//
//                        // Reset color and title to default values
//
//                        title = "RVSelection"
//                        supportActionBar?.setBackgroundDrawable(
//                            ColorDrawable(getColor(R.color.colorPrimary)))
//                    }
                }
            })
        adapter.setTracker(tracker)


        binding.viewModel = viewModel
        binding.listViewPlayers.layoutManager = GridLayoutManager(context, 2)



        binding.lifecycleOwner = this

        viewModel.players.observe(viewLifecycleOwner){
            (binding.listViewPlayers.adapter as PlayersSelectionListAdapter).listOfPlayers = it.map { pl -> pl.transform() }
            binding.listViewPlayers.adapter!!.notifyDataSetChanged()
        }

        viewModel.selectionDone.observe(viewLifecycleOwner){
            if (it) {
                val usersArray = ArrayList<String>()
                tracker!!.selection.forEach {
                    usersArray.add(viewModel.players.value!![it.toInt()].name)
                }
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "usersArray",
                    usersArray
                )
                navController.popBackStack()
                viewModel.onSelectionDone()
            }
        }



        return binding.root
    }



}