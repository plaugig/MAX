package com.example.max.ui.searchChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.max.R
import com.example.max.databinding.UserSearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserSearchFragment: Fragment() {

    private var _binding: UserSearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserSearchViewModel by viewModels()
    private lateinit var searchAdapter: UserSearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserSearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchAdapter = UserSearchAdapter(
            listener = viewModel
        )

        binding.rvUserList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is SearchUiEvent.OpenChat -> {
                                findNavController().navigate(
                                    R.id.action_userSearchFragment_to_chatFragment,
                                    bundleOf("chatId" to event.userId)
                                )
                            }
                        }
                    }
                }

                launch {
                    viewModel.users.collect { userList ->
                        searchAdapter.submitList(userList)
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}