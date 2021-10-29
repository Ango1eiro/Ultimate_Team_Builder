package com.example.anitultimateteambuilder.players

import android.content.ClipData
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anitultimateteambuilder.*
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.team_builder.TeamBuilderViewModel

class PlayersListAdapterDragAndDrop(
    private val context: Context?,
    val viewModel: TeamBuilderViewModel,
    private val resourceId11: Int,
    val listType: ListType
) : RecyclerView.Adapter<PlayersListAdapterDragAndDrop.ViewHolder>(){

    private val showStats = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showStats",false)

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvPlayerName: TextView = view.findViewById(R.id.tvPlayerName)
        var tvPlayerStats: TextView = view.findViewById(R.id.tvPlayerStats)
        var cvPlayerItemSmall : CardView = view.findViewById(R.id.cvPlayerItemSmall)
        var ivPlayer: ImageView = view.findViewById(R.id.imageViewPlayer)
        var cardInnerLayout : ConstraintLayout = view.findViewById(R.id.cardInnerLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(resourceId11, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = when (listType) {
            ListType.LIST_PLAYERS_TO_DISTRIBUTE -> viewModel.playersToDistribute.value!![position]
            ListType.LIST_TEAM_ONE -> viewModel.teamOne.value!![position]
            ListType.LIST_TEAM_TWO -> viewModel.teamTwo.value!![position]
            ListType.LIST_TEAM_THREE -> viewModel.teamThree.value!![position]
        }

        holder.tvPlayerName.text = getNameForCV(resourceId11,item.name)
        holder.tvPlayerStats.text = if (showStats) item.stats.toString() else "XX"
        holder.ivPlayer.setImageBitmap(byteArrayToImage(item.image))
        holder.cvPlayerItemSmall.setOnLongClickListener {
            val data: ClipData = ClipData.newPlainText("","")
            val shadowBuilder: View.DragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, shadowBuilder, it, 0)
            true
        }
        updateCardBackground(context!!,item.rarity,holder.cardInnerLayout)

        holder.cvPlayerItemSmall.setTag(position)
//        holder.cvPlayerItemSmall.setOnDragListener(PlayersListDragListener());

    }

    override fun getItemCount() : Int {
        return when (listType) {
            ListType.LIST_PLAYERS_TO_DISTRIBUTE -> viewModel.playersToDistribute.value?.size ?: 0
            ListType.LIST_TEAM_ONE -> viewModel.teamOne.value?.size ?: 0
            ListType.LIST_TEAM_TWO -> viewModel.teamTwo.value?.size ?: 0
            ListType.LIST_TEAM_THREE -> viewModel.teamThree.value?.size ?: 0
        }

    }

}

