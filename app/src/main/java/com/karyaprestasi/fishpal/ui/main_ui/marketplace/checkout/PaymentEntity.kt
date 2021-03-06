package com.karyaprestasi.fishpal.ui.main_ui.marketplace.checkout

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
    var netPrice : Long = 0,
    var totalHarga : Long = 0,
    var timeDate : String = "",
    var date : String = "",
) : Parcelable