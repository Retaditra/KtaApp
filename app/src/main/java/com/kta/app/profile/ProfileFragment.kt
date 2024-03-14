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
import com.bumptech.glide.Glide
import com.kta.app.R
import com.kta.app.data.database.ScheduleRepository
import com.kta.app.databinding.FragmentProfileBinding
import com.kta.app.login.LoginActivity
import com.kta.app.utils.EncryptPreferences
import com.kta.app.utils.UserProfilePreferences
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var preference: EncryptPreferences
    private val viewModel: ProfileViewModel by viewModels()

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

        userProfile()
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun userProfile() {
        UserProfilePreferences.init(requireContext())
        val data = UserProfilePreferences.getUserProfile()

        binding.apply {
            Glide.with(requireContext())
                .load(data.imageProfile)
                .circleCrop()
                .into(binding.userImage)
            userName.text = data.name
            userId.text = data.id
            userRole.text = data.position
            userPhone.text = getString(R.string.user_phone, data.phone)
            userBirth.text = getString(R.string.user_birth, data.birthplace, data.dateBirth)
        }
    }

    private fun logout() {
        val token = preference.getPreferences().getString("token", null)

        viewModel.logout(token.toString(),
            onSuccess = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                logoutProses()
            },
            onFailure = {
                logoutProses()
                Toast.makeText(
                    requireContext(), getString(R.string.messageLogout), Toast.LENGTH_SHORT
                ).show()
            },
            loading = {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
    }

    private fun logoutProses() {
        UserProfilePreferences.init(requireContext())
        UserProfilePreferences.removeUserProfile()
        preference.removePreferences()

        val repository: ScheduleRepository = ScheduleRepository.getInstance(requireContext())
        lifecycleScope.launch {
            repository.deleteAll()
        }

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}