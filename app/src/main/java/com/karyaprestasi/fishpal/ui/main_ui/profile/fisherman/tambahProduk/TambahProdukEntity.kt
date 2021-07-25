package com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.tambahProduk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TambahProdukEntity(
    var idProduk : String = "",
    var namaIkan : String = "",
    var harga : Int = 0,
    var tokoIkan : String = "",
    var linkImage : String = "",
    var userId : String = ""
) : Parcelable