package com.bandirma.entity

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.annotation.IntegerRes

data class User(
    val id : String,
    val name : String,
    val surname : String,
    val password : String,
    val email : String
)
