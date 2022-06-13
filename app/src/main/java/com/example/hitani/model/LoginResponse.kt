package com.example.hitani.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
): Parcelable