package com.jinxian.wenshi.module_discover.widgets.aite.widget

import android.text.Selection
import android.text.Spannable

object KeyCodeDeleteHelper {
    fun onDelDown(text: Spannable): Boolean {
        val selectionStart = Selection.getSelectionStart(text)
        val selectionEnd = Selection.getSelectionEnd(text)
        text.getSpans(selectionStart, selectionEnd, MentionDataBindingSpan::class.java).firstOrNull { text.getSpanEnd(it) == selectionStart }?.run {
            return (selectionStart == selectionEnd).also {
                val spanStart = text.getSpanStart(this)
                val spanEnd = text.getSpanEnd(this)
                Selection.setSelection(text, spanStart, spanEnd)
            }
        }
        return false
    }
}