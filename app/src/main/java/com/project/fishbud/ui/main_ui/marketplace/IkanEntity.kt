package com.project.fishbud.ui.main_ui.marketplace

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IkanEntity(
    var namaiKan : String,
    var harga : Int,
    var tokoIkan : String,
) : Parcelable