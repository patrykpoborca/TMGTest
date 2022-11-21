package com.patryk.tmgtest.ui.reusable

import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patryk.tmgtest.databinding.VhScoreBinding
import com.patryk.tmgtest.models.TMGGameScore

class TMGScoresRecyclerAdapter: RecyclerView.Adapter<TMGScoreItemViewHolder>() {
    private val scores: List<TMGGameScore> = mutableListOf()

    override fun onBindViewHolder(holder: TMGScoreItemViewHolder, position: Int) {
        holder.bind(scores[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TMGScoreItemViewHolder {
        return TMGScoreItemViewHolder(
            VhScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = scores.size
}

class TMGScoreItemViewHolder(private val binding: VhScoreBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(tmgGameScore: TMGGameScore) {
        tmgGameScore.losingScore.run {
            binding.loserScore.setText(score.toString())
            binding.loserName.setText(name)
        }
        tmgGameScore.winningScore.run {
            binding.winnerScore.setText(score.toString())
            binding.winnerName.setText(name)
        }
    }

    fun addEditableLogic(editText: EditText, isNumber: Boolean) {
        editText.setOnLongClickListener {
            editText.inputType = if(isNumber) InputType.TYPE_CLASS_NUMBER else (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
            true
        }
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
            ) {
                editText.inputType = InputType.TYPE_NULL
                true
            }
            false
        })
    }

}