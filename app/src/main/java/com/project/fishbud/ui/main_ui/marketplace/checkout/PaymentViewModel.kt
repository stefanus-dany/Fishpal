package com.project.fishbud.ui.main_ui.marketplace.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.project.fishbud.model.UserModel
import com.project.fishbud.ui.main_ui.marketplace.IkanEntity
import com.project.fishbud.ui.main_ui.profile.buyer.OrderFishermanEntity
import com.project.fishbud.utils.DataFirebase
import java.util.concurrent.Executors


class PaymentViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context

    fun getDataUser(): LiveData<MutableList<UserModel>> {
        val mutableData = MutableLiveData<MutableList<UserModel>>()
        DataFirebase.getDataUser().observeForever {
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
        timeDate: String,
        date: String,
        dataIkan: ArrayList<IkanEntity>?
    ) {
        val reference =
            FirebaseDatabase.getInstance().reference.child("Users").child(buyerId)
                .child("waitingPayment")
                .child(idPembayaran)
        val value = PaymentEntity(
            idPembayaran,
            buyerName,
            buyerId,
            alamat,
            kartuKredit,
            jenisPengiriman,
            totalHarga,
            timeDate,
            date
        )
        reference.setValue(value).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                for (i in 0 until (dataIkan!!.size)){
                    val data = dataIkan[i]
                    //${tmp.userId} ${tmp.idProduk} ${tmp.namaIkan} ${tmp.tokoIkan} ${tmp.linkImage} ${tmp.harga}
                    val idProduk = data.idProduk
                    val nelayanId = data.userId
                    val namaIkan = data.namaIkan
                    val tokoIkan = data.tokoIkan
                    val linkImage = data.linkImage
                    val harga = data.harga

                    val reference2 =
                        FirebaseDatabase.getInstance().reference.child("Users").child(buyerId)
                            .child("waitingPayment")
                            .child(idPembayaran)
                            .child("itemOrdered")
                            .child(idProduk)
                    val value2 = OrderFishermanEntity(
                        nelayanId,
                        idProduk,
                        namaIkan,
                        harga,
                        linkImage,
                        tokoIkan
                    )
                    reference2.setValue(value2).addOnCompleteListener { er ->
                        if (er.isSuccessful) {

                        } else {
                            Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                    .show()
            }
        }

//        var forDataIkan = ""
//        if (dataIkan != null) {
//            for (i in 0 until (dataIkan.size)) {
//                val tmp = dataIkan[i]
//                forDataIkan += if (i != (dataIkan.size - 1)) {
//                    ("${tmp.userId} ${tmp.idProduk} ${tmp.namaIkan} ${tmp.tokoIkan} ${tmp.linkImage} ${tmp.harga}, ")
//                } else {
//                    ("${tmp.userId} ${tmp.idProduk} ${tmp.namaIkan} ${tmp.tokoIkan} ${tmp.linkImage} ${tmp.harga}")
//                }
//            }
//        }


    }
}