package com.project.fishbud.ui.main_ui.profile.buyer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderFishermanEntity(
    var nelayanId : String = "",
    var idPesanan : String = "",
    var namaIkan : String= "",
    var harga : Long = 0,
    var linkImage : String = "",
    var tokoIkan : String = ""
) : Parcelable