package com.project.fishbud.ui.main_ui.profile.fisherman.open_store

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DaftarTokoEntity(
    var storeName : String = "",
    var storeCity : String = "",
    var storeAddress : String = "",
    var ktpUrl : String = "",
    var selfPhoto : String = ""
) : Parcelable