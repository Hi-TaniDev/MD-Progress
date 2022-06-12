package com.capstone.capstone.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.capstone.databinding.ActivityRegisterBinding
import com.capstone.capstone.login.activity.LoginActivity
import com.capstone.capstone.networking.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)

        binding.buttonRegister.setOnClickListener {
            val name: String = binding.nameRegister.text.toString()
            val email: String = binding.emailRegister.text.toString()
            val password: String = binding.password.text.toString()
            val confirPass: String = binding.passwordConfir.text.toString()

            showLoading(true)
            Register(name, email, password, confirPass)
        }
        binding.here.setOnClickListener {
            moveIntentLogin()
        }
    }

    private fun Register(name: String, email: String, password: String, confirPass: String) {
        ApiConfig.getApiService().register(
            name, email, password, confirPass
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                when (response.code()) {
                    200 -> {
                        moveIntentLogin()
                    }
                    403 -> {
                        showError("Pastikan password yang dimasukkan sama")
                    }
                    else -> {
                        showError("Error")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showError("Error")
            }

        })
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

    private fun moveIntentLogin() {
        Intent(this@RegisterActivity, LoginActivity::class.java).also {
            startActivity(it)
        }

    }
}