package com.jinxian.wenshi.ext

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import com.jinxian.wenshi.util.FunctionUtil
import com.jinxian.wenshi.util.FunctionUtil.DEFAULT_DURATION_TIME

var View.visible: Boolean
    get() {
        return visibility == View.VISIBLE
    }
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.invisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

var View.gone: Boolean
    get() {
        return visibility == View.GONE
    }
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }

fun <T : View> T.setVisible(visible: Boolean, action: (T.() -> Unit)? = null): T {
    if (visible) {
        visibility = View.VISIBLE
        action?.invoke(this)
    } else {
        visibility = View.GONE
    }
    return this
}

fun <T : View> T.setInVisible(invisible: Boolean, trueAction: (T.() -> Unit)? = null): T {
    if (invisible) {
        visibility = View.INVISIBLE
        trueAction?.invoke(this)
    } else {
        visibility = View.VISIBLE
    }
    return this
}

fun <T : View> T.setGone(gone: Boolean, trueAction: (T.() -> Unit)? = null): T {
    if (gone) {
        visibility = View.GONE
        trueAction?.invoke(this)
    } else {
        visibility = View.VISIBLE
    }
    return this
}

fun View.setCornerRadius(color: Int = Color.WHITE, cornerRadius: Int = 15.dp2px) {
    background = GradientDrawable().apply {
        setColor(color)
        setCornerRadius(cornerRadius.toFloat())
    }
}

fun View.setCornerRadii(
    color: Int = Color.WHITE,
    leftTop: Int = 0,
    rightTop: Int = 0,
    rightBottom: Int = 0,
    leftBottom: Int = 0
) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadii = floatArrayOf(
            leftTop.toFloat(),
            leftTop.toFloat(),
            rightTop.toFloat(),
            rightTop.toFloat(),
            rightBottom.toFloat(),
            rightBottom.toFloat(),
            leftBottom.toFloat(),
            leftBottom.toFloat()
        )
    }
}

var View.startMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart = value
    }

var View.leftMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = value
    }

var View.topMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
    }

var View.endMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd = value
    }

var View.rightMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = value
    }

var View.bottomMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = value
    }
var View.size: Pair<Int, Int>
    get() {
        return (layoutParams?.width ?: 0) to (layoutParams?.height ?: 0)
    }
    set(value) {
        layoutParams?.width = value.first
        layoutParams?.height = value.second
    }

var View.bottomPadding: Int
    get(): Int {
        return paddingBottom
    }
    set(value) {
        if (value != paddingBottom) {
            setPadding(paddingLeft, paddingTop, paddingRight, value)
        }
    }

var View.topPadding: Int
    get(): Int {
        return paddingTop
    }
    set(value) {
        if (value != paddingTop) {
            setPadding(paddingLeft, value, paddingRight, paddingBottom)
        }
    }

var View.rightPadding: Int
    get(): Int {
        return paddingRight
    }
    set(value) {
        if (value != paddingRight) {
            setPadding(paddingLeft, paddingTop, value, paddingBottom)
        }
    }

var View.leftPadding: Int
    get(): Int {
        return paddingLeft
    }
    set(value) {
        if (value != paddingLeft) {
            setPadding(value, paddingTop, paddingRight, paddingBottom)
        }
    }

var View.startPadding: Int
    get(): Int {
        return paddingStart
    }
    set(value) {
        if (value != paddingStart) {
            setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)
        }
    }

var View.endPadding: Int
    get(): Int {
        return paddingEnd
    }
    set(value) {
        if (value != paddingEnd) {
            setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)
        }
    }

fun <T : View> T.setOnLimitFastClickListener(
    duration: Long = DEFAULT_DURATION_TIME,
    continueCall: (() -> Unit)? = null,
    doThing: () -> Unit
): T {
    setOnClickListener {
        FunctionUtil.throttle(duration, continueCall, doThing)
    }
    return this
}


