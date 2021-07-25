package com.karyaprestasi.fishpal.ui.main_ui.profile.buyer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataProfileEntity(
    var name : String = "",
    var city : String = "",
    var birthday : String = "",
    var urlProfileImage : String = "",
    var address : String = ""
) : Parcelable