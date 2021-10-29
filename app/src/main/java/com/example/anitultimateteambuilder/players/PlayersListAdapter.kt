package com.example.anitultimateteambuilder.players

import android.content.ClipData
import android.content.Context
import android.graphics.drawable.AnimationDrawable
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
import com.example.anitultimateteambuilder.PlayerRarity
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.byteArrayToImage
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.updateCardBackground

class PlayersListAdapter(
    private val context: Context?,
    var listOfPlayers: List<Player>,
    private val resource: Int,
    private val action: (player_name: String) -> Unit
) : RecyclerView.Adapter<PlayersListAdapter.ViewHolder>() {

    private val showStats = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showStats",false)

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvPlayerName: TextView = view.findViewById(R.id.tvPlayerName)
        var tvPlayerStats: TextView = view.findViewById(R.id.tvPlayerStats)
        var cvPlayer: CardView = view.findViewById(R.id.cvPlayer)
        var ivPlayer: ImageView = view.findViewById(R.id.imageViewPlayer)
        var cardInnerLayout : ConstraintLayout = view.findViewById(R.id.cardInnerLayout)
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
        holder.tvPlayerStats.text = if (showStats) item.stats.toString() else "XX"
        holder.ivPlayer.setImageBitmap(byteArrayToImage(item.image))
        holder.cvPlayer.setOnClickListener(){
            action(holder.tvPlayerName.text.toString())
        }
        updateCardBackground(context!!,item.rarity,holder.cardInnerLayout)
    }

    override fun getItemCount() = listOfPlayers.size

}

