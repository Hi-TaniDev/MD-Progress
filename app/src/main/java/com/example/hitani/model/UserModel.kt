package com.example.hitani.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    val email : String,
    val token: String,
    val is_login: Boolean
): Parcelable