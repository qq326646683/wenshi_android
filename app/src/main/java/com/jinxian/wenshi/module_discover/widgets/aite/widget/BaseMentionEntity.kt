package com.jinxian.wenshi.module_discover.widgets.aite.widget

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

interface BaseMentionEntity {
    // 作为span的字符串
    fun getSpannableString(): SpannableString
    // span显示的颜色
    fun getSpanColor():Int

    fun getSpannedName(): Spannable {
        return getSpannableString().apply {
            setSpan(ForegroundColorSpan(getSpanColor()),0,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}