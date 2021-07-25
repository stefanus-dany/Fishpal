package com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.new_order

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

class NewOrderViewModel : ViewModel() {

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

    fun getIdOrderedFromFisherman(): LiveData<MutableList<String>> {
        val mutableData = MutableLiveData<MutableList<String>>()
        DataFirebase.getIdOrderedFromFisherman().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

//    fun getIdOrderedFromFisherman(idOrdered: List<String>): LiveData<MutableList<String>> {
//        val mutableData = MutableLiveData<MutableList<String>>()
//        DataFirebase.getIdOrderedFromFisherman(idOrdered).observeForever {
//            mutableData.value = it
//        }
//        return mutableData
//    }

    fun getItemOrderedFromFisherman(idOrdered : List<String>): LiveData<MutableList<OrderFishermanEntity>> {
        val mutableData = MutableLiveData<MutableList<OrderFishermanEntity>>()
        DataFirebase.getItemOrderedFromFisherman(idOrdered).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun storeToDatabase(
        nelayanId: String,
        idProduk: String,
        namaIkan: String,
        harga: Long,
        totalHarga : Long,
        linkImage: String,
        tokoIkan: String,
        idPembayaran: String,
        idBuyer: String
    ) {
        Log.i("cekBuyer", "idBuyer: $idBuyer")
        val reference2 =
            FirebaseDatabase.getInstance().reference.child("Users").child(idBuyer)
                .child("shipping")
                .child(idPembayaran)
                .child(idProduk)

        val value = OrderFishermanEntity(
            nelayanId,
            idProduk,
            namaIkan,
            harga,
            totalHarga,
            linkImage,
            tokoIkan,
            idPembayaran,
            idBuyer
        )
        Log.i("cekN", "storeToDatabase: ")
        reference2.setValue(value).addOnCompleteListener {
            if (it.isSuccessful) {

                //hapus dari itemOrdered
                val reference =
                    FirebaseDatabase.getInstance().reference.child("Users").child(nelayanId)
                        .child("itemOrdered")
                        .child(idPembayaran)
                        .child(idProduk)
                reference.removeValue().addOnCompleteListener { er ->
                    if (er.isSuccessful) {
                        Toast.makeText(mContext, "Pesanan diantar!", Toast.LENGTH_SHORT)
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