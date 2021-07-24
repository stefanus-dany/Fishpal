package com.project.fishbud.ui.main_ui.community

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityEntity(
    var writerId : String = "",
    var writerName : String = "",
    var threadId : String = "",
    var threadDesc : String = "",
    var urlProfileImage : String = "",
    var dateTime : String = ""
) : Parcelable