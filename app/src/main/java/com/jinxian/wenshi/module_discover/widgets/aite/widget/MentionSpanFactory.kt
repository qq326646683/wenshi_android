package com.jinxian.wenshi.module_discover.widgets.aite.widget

import android.text.*
import android.view.KeyEvent
import android.widget.EditText

object MentionSpanFactory {
    // setSpan的时候，end参数指定为source.length,保证光标不会落在字符串的中间，直接落在最后的位置
    // 区间标志，在这里并无影响，使用TextView显示的时候，请指定两端都为开区间
    private fun newSpannable(source: CharSequence, vararg spans: Any): Spannable {
        return SpannableString.valueOf(source).apply {
            spans.forEach {
                setSpan(it, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    // 添加at特征
    fun appendMentionUsers(etInput: EditText, users: List<MentionUserEntity>) {
        users.forEach {
            val index = etInput.selectionStart
            val spannableStringBuilder = etInput.text as SpannableStringBuilder
            val newSpannable = newSpannable(it.getSpannedName(), it)
            if (index < 0 || index >= etInput.length()) {
                spannableStringBuilder.append(SpannableStringBuilder(newSpannable)).append(" ")
            } else {
                spannableStringBuilder.insert(index, newSpannable)
                spannableStringBuilder.insert(etInput.selectionStart, " ")
            }
        }
    }

    // 删除at特征
    fun initDeleteMention(etInput: EditText) {
        val watcher = SelectionSpanWatcher(MentionDataBindingSpan::class)
        etInput.setEditableFactory(NoCopySpanEditableFactory(watcher))
        etInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener KeyCodeDeleteHelper.onDelDown(
                    (v as EditText).text)
            }
            return@setOnKeyListener false
        }
    }
}