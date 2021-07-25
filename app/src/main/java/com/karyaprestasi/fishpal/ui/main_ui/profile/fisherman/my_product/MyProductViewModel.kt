package com.karyaprestasi.fishpal.ui.main_ui.profile.fisherman.my_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karyaprestasi.fishpal.ui.main_ui.marketplace.IkanEntity
import com.karyaprestasi.fishpal.utils.DataFirebase

class MyProductViewModel : ViewModel() {

    fun getMyProduct(): LiveData<MutableList<IkanEntity>> {
        val mutableData = MutableLiveData<MutableList<IkanEntity>>()
        DataFirebase.getMyProduct().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}