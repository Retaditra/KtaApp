package com.kta.app.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kta.app.R
import com.kta.app.data.database.DataRepository
import com.kta.app.databinding.ActivityLoginBinding
import com.kta.app.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository: DataRepository = DataRepository.getInstance(applicationContext)
        lifecycleScope.launch {
            repository.deleteAll()
        }

        backPressed()
        setupEmailValidate()
        setupPasswordValidate()
        setupView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {
        binding.loginLayout.setOnTouchListener { _, _ ->
            clearFocusAndHideKeyboard()
            true
        }

        binding.loginFrame.setOnTouchListener { _, _ ->
            clearFocusAndHideKeyboard()
            true
        }

        binding.loginButton.setOnClickListener {
            val phone = binding.loginPhone.text.toString()
            val password = binding.loginPassword.text.toString()

            if (isInputValid(phone, password)) {
                login(phone, password)
            }
        }

        binding.resetPassword.setOnClickListener {
            val phoneNumber = "+62"
            val message = getString(R.string.messageReset)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(
                "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                    Uri.encode(message)
                }"
            )
            startActivity(intent)
        }
    }

    private fun backPressed() {
        var isBackPressedOnce = false
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPressedOnce) {
                    finish()
                } else {
                    isBackPressedOnce = true
                    Toast.makeText(
                        this@LoginActivity, getString(R.string.closeWarning),
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        isBackPressedOnce = false
                    }, 2000)
                }
            }
        })
    }

    private fun login(phone: String, password: String) {
        progressBar(true)
        viewModel.login(
            phone, password,
            onSuccess = {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                progressBar(false)
            },
            message = {
                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                progressBar(false)
            },
        )
    }

    private fun isInputValid(phone: String, password: String): Boolean {
        var isValid = true

        if (phone.isEmpty()) {
            binding.phoneLayout.error = getString(R.string.emptyPhone)
            isValid = false
        }
        if (password.isEmpty() || password.length < 3) {
            binding.passwordLayout.error = getString(R.string.invalid_password)
            isValid = false
        }
        return isValid
    }

    private fun setupEmailValidate() {
        binding.loginPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.phoneLayout.error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
    }

    private fun setupPasswordValidate() {
        binding.loginPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.passwordLayout.error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
    }

    private fun clearFocusAndHideKeyboard() {
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            currentFocusView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
        }
    }

    private fun progressBar(visible: Boolean) {
        binding.progressBarLogin.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
