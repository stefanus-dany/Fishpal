package com.project.fishbud.ui.main_ui.marketplace

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IkanEntity(
    var idProduk : String = "",
    var namaIkan : String = "",
    var harga : Int = 0,
    var tokoIkan : String = "",
    var linkImage : String = ""
) : Parcelable