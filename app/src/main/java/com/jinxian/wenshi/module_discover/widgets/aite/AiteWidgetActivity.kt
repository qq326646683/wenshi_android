package com.jinxian.wenshi.module_discover.widgets.aite

import android.text.Editable
import android.text.TextWatcher
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.ext.errorToast
import com.jinxian.wenshi.ext.infoToast
import com.jinxian.wenshi.module_discover.widgets.aite.widget.MentionSpanFactory
import com.jinxian.wenshi.module_discover.widgets.aite.widget.MentionUserEntity
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_widget_aite.*

class AiteWidgetActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_widget_aite

    override fun initView() {
        MentionSpanFactory.initDeleteMention(etInput)

        etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1 && !s.isNullOrEmpty() && s.toString()[start] == '@') {
                    XPopup.Builder(this@AiteWidgetActivity)
                        .asCenterList(
                            "选择@的人",
                            arrayOf("刘德华", "周华健", "陈小春", "李寻欢")
                        ) { index, result ->
                            // 将手动输入的@移除
                            var index = etInput.selectionStart
                            val str = etInput.text.toString()
                            if ("" != str && index > 0) {
                                etInput.text?.delete(index - 1, index)
                            }
                            // 字数限制
                            val willInputLength = result.length + 2
                            if ((etInput.text?.length ?: 0) + willInputLength > 50) {
                                errorToast("最多输入50字")
                            }
                            // 添加@人员
                            val mentionUsers = listOf(MentionUserEntity("1", result))
                            MentionSpanFactory.appendMentionUsers(etInput, mentionUsers)

                        }
                        .show()

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        btnGetAite.setOnClickListener {
            val editable = etInput.text
            val spans: Array<out MentionUserEntity>? =
                editable?.getSpans(0, editable.length, MentionUserEntity::class.java)
            val resultList = spans?.map {
                it.name
            }
            infoToast(resultList.toString())
        }
    }
}