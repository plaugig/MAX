package com.example.max.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.max.R
import com.example.max.databinding.ChatFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatFragment: Fragment(R.layout.chat_fragment) {

    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private val chatAdapter = ChatAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChatFragmentBinding.bind(view)

        val chatId = arguments?.getString("chatId") ?: ""
        viewModel.setupChat(chatId)

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.message.collect { list ->
                    chatAdapter.submitList(list)

                    if(list.isNotEmpty()){
                        binding.massageRV.scrollToPosition(list.size - 1)
                    }
                }
            }
        }
        binding.sendMassage.setOnClickListener {
            val text = binding.enteringMessages.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendMessage(text)
                binding.enteringMessages.text?.clear()
            }
        }
        binding.exit.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun setupRecyclerView(){
        binding.massageRV.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}