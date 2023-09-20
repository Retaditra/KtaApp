package com.kta.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.kta.app.databinding.DialogSessionExpiredBinding
import com.kta.app.login.LoginActivity
import com.kta.app.utils.EncryptedSharedPreferences

class SessionExpiredDialog(private val context: Context) {

    private val sharedPreferencesHelper: EncryptedSharedPreferences = EncryptedSharedPreferences(context)

    private fun showDialog() {
        val binding = DialogSessionExpiredBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()
        binding.textMessage.text = "Sesi login Anda telah berakhir. Silakan login kembali."

        binding.btnLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            dialog.dismiss()

            with(sharedPreferencesHelper.getSharedPreferences().edit()) {
                remove("token")
                remove("name")
                remove("phone")
                remove("id")
                apply()
            }
        }

        dialog.show()
    }

    companion object {
        fun show(context: Context) {
            SessionExpiredDialog(context).showDialog()
        }
    }
}

