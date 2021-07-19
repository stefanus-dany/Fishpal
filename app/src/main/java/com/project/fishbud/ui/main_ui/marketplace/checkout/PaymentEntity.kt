package com.project.fishbud.ui.main_ui.marketplace.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentEntity(
    var idPembayaran : String = "",
    var buyerName : String = "",
    var buyerId : String = "",
    var alamat : String = "",
    var kartuKredit : String = "",
    var jenisPengiriman : String = "",
    var totalHarga : Long = 0,
    var timeDate : String = ""
) : Parcelable