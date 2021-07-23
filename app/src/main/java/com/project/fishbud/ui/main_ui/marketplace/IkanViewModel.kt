package com.project.fishbud.ui.main_ui.marketplace

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.fishbud.utils.DataFirebase

class IkanViewModel : ViewModel() {

    private val displaySearch = MutableLiveData<MutableList<IkanEntity>>()

    fun getDataIkanFromFirebase(): LiveData<MutableList<IkanEntity>> {
        val mutableData = MutableLiveData<MutableList<IkanEntity>>()
        DataFirebase.getDataIkan().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}