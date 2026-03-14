package com.example.max.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.max.R
import com.example.max.databinding.RegistrationFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class RegistrationFragment : Fragment(R.layout.registration_fragment) {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RegistrationFragmentBinding.bind(view)

        binding.btnStart.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            if (name.isEmpty()) {
                binding.inputNameLayout.error = getString(R.string.error)
                return@setOnClickListener
            }
            binding.btnStart.isEnabled = false

            val auth = Firebase.auth
            auth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful){

                    val user = auth.currentUser

                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }

                    user?.updateProfile(
                        profileUpdates
                    )?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful){
                            findNavController().navigate(R.id.chatsListFragment)
                        }
                    }
                } else {
                   binding.btnStart.isEnabled = true
                    Toast.makeText(
                        context,
                        "Ошибка: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}