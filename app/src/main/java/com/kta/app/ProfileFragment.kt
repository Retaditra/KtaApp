package com.kta.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kta.app.databinding.FragmentProfileBinding
import com.kta.app.login.LoginActivity
import com.kta.app.utils.EncryptedSharedPreferences

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferencesHelper: EncryptedSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferencesHelper = EncryptedSharedPreferences(requireContext())
        val sharedPref = sharedPreferencesHelper.getSharedPreferences()
        val token = sharedPref.getString("token", null)
        val id = sharedPref.getString("id", null)
        val email = sharedPref.getString("email", null)
        val name = sharedPref.getString("name", null)

        binding.userName.text = name
        binding.userID.text = id
        binding.userEmail.text = email

        if (token != null) {
            binding.logoutButton.setOnClickListener {
                with(sharedPref.edit()) {
                    remove("token")
                    remove("id")
                    remove("name")
                    remove("email")
                    apply()
                }

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}

