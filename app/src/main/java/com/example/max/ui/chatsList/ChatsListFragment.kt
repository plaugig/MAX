package com.example.max.ui.chatsList

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
import com.example.max.data.max.UserData
import com.example.max.databinding.ChatsListFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatsListFragment : Fragment(R.layout.chats_list_fragment) {

    private var _binding: ChatsListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatListViewModel by viewModels()

    private lateinit var chatsListAdapter: ChatsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChatsListFragmentBinding.bind(view)

        val auth = Firebase.auth
        if (auth.currentUser == null){
            findNavController().navigate(
                R.id.action_chatsListFragment_to_registrationFragment
            )
        } else {
            setupRecyclerView()
            setupChatListData()
        }


    }

    private fun setupRecyclerView() {
        chatsListAdapter = ChatsListAdapter { chat ->
            val bundle = bundleOf("chatId" to chat.id)
            findNavController().navigate(R.id.action_chats, bundle)
        }
        binding.listOfChat.apply {
            adapter = chatsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupChatListData(){
        val testUsers = listOf(
            UserData("1", "Lox Ebany", "https://api.dicebear.com/7.x/avataaars/svg?seed=Lox"),
            UserData("2", "Mentor", "https://api.dicebear.com/7.x/avataaars/svg?seed=Mentor"),
            UserData("3", "Myniga", "https://api.dicebear.com/7.x/avataaars/svg?seed=Niga")
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.chats.collect { chatsList ->
                    chatsListAdapter.submitList(chatsList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}