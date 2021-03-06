package com.karyaprestasi.fishpal.ui.main_ui.profile.buyer.waiting_payment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.karyaprestasi.fishpal.ui.main_ui.marketplace.checkout.PaymentEntity
import com.karyaprestasi.fishpal.ui.main_ui.profile.OrderFishermanEntity
import com.karyaprestasi.fishpal.utils.DataFirebase


class WaitingPaymentViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    fun getDataPayment(): LiveData<MutableList<PaymentEntity>> {
        val mutableData = MutableLiveData<MutableList<PaymentEntity>>()
        DataFirebase.getDataPayment().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun getItemOrdered(idPembayaran: String): LiveData<MutableList<OrderFishermanEntity>> {
        val mutableData = MutableLiveData<MutableList<OrderFishermanEntity>>()
        DataFirebase.getItemOrdered(idPembayaran).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun storeToDatabase(
        idPembayaran: String,
        buyerName: String,
        buyerId: String,
        alamat: String,
        kartuKredit: String,
        jenisPengiriman: String,
        totalHarga: Long,
        netPrice: Long,
        date: String,
        payBefore: String,
        dataIkan: String,
        orderData: MutableList<OrderFishermanEntity>
    ) {
        Log.i("cekBuyer", "buyerId di waiting: $buyerId")
        //store item order to fisherman
        for (i in 0 until (orderData.size)) {
            val data = orderData[i]
            val nelayanId = data.nelayanId
            val idProduk = data.idPesanan
            val namaIkan = data.namaIkan
            val linkImage = data.linkImage
            val tokoIkan = data.tokoIkan
            val buyerId = data.idBuyer
            val hargaPerItem = data.harga

            val reference2 =
                FirebaseDatabase.getInstance().reference.child("Users").child(nelayanId)
                    .child("itemOrdered")
                    .child(idPembayaran)
                    .child(idProduk)

            val value = OrderFishermanEntity(
                nelayanId,
                idProduk,
                namaIkan,
                hargaPerItem,
                totalHarga,
                linkImage,
                tokoIkan,
                idPembayaran,
                buyerId
            )

            reference2.setValue(value).addOnCompleteListener {
                if (it.isSuccessful) {
                    //tambah idPesanan
//                    val reference0 =
//                        FirebaseDatabase.getInstance().reference.child("Users").child(nelayanId)
//                            .child("itemOrdered")
//                            .child(idPembayaran)
//                            .child("idPesanan")
//                    reference0.setValue(idPembayaran)

                    //hapus dari waitingWayment
                    val reference =
                        FirebaseDatabase.getInstance().reference.child("Users").child(buyerId)
                            .child("waitingPayment")
                            .child(idPembayaran)
                    reference.removeValue().addOnCompleteListener { er ->
                        if (er.isSuccessful) {
                            Toast.makeText(mContext, "Pesanan telah dibayar!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
//                    Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}