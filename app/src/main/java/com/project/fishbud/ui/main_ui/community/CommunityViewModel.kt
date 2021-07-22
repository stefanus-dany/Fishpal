package com.project.fishbud.ui.main_ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.fishbud.utils.DataFirebase

class CommunityViewModel : ViewModel() {

    fun getDataThread(): LiveData<MutableList<CommunityEntity>> {
        val mutableData = MutableLiveData<MutableList<CommunityEntity>>()
        DataFirebase.getDataThread().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}