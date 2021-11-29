package com.example.anitultimateteambuilder.game_results.game_result


import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.example.anitultimateteambuilder.ListType
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.domain.Player
import com.example.anitultimateteambuilder.repository.Repository.Companion.getPlayersList


class GameResultDragListener : View.OnDragListener {
    private var isDropped = false

    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                isDropped = true
                var positionTarget = -1
                val viewSource: View = event.localState as View
                val viewId: Int = v.getId()
                val flItem: Int = R.id.cvPlayerItemSmall
                val rvTeamOne: Int = R.id.rvTeamOne
                val rvTeamTwo: Int = R.id.rvTeamTwo
                when (viewId) {
                    flItem, rvTeamOne, rvTeamTwo -> {
                        val target: RecyclerView
                        val source = viewSource.parent.parent as RecyclerView

                        when (viewId) {
                            rvTeamOne -> target = v.getRootView().findViewById(rvTeamOne)
                            rvTeamTwo -> target = v.getRootView().findViewById(rvTeamTwo)
                            else -> {
                                return true
                            }
                        }
                        if (target == source) return true

                        val adapterSource: GameResultAdapterDragAndDrop = source.adapter as GameResultAdapterDragAndDrop
                        val positionSource = viewSource.tag as Int

                        val listSource: List<Player> = when (adapterSource.listType){
                            ListType.LIST_TEAM_ONE -> adapterSource.viewModel.teamOne.value!!
                            ListType.LIST_TEAM_TWO -> adapterSource.viewModel.teamTwo.value!!
                            else -> listOf<Player>()
                        }

                        val player: Player = listSource[positionSource]

                        val newSourceList = mutableListOf<Player>()
                        newSourceList.addAll(listSource)
                        newSourceList.removeAt(positionSource)

                        adapterSource.viewModel.updatePlayersList(adapterSource.listType,newSourceList)

                        val adapterTarget: GameResultAdapterDragAndDrop = target.adapter as GameResultAdapterDragAndDrop
                        val listTarget: List<Player> = adapterTarget.viewModel.getPlayersList(adapterTarget.listType)

                        val newTargetList = mutableListOf<Player>()
                        newTargetList.addAll(listTarget)
                        newTargetList.add(player)

                        adapterTarget.viewModel.updatePlayersList(adapterTarget.listType,newTargetList)



//                        adapterSource.viewModel.playersToDistribute.value!!
//                            listSource.removeAt(positionSource)
//                            adapterSource.updateList(listSource)
//                            adapterSource.notifyDataSetChanged()
//                            val adapterTarget: ListAdapter? = target.adapter as ListAdapter?
//                            val customListTarget: MutableList<String> = adapterTarget.getList()
//                            if (positionTarget >= 0) {
//                                customListTarget.add(positionTarget, list)
//                            } else {
//                                customListTarget.add(list)
//                            }
//                            adapterTarget.updateList(customListTarget)
//                            adapterTarget.notifyDataSetChanged()
                    }
                }
            }
        }
//        if (!isDropped && event.localState != null) {
//            (event.localState as View).setVisibility(View.VISIBLE)
//        }
        return true
    }

}