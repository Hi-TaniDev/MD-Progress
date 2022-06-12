package com.capstone.capstone.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(
    val error: Boolean,
    val message: String
):Parcelable
