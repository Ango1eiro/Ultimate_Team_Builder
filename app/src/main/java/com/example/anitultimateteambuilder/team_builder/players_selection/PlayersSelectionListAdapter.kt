package com.example.anitultimateteambuilder.team_builder.players_selection

import android.content.ClipData
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.anitultimateteambuilder.PlayerRarity
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.byteArrayToImage
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.updateCardBackground
import com.google.android.material.card.MaterialCardView

class PlayersSelectionListAdapter(
    private val context: Context?,
    var listOfPlayers: List<Player>,
    private val resource: Int
) : RecyclerView.Adapter<PlayersSelectionListAdapter.ViewHolder>() {

    private val showStats = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showStats",false)

    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvPlayerName: TextView = view.findViewById(R.id.tvPlayerName)
        var tvPlayerStats: TextView = view.findViewById(R.id.tvPlayerStats)
        var cvPlayer: CardView = view.findViewById(R.id.cvPlayer)
        var ivPlayer: ImageView = view.findViewById(R.id.imageViewPlayer)
        var cardInnerLayout : ConstraintLayout = view.findViewById(R.id.cardInnerLayout)

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object: ItemDetailsLookup.ItemDetails<Long>() {

                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long = itemId

            }

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
        val parent = holder.tvPlayerName.parent.parent as MaterialCardView
        if(tracker!!.isSelected(position.toLong())) {

            parent.strokeWidth = 5
            parent.setStrokeColor(context!!.resources.getColor(R.color.yellow))

        } else {
            // Reset color to white if not selected
            parent.strokeWidth = 0
            parent.setStrokeColor(Color.WHITE)
        }
        updateCardBackground(context!!,item.rarity,holder.cardInnerLayout)

    }

    override fun getItemCount() = listOfPlayers.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

}

class MyLookup(private val rv: RecyclerView)
    : ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent)
            : ItemDetails<Long>? {

        val view = rv.findChildViewUnder(event.x, event.y)
        if(view != null) {
            return (rv.getChildViewHolder(view) as PlayersSelectionListAdapter.ViewHolder)
                .getItemDetails()
        }
        return null

    }
}

