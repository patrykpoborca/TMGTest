package com.patryk.tmgtest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import autodispose2.AutoDispose
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.patryk.tmgtest.databinding.FragmentLeaderboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderboardsFragment : Fragment() {

    private lateinit var leaderboardViewModel: LeaderboardViewModel
    private var _binding: FragmentLeaderboardBinding? = null
    private val adapter = PlayerRecyclerAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        leaderboardViewModel =
            ViewModelProvider(this)[LeaderboardViewModel::class.java]

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        binding.playerRecycler.adapter = adapter
        binding.playerRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.filterTypeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, FilterType.values().map {
            requireContext().getString(it.title)
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupListeners()
        leaderboardViewModel.eventStream
            .onNext(ScreenLoad)
        binding.filterTypeSpinner
            .onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                leaderboardViewModel.eventStream
                    .onNext(OnFilterChange(FilterType.values()[position]))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupListeners() {

        leaderboardViewModel.observeScreenState()
            .retry()
            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe({
                       adapter.updatePlayers(it.filteredPlayers)
            }, {})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}