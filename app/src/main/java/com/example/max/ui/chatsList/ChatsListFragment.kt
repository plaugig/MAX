package com.example.max.ui.chatsList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.max.R
import com.example.max.databinding.ChatsListFragmentBinding
import com.example.max.ui.chatsList.item.ItemChatListData

class ChatsListFragment : Fragment(R.layout.chats_list_fragment) {

    private var _binding: ChatsListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatsListAdapter: ChatsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChatsListFragmentBinding.bind(view)

        setupRecyclerView()

        val testData = listOf(
            ItemChatListData("1", "Lox Ebany", "Верни деньги заебал"),
            ItemChatListData("2", "Mentor", "Хули так долга тварь ебаная ?!"),
            ItemChatListData("3", "Myniga", "а я сегодня буду кушать?")
        )
        chatsListAdapter.submitList(testData)
    }

    private fun setupRecyclerView() {
        chatsListAdapter = ChatsListAdapter { chat ->
            findNavController().navigate(R.id.action_chats)
        }
        binding.listOfChat.apply {
            adapter = chatsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}