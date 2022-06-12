package com.capstone.capstone.login.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    val id: String,
    val name: String,
    val token: String
):Parcelable
