package com.calielian.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.calielian.task.R
import com.calielian.task.databinding.FragmentRegisterBinding
import com.calielian.task.util.initToolbar
import com.calielian.task.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        auth = Firebase.auth

        initListener()
    }

    private fun checkAuth() {
        try {
            val currentUser = auth.currentUser

            if (currentUser != null) {
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_authentication)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListener(){
        binding.registrar.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.email.text.toString().trim()
        val senha = binding.senha.text.toString().trim()
        if(email.isNotBlank()){
            if(senha.isNotBlank()){
                binding.progressbar.isVisible = true
                Toast.makeText(requireContext(), "Tudo OK!", Toast.LENGTH_SHORT).show()
                registerUser(email, senha)
            }else{
                showBottomSheet(message = getString(R.string.password_empty_register_fragment))
            }
        }else{
            showBottomSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }

    private fun registerUser(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.action_global_homeFragment)
                    } else {
                        binding.progressbar.isVisible = false
                        Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}