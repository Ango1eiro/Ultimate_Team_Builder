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
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.domain.Player

class PlayersListAdapter(
    private val context: Context?,
    private val listOfPlayers: List<Player>,
    private val resource: Int
) : RecyclerView.Adapter<PlayersListAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvPlayerName: TextView = view.findViewById(R.id.tvPlayerName)
        var tvPlayerStats: TextView = view.findViewById(R.id.tvPlayerStats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(resource, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfPlayers[position]
        holder.tvPlayerName.text = item.name
        holder.tvPlayerStats.text = item.stats.toString()
    }

    override fun getItemCount() = listOfPlayers.size

}

