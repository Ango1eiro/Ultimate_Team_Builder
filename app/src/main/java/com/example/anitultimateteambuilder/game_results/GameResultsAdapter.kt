package com.example.anitultimateteambuilder.game_results

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
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anitultimateteambuilder.*
import com.example.anitultimateteambuilder.domain.GameResultLight
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.players.player.PlayerViewModel

class GameResultsAdapter(private val viewmodel: AndroidViewModel,
                         private val context: Context?,
                         var listOfGameResultsLight: List<GameResultLight>,
                         private val resource: Int) : RecyclerView.Adapter<GameResultsAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var tvDate: TextView = view.findViewById(R.id.tvDate)
        var tvTeamOne: TextView = view.findViewById(R.id.tvTeamOne)
        var tvTeamTwo: TextView = view.findViewById(R.id.tvTeamTwo)
        var tvTeamOneScore: TextView = view.findViewById(R.id.tvTeamOneScore)
        var tvTeamTwoScore: TextView = view.findViewById(R.id.tvTeamTwoScore)
        var cvGameResult: CardView = view.findViewById(R.id.cvGameResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(resource, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfGameResultsLight[position]
        holder.tvDate.text = item.date.toHealthyString()
        holder.tvTeamOne.text = item.teamOne.joinToString(separator = "\n") { getFirstName(it,12) }
        holder.tvTeamTwo.text = item.teamTwo.joinToString(separator = "\n") { getFirstName(it,12) }
        holder.tvTeamOneScore.text = item.teamOneScore.toString()
        holder.tvTeamTwoScore.text = item.teamTwoScore.toString()
        holder.cvGameResult.setOnClickListener {
            if (viewmodel is GameResultsViewModel) {
                viewmodel.navigateToGameResultId = item.id
                viewmodel.navigateToGameResult()
            }
            if (viewmodel is PlayerViewModel) {
                viewmodel.navigateToGameResultId.postValue(item.id)
            }
        }
    }

    override fun getItemCount() = listOfGameResultsLight.size

}

