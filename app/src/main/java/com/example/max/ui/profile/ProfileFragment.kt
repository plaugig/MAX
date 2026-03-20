package com.example.max.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.max.R
import com.example.max.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.profile?.let {
                        binding.name.text = it.name
                    }

                    binding.actionButton.visibility = if (state.isCurrentProfile) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.actionButton.setOnClickListener {
            viewModel.clearAllMessages()
            findNavController().navigate(R.id.registrationFragment)
        }
    }
}