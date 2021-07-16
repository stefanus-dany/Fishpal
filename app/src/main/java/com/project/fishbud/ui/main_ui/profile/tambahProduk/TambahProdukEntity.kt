package com.project.fishbud.ui.main_ui.profile.tambahProduk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TambahProdukEntity(
    var idProduk : String = "",
    var namaiKan : String = "",
    var harga : Int = 0,
    var tokoIkan : String = "",
    var linkImage : String = ""
) : Parcelable