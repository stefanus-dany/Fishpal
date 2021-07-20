package com.project.fishbud.ui.main_ui.profile.buyer.waitingPayment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WaitingPaymentEntity(
    var nelayanId : String = "",
    var idPesanan : String = "",
    var namaIkan : String= "",
    var harga : Long = 0,
    var linkImage : String = "",
    var tokoIkan : String = "",
    var idPembayaran : String = "",
    var buyerName : String = "",
    var buyerId : String = "",
    var alamat : String = "",
    var kartuKredit : String = "",
    var jenisPengiriman : String = "",
    var totalHarga : Long = 0,
    var date : String = "",
    var payBefore : String = "",
    var countQuantity : Long = 0
) : Parcelable