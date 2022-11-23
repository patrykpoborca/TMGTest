package com.patryk.tmgtest.ui.reusable

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patryk.tmgtest.R
import com.patryk.tmgtest.databinding.VhScoreBinding
import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.utility.deriveTMGScore
import com.patryk.tmgtest.utility.translateXByOrReset

class TMGScoresRecyclerAdapter : RecyclerView.Adapter<TMGScoreItemViewHolder>() {

    private val scores: MutableList<TMGGameScore> = mutableListOf()
    internal var vhInEditMode: TMGScoreItemViewHolder? = null
    // exposed for external listener (vm)
    var updateListener: ((gameScore: TMGGameScore) -> Unit)? = null

    override fun onBindViewHolder(holder: TMGScoreItemViewHolder, position: Int) {
        holder.bind(scores[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TMGScoreItemViewHolder {
        return TMGScoreItemViewHolder(
            VhScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            this
        )
    }

    fun onSaveUpdate(update: TMGGameScore?) {
        vhInEditMode?.onSaveUpdate(update)
        vhInEditMode = null
    }

    override fun getItemCount(): Int = scores.size
    fun updateData(data: List<TMGGameScore>, scoresRecycler: RecyclerView) {
        // no update
        if (scores == data || data.isEmpty())
            return
        when (scores.size) {
            data.size -> {
                data.filterIndexed { index, item -> scores[index] != item }.firstOrNull()
                    ?.let { diff ->
                        scores[diff.index] = diff
                        notifyItemChanged(diff.index)
                        scoresRecycler
                            .scrollToPosition(diff.index)
                    }
            }
            0 -> {
                scores.clear()
                scores.addAll(data)
                //blowup whole data as it's destructive operation
                notifyDataSetChanged()
            }
            else -> {
                // lazy way of a diffutil as we have no delete operation
                scores.add(data.last())
                notifyItemInserted(scores.size)
                scoresRecycler.scrollToPosition(scores.size - 1)
            }
        }

    }


}

// Note: for the sake of brevity for the test I chose not to implement a VM layer here. It would make sense to have it here if this screen would
// gain additional complexity in valiation etc. But considering the scope of the assignment and size of app already i opted to skip it here.
class TMGScoreItemViewHolder(private val binding: VhScoreBinding, private val adapter: TMGScoresRecyclerAdapter) :
    RecyclerView.ViewHolder(binding.root) {
    var currentScore: TMGGameScore? = null
    var currentPosition = -1

    fun bind(tmgGameScore: TMGGameScore, position: Int) {
        currentPosition = position
        currentScore = tmgGameScore
        tmgGameScore.losingScore.run {
            binding.loserScore.setText(score.toString())
            binding.loserName.setText(name)
        }
        tmgGameScore.winningScore.run {
            binding.winnerScore.setText(score.toString())
            binding.winnerName.setText(name)
        }
        binding.apply {
            binding.root
                .setOnLongClickListener(View.OnLongClickListener {
                    adapter.vhInEditMode?.animateInIcon(false)
                    adapter.vhInEditMode = this@TMGScoreItemViewHolder
                    animateInIcon(true)
                    true
                })
        }
        binding.scorePosition.text = "#${tmgGameScore.index}"
        binding.saveAction
            .setOnClickListener {
                onSaveAction()
            }
    }

    private fun onSaveAction() {
        animateInIcon(false)
        attemptSave()
    }

    // poor man's budget animation
    fun animateInIcon(showIcon: Boolean) {

        val animMargin = binding.root.resources.getDimension(R.dimen.anim_margin)
        binding.loserScore.animate()
            .translateXByOrReset(-animMargin, !showIcon)
            .setDuration(200)
            .start()
        binding.loserName.animate()
            .translateXByOrReset(-animMargin, !showIcon)
            .setDuration(200)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator){}

                override fun onAnimationEnd(animation: Animator){
                    binding.saveAction.visibility = if(showIcon) View.VISIBLE else View.GONE
                    changeEnabledStateForTexts(showIcon)
                }

                override fun onAnimationCancel(animation: Animator){}

                override fun onAnimationRepeat(animation: Animator){}
            })
            .start()

    }

    private fun attemptSave() {
        // pushing validation to enclosing VM
        adapter.updateListener?.invoke(
            deriveTMGScore(
                binding.winnerName.text.toString(),
                binding.winnerScore.text.toString().toIntOrNull(),
                binding.loserName.text.toString(),
                binding.loserScore.text.toString().toIntOrNull(),
                currentScore!!.index
            )
        )
    }

    fun onSaveUpdate(update: TMGGameScore?) {
        if (update == null && currentScore != null) {
            //VM decided invalid, revert
            bind(currentScore!!, currentPosition)
        }
        if (update != null) {
            bind(update, currentPosition)
        }
    }

    private fun changeEnabledStateForTexts(isEnabled: Boolean) {
        binding.loserScore.isEnabled = isEnabled
        binding.loserName.isEnabled = isEnabled
        binding.winnerName.isEnabled = isEnabled
        binding.winnerScore.isEnabled = isEnabled

    }

}