package com.patryk.tmgtest.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.patryk.tmgtest.utility.deriveTMGScore
import androidx.recyclerview.widget.RecyclerView
import autodispose2.AutoDispose.autoDisposable
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.patryk.tmgtest.R
import com.patryk.tmgtest.databinding.FragmentScoresBinding
import com.patryk.tmgtest.ui.reusable.TMGScoresRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoresFragment : Fragment() {

    private lateinit var scoresViewModel: ScoresViewModel
    private var _binding: FragmentScoresBinding? = null
    private val adapter = TMGScoresRecyclerAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scoresViewModel =
            ViewModelProvider(this).get(ScoresViewModel::class.java)

        _binding = FragmentScoresBinding.inflate(inflater, container, false)
        binding.scoresRecycler.adapter = adapter
        binding.scoresRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupListeners()
        scoresViewModel.eventStream.onNext(onScreenLoad)
    }

    private fun setupListeners() {
        scoresViewModel.listenToVMEffects(requireContext(), this) {
            if (it is ScoreUpdateEffect)
                adapter.onSaveUpdate(it.score)
        }
        adapter.updateListener = { item ->
            scoresViewModel.eventStream.onNext(onUpdateScoreItem(item))
        }
        binding.clickAbsorber.setOnClickListener { scoresViewModel.eventStream.onNext(onClickAbsorbed) }

        setupFabLogic()

        scoresViewModel
            .observeScreenState()
            .retry()
            .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe({
                if (it.isMakingScore) {
                    binding.clickAbsorber.visibility = View.VISIBLE
                    binding.fab.setImageResource(R.drawable.ic_baseline_check_24)
                } else {
                    binding.clickAbsorber.visibility = View.GONE
                    binding.fab.setImageResource(R.drawable.ic_baseline_add_24)
                }
                adapter.updateData(it.tmgGameScores)
            },
                {
                    Log.e("Error", "Subscription failure", it)
                })
    }

    private fun setupFabLogic() {
        binding.fab
            .setOnClickListener {
                val score = binding.let {
                    deriveTMGScore(
                        it.p1Name.text.toString(),
                        it.p1Score.text.toString().toIntOrNull(),
                        it.p2Name.text.toString(),
                        it.p2Score.text.toString().toIntOrNull(),
                        -1
                    )
                }
                scoresViewModel.eventStream.onNext(FabClicked(score))
            }


        binding.scoresRecycler
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        binding.fab
                            .animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .start()
                    } else {
                        binding.fab
                            .animate()
                            .scaleX(0f)
                            .scaleY(0f)
                            .setDuration(150)
                            .start()
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}