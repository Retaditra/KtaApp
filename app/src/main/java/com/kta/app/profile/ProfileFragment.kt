package com.kta.app.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kta.app.SessionExpiredDialog
import com.kta.app.databinding.FragmentProfileBinding
import com.kta.app.login.LoginActivity
import com.kta.app.utils.EncryptedSharedPreferences

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferencesHelper: EncryptedSharedPreferences
    private val viewModel: LogoutViewModel by viewModels()

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
        val name = sharedPref.getString("name", null)
        val phone = sharedPref.getString("phone", null)
        val id = sharedPref.getString("id", null)

        binding.userName.text = name
        binding.userPhone.text = phone
        binding.userID.text = id

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val token = sharedPreferencesHelper.getSharedPreferences().getString("token", null)
        progressBar(true)

        viewModel.logout(token.toString(),
            onSuccess = { success ->
                Toast.makeText(requireContext(), success, Toast.LENGTH_SHORT).show()
                progressBar(false)

                with(sharedPreferencesHelper.getSharedPreferences().edit()) {
                    remove("token")
                    remove("name")
                    remove("phone")
                    remove("id")
                    apply()
                }

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            },
            onFailure = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                progressBar(false)
                if (errorMessage == "unauthenticated") {
                    SessionExpiredDialog.show(requireContext())
                }
            })
    }

    private fun progressBar(visible: Boolean) {
        binding.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

