package com.karyaprestasi.fishpal.ui.recognition

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoScanEntity(
    var MaxAge : String = "",
    var Size : String = "",
    var Nutrition : String = ""
) : Parcelable