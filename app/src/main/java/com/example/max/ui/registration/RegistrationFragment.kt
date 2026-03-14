package com.example.max.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.max.R
import com.example.max.data.max.UserData
import com.example.max.databinding.RegistrationFragmentBinding
import com.example.max.domain.MainInteractor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.registration_fragment) {

    @Inject
    lateinit var interactor: MainInteractor


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
                            val userData = UserData(
                                userId = user.uid,
                                name = name,
                                avatarUrl = null
                            )

                            viewLifecycleOwner.lifecycleScope.launch {
                                try {
                                    interactor.saveProfile(userData)

                                    findNavController().navigate(R.id.chatsListFragment)
                                }catch (e: Exception){
                                    binding.btnStart.isEnabled = true
                                    Toast.makeText(
                                        context,
                                        "Ошибка БД: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }


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