package com.karyaprestasi.fishpal.ui.main_ui.profile.buyer.transaction

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karyaprestasi.fishpal.ui.main_ui.marketplace.checkout.PaymentEntity
import com.karyaprestasi.fishpal.ui.main_ui.profile.OrderFishermanEntity
import com.karyaprestasi.fishpal.utils.DataFirebase


class TransactionViewModel : ViewModel() {

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

    fun getIdShipping(): LiveData<MutableList<String>> {
        val mutableData = MutableLiveData<MutableList<String>>()
        DataFirebase.getIdShipping().observeForever {
            mutableData.value = it
        }
        return mutableData
    }

    fun getItemShipping(idOrdered: List<String>): LiveData<MutableList<OrderFishermanEntity>> {
        val mutableData = MutableLiveData<MutableList<OrderFishermanEntity>>()
        DataFirebase.getItemShipping(idOrdered).observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}