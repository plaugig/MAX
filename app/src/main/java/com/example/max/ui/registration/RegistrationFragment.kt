package com.example.max.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.max.R
import com.example.max.databinding.RegistrationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        binding.singIn.isEnabled = !state.isSingUpButtonLoading
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is RegistrationEvent.EmptyName -> {
                                binding.inputNameLayout.error = getString(R.string.error)
                            }

                            is RegistrationEvent.Error -> {
                                showError(event.message)
                            }

                            is RegistrationEvent.CompleteRegistration -> {
                                findNavController().navigate(R.id.chatsListFragment)
                            }
                        }
                    }
                }
            }
        }

        binding.singIn.setOnClickListener {
            viewModel.signIn(
                name = binding.etName.text.toString()
            )
        }
    }

    private fun showError(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}