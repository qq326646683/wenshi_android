package com.jinxian.wenshi.ext

import com.jinxian.wenshi.util.device.ScreenUtil
import java.text.DecimalFormat

val Number.dp2px
    get() = ScreenUtil.dp2px(this.toFloat())

val Number.dp2px_f
    get() = ScreenUtil.dp2px_f(this.toFloat())

fun Number.format(pattern: String = "00"): String {
    return DecimalFormat(pattern).format(this)
}

@JvmOverloads
fun Int?.nullOr(dft: Int = 0) = this ?: dft

@JvmOverloads
fun Long?.nullOr(dft: Long = 0) = this ?: dft

@JvmOverloads
fun Float?.nullOr(dft: Float = 0F) = this ?: dft

@JvmOverloads
fun Double?.nullOr(dft: Double = 0.toDouble()) = this ?: dft

@JvmOverloads
fun Short?.nullOr(dft: Short = 0) = this ?: dft

@JvmOverloads
fun Byte?.nullOr(dft: Byte = 0) = this ?: dft

/**
 * 满足条件，则返回给定的值，否则返回自身
 */
fun <T : Number> T?.assignIf(condition: T?.() -> Boolean, value: T): T? {
    return if (condition()) {
        value
    } else {
        this
    }
}