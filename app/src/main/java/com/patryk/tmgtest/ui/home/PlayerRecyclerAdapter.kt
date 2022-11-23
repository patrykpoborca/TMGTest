package com.patryk.tmgtest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patryk.tmgtest.databinding.VhPlayerBinding

class PlayerRecyclerAdapter : RecyclerView.Adapter<PlayerVH>() {
    private val list: MutableList<SyntheticPlayer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerVH {
        return PlayerVH(VhPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlayerVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updatePlayers(filteredPlayers: List<SyntheticPlayer>) {
        if (filteredPlayers.isEmpty())
            return
        list.clear()
        list.addAll(filteredPlayers)
        notifyDataSetChanged()
    }

}

class PlayerVH(val binding: VhPlayerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(syntheticPlayer: SyntheticPlayer) {
        binding.gamesPlayed.text = syntheticPlayer.gamesPlayed.toString()
        binding.gamesWon.text = syntheticPlayer.gamesWon.toString()
        binding.gamesRatio.text = syntheticPlayer.ratioString
        binding.playerName.text = syntheticPlayer.name

    }
}