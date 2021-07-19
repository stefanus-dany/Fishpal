package com.project.fishbud.ui.main_ui.marketplace.checkout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.project.fishbud.model.UserModel
import com.project.fishbud.ui.main_ui.profile.tambahProduk.TambahProdukEntity
import com.project.fishbud.utils.DataFirebase
import java.util.concurrent.Executors


class PaymentViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context

    //implementasi alfabet acak untuk id
    private var alphabet: List<Char> = emptyList()
    private lateinit var idPembayaran: String
    private var bgthread: Thread? = null
    private var check = false

    init {
        alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        idPembayaran = List(20) { alphabet.random() }.joinToString("")
    }

    fun getDataUser(): LiveData<MutableList<UserModel>> {
        val mutableData = MutableLiveData<MutableList<UserModel>>()
        DataFirebase.getDataUser().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun storeToDatabase(
        buyerName : String,
        buyerId : String,
        alamat : String,
        kartuKredit : String,
        jenisPengiriman : String,
        totalHarga : Long,
        timeDate : String
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // Simulate process in background thread
            try {
                if (!check) {
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
                        timeDate
                    )
                    reference.setValue(value).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                mContext,
                                "Produk telah ditambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(mContext, "Error from database", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                // Update ui in main thread
                bgthread = Thread()
                bgthread?.start()
            }
        }

    }
}