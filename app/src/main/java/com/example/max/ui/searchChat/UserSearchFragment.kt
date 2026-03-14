package com.example.max.ui.searchChat

import android.os.Bundle
import android.view.View
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
class UserSearchFragment: Fragment(R.layout.user_search_fragment) {
    private var _binding: UserSearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserSearchViewModel by viewModels()
    private lateinit var searchAdapter: UserSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = UserSearchFragmentBinding.bind(view)

        setupRecyclerView()
        setupObservers()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun setupRecyclerView(){
        searchAdapter = UserSearchAdapter { selectedUser ->
            viewModel.createChat(selectedUser)

            val bindle = bundleOf("chatId" to selectedUser.id)
            findNavController().navigate(
                R.id.action_userSearchFragment_to_chatFragment,
                bindle
            )
        }
        binding.rvUserList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.users.collect { userList ->
                    searchAdapter.submitList(userList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}