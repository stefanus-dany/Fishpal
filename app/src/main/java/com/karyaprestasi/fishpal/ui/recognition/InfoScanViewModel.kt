package com.karyaprestasi.fishpal.ui.recognition

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karyaprestasi.fishpal.utils.DataFirebase

class InfoScanViewModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context

    fun getDataInfoScan(fish : String): LiveData<MutableList<InfoScanEntity>> {
        val mutableData = MutableLiveData<MutableList<InfoScanEntity>>()
        DataFirebase.getDataInfoScan(fish).observeForever {
            mutableData.value = it
        }
        return mutableData
    }

}