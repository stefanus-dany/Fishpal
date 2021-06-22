package com.project.fishbud.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var email : String,
    var name : String,
    var id : String,
) : Parcelable