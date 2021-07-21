package com.project.fishbud.ui.main_ui.community

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityEntity(
    var idProduk : String = "",
    var namaIkan : String = "",
    var harga : Long = 0,
    var tokoIkan : String = "",
    var linkImage : String = "",
    var userId : String = ""
) : Parcelable