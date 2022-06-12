package com.capstone.capstone.login.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.capstone.MainActivity
import com.capstone.capstone.databinding.ActivityLoginBinding
import com.capstone.capstone.login.model.LoginResponse
import com.capstone.capstone.networking.ApiConfig
import com.capstone.capstone.preferences.model.userpreferences.UserPreferences
import com.capstone.capstone.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)

        binding.buttonLogin.setOnClickListener {
            val email: String = binding.emailLogin.text.toString()
            val password: String = binding.passwordLogin.text.toString()


            showLoading(true)
            Login(email, password)
        }
        binding.hereLogin.setOnClickListener {
            moveIntentRegister()
        }
        binding.buttonLogin.setOnClickListener {
            moveIntentMain()
        }
    }

    private fun moveIntentRegister() {
        Intent(this@LoginActivity, RegisterActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun Login(email: String, password: String) {
        ApiConfig.getApiService().login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    showLoading(false)
                    when (response.code()) {
                        200 -> {
                            val name: String = response.body()?.loginResult!!.name
                            val token: String = response.body()?.loginResult!!.token
                            saveSession(email, name, token)
                            moveIntentMain()
                        }
                        400 -> {
                            showError("Password Salah!")
                        }
                        404 -> {
                            showError("User tidak ditemukan")
                        }
                        else -> {
                            showError("Silahkan check koneksi internet anda")
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showError("Terdapat masalah pada server")
                }

            })

    }

    private fun saveSession(email: String, name: String, token: String) {
        preferences.put(UserPreferences.EMAIL_KEY, email)
        preferences.put(UserPreferences.TOKEN, token)
        preferences.put(UserPreferences.IS_LOGIN, true)
    }

    override fun onStart() {
        super.onStart()
        if (preferences.getBoolean(UserPreferences.IS_LOGIN)) {
            moveIntentMain()
            finish()
        }


    }

    private fun showError(text: String) {
        binding.tvError.text = text
        binding.tvError.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun moveIntentMain() {
        Intent(this@LoginActivity, MainActivity::class.java).also {
            startActivity(it)
        }

    }

}