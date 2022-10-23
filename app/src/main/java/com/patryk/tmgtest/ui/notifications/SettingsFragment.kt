package com.patryk.tmgtest.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.patryk.tmgtest.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.deleteButton
            .setOnClickListener {
                settingsViewModel.onDeleteCache()
            }
        settingsViewModel.listenToVMEffects(requireContext(), this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}