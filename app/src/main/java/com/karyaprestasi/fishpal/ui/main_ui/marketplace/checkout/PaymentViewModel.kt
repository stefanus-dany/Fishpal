package com.karyaprestasi.fishpal.ui.main_ui.marketplace.checkout

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
import com.karyaprestasi.fishpal.model.UserModel
import com.karyaprestasi.fishpal.ui.main_ui.marketplace.IkanEntity
import com.karyaprestasi.fishpal.ui.main_ui.profile.OrderFishermanEntity
import com.karyaprestasi.fishpal.utils.DataFirebase


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
        netPrice: Long,
        timeDate: String,
        date: String,
        dataIkan: ArrayList<IkanEntity>?,
        ikanQuantity: HashMap<Int, Long>
    ) {
        val user = FirebaseAuth.getInstance().currentUser as FirebaseUser
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
            netPrice,
            totalHarga,
            timeDate,
            date
        )
        reference.setValue(value).addOnCompleteListener {
            if (it.isSuccessful) {
                for (i in 0 until (dataIkan!!.size)) {
                    val data = dataIkan[i]
                    //harga per item
                    val hargaQuantity = ikanQuantity[i]
                    Log.i("teg", "cekHargaQuantity: $hargaQuantity")
                    //${tmp.userId} ${tmp.idProduk} ${tmp.namaIkan} ${tmp.tokoIkan} ${tmp.linkImage} ${tmp.harga}
                    val idProduk = data.idProduk
                    val nelayanId = data.userId
                    val namaIkan = data.namaIkan
                    val tokoIkan = data.tokoIkan
                    val linkImage = data.linkImage

                    val cekValue = hargaQuantity.toString()
                    if (cekValue != "0") {

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
                            hargaQuantity!!, //harga peritem
                            totalHarga,
                            linkImage,
                            tokoIkan,
                            idPembayaran,
                            user.uid
                        )
                        Log.i("cekbuyer", "user.uid payment : ${user.uid}")
                        reference2.setValue(value2).addOnCompleteListener { er ->
                            if (er.isSuccessful) {

                            } else {
                                Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                                    .show()
                            }
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