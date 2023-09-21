package com.kta.app.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kta.app.R
import com.kta.app.data.database.DataRepository
import com.kta.app.databinding.FragmentProfileBinding
import com.kta.app.login.LoginActivity
import com.kta.app.utils.EncryptPreferences
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var preference: EncryptPreferences
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
        preference = EncryptPreferences(requireContext())

        getProfile()
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun getProfile() {
        val profile = preference.getPreferences()
        val name = profile.getString("name", null)
        val phone = profile.getString("phone", null)
        val id = profile.getString("id", null)

        binding.apply {
            userName.text = name
            userPhone.text = phone
            userID.text = id
        }
    }

    private fun logout() {
        val token = preference.getPreferences().getString("token", null)

        progressBar(true)
        viewModel.logout(token.toString(),
            onSuccess = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                logoutProses()
                progressBar(false)
            },
            onFailure = {
                logoutProses()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.messageLogout),
                    Toast.LENGTH_SHORT
                ).show()
                progressBar(false)
            })
    }

    private fun logoutProses() {
        preference.removePreferences()

        val repository: DataRepository = DataRepository.getInstance(requireContext())
        lifecycleScope.launch {
            repository.deleteAll()
        }

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun progressBar(visible: Boolean) {
        binding.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

