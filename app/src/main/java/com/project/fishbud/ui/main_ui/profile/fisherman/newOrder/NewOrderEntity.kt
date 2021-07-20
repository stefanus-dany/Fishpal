package com.project.fishbud.ui.main_ui.profile.fisherman.newOrder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewOrderEntity(
    var idPesanan : String = "",
    var buyerName : String = "",
    var buyerId : String = "",
    var alamat : String = "",
    var kartuKredit : String = "",
    var jenisPengiriman : String = "",
    var totalHarga : Long = 0,
    var date : String = "",
    var timeDate : String = "",
    var dataIkan : String = "",
) : Parcelable