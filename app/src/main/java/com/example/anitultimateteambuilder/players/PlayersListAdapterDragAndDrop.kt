package com.example.anitultimateteambuilder.players

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.anitultimateteambuilder.LIST_TYPE
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.team_builder.TeamBuilderViewModel

class PlayersListAdapterDragAndDrop(
    private val context: Context?,
    val viewModel: TeamBuilderViewModel,
    private val resource: Int,
    val listType: LIST_TYPE
) : RecyclerView.Adapter<PlayersListAdapterDragAndDrop.ViewHolder>(){

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvPlayerName: TextView = view.findViewById(R.id.tvPlayerName)
        var tvPlayerStats: TextView = view.findViewById(R.id.tvPlayerStats)
        var cvPlayerItemSmall : CardView = view.findViewById(R.id.cvPlayerItemSmall)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(resource, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = when (listType) {
            LIST_TYPE.LIST_PLAYERS_TO_DISTRIBUTE -> viewModel.playersToDistribute.value!![position]
            LIST_TYPE.LIST_TEAM_ONE -> viewModel.teamOne.value!![position]
            LIST_TYPE.LIST_TEAM_TWO -> viewModel.teamTwo.value!![position]
            LIST_TYPE.LIST_TEAM_THREE -> viewModel.teamThree.value!![position]
        }

        holder.tvPlayerName.text = item.name
        holder.tvPlayerStats.text = item.stats.toString()

        holder.cvPlayerItemSmall.setOnLongClickListener {
            val data: ClipData = ClipData.newPlainText("","")
            val shadowBuilder: View.DragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, shadowBuilder, it, 0)
            true
        }

        holder.cvPlayerItemSmall.setTag(position)
//        holder.cvPlayerItemSmall.setOnDragListener(PlayersListDragListener());

    }

    override fun getItemCount() : Int {
        return when (listType) {
            LIST_TYPE.LIST_PLAYERS_TO_DISTRIBUTE -> viewModel.playersToDistribute.value?.size ?: 0
            LIST_TYPE.LIST_TEAM_ONE -> viewModel.teamOne.value?.size ?: 0
            LIST_TYPE.LIST_TEAM_TWO -> viewModel.teamTwo.value?.size ?: 0
            LIST_TYPE.LIST_TEAM_THREE -> viewModel.teamThree.value?.size ?: 0
        }

    }


//    fun getList() : List<Player> {
//        return viewModel.playersToDistribute.value!!
//    }

    fun updateAndSubmitList(list : List<Player>) {

    }


}

