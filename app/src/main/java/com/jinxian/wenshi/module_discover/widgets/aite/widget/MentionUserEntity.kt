package com.jinxian.wenshi.module_discover.widgets.aite.widget

import android.graphics.Color
import android.os.Parcelable
import android.text.SpannableString
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MentionUserEntity(val id: String?, val name: String?) : BaseMentionEntity, MentionDataBindingSpan, Parcelable {

    override fun getSpannableString(): SpannableString {
        return SpannableString("@$name")
    }

    override fun getSpanColor(): Int {
        return Color.parseColor("#5975FF")
    }
}