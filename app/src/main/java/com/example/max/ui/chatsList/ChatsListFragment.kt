package com.example.max.ui.chatsList

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
import com.example.max.databinding.ChatsListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatsListFragment : Fragment() {

    private var _binding: ChatsListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatListViewModel by viewModels()

    private lateinit var chatsListAdapter: ChatsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatsListAdapter = ChatsListAdapter(
            listener = viewModel
        )
        binding.listOfChat.apply {
            adapter = chatsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is ChatListUiEvent.NavigateToRegistration -> {
                                findNavController().navigate(
                                    R.id.action_chatsListFragment_to_registrationFragment
                                )
                            }

                            is ChatListUiEvent.OpenChat -> {
                                findNavController().navigate(
                                    R.id.action_chats,
                                    bundleOf("chatId" to event.chatId)
                                )
                            }
                        }
                    }
                }

                launch {
                    viewModel.chats.collect { chatsList ->
                        chatsListAdapter.submitList(chatsList)
                    }
                }
            }
        }

        setupListeners()
    }

    private fun setupListeners(){
        binding.newChatButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_chatsListFragment_to_userSearchFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}