package com.example.max.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.max.R
import com.example.max.databinding.ChatFragmentBinding
import com.example.max.ui.chat.bottom.sheet.AttachmentBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private val chatAdapter = ChatAdapter()

    private var selectedImageUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { list ->
                    chatAdapter.submitList(list)

                    if (list.isNotEmpty()) {
                        binding.massageRV.scrollToPosition(list.size - 1)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chatUser.collect { user ->
                user?.let {
                    binding.name.text = it.name

                    binding.avatar.load(it.avatarUrl) {
                        crossfade(true)
                        placeholder(R.drawable.lox)
                        error(R.drawable.lox)
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

        binding.exit.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.addFile.setOnClickListener {
            val bottomSheet = AttachmentBottomSheet(
                onGalleryClick = {
                    pickImageLauncher.launch("image/*")
                }
            )
            bottomSheet.show(childFragmentManager, "AttachmentBottomSheet")
        }

        binding.btnRemoveImage.setOnClickListener {
            selectedImageUri = null
            binding.previewContainer.visibility = View.GONE
        }

        binding.sendMassage.setOnClickListener {
            val text = binding.enteringMessages.text.toString()

            if (text.isNotBlank() || selectedImageUri != null) {
                if (selectedImageUri != null) {
                    viewModel.sendImageMessage(selectedImageUri!!)
                } else {
                    viewModel.sendMessage(text)
                }

                binding.enteringMessages.text?.clear()
                binding.previewContainer.visibility = View.GONE
                selectedImageUri = null
            }
        }

        binding.avatar.setOnClickListener {
            val userId = viewModel.getPeerUserId()
            findNavController().navigate(
                R.id.profileFragment,
                bundleOf("userId" to userId)
            )
        }
    }

    private fun setupRecyclerView() {
        binding.massageRV.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it.toString()
            binding.previewContainer.visibility = View.VISIBLE
            binding.imagePreview.load(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}