package com.example.max.ui.chat.bottom.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.max.R
import com.example.max.databinding.BottomSheetChatAttachmentsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AttachmentBottomSheet(
    private val onGalleryClick: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChatAttachmentsBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetChatAttachmentsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.gallery?.setOnClickListener {
            onGalleryClick()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}