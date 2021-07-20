package com.project.fishbud.ui.main_ui.profile.fisherman.newOrder

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.project.fishbud.ui.main_ui.marketplace.checkout.PaymentEntity
import com.project.fishbud.ui.main_ui.profile.buyer.waitingPayment.OrderFishermanEntity
import com.project.fishbud.utils.DataFirebase


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
        date: String,
        payBefore: String,
        dataIkan: String,
        orderData: MutableList<OrderFishermanEntity>
    ) {

        //store item order to fisherman
        for (i in 0 until (orderData.size)) {
            val data = orderData[i]
            val nelayanId = data.nelayanId
            val idProduk = data.idPesanan
            val namaIkan = data.namaIkan
            val harga = data.harga
            val linkImage = data.linkImage
            val tokoIkan = data.tokoIkan

            val reference2 =
                FirebaseDatabase.getInstance().reference.child("Users").child(nelayanId)
                    .child("itemOrdered")
                    .child(idPembayaran)
                    .child(idProduk)

            val value = OrderFishermanEntity(
                nelayanId,
                idProduk,
                namaIkan,
                harga,
                linkImage,
                tokoIkan,
            )

            reference2.setValue(value).addOnCompleteListener {
                if (it.isSuccessful) {
                    //tambah idPesanan
                    val reference0 =
                        FirebaseDatabase.getInstance().reference.child("Users").child(nelayanId)
                            .child("itemOrdered")
                            .child(idPembayaran)
                            .child("idPesanan")
                    reference0.setValue(idPembayaran)

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