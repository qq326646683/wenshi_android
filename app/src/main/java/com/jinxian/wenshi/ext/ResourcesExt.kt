package com.jinxian.wenshi.ext

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.jinxian.wenshi.AppContext

private val sTmpValue: TypedValue by lazy { TypedValue() }


fun <T : Any> T.getString(@StringRes resId: Int): String? = resId.res2String()

fun Int.res2Color(): Int = if (AppContext == null) {
    0
} else {
    ContextCompat.getColor(AppContext!!, this)
}

fun Int.res2String(): String? = AppContext?.getString(this)

fun Int.res2String(vararg args: Any): String? = AppContext?.getString(this, *args)

fun Int.res2Drawable(): Drawable? = if (AppContext == null) {
    null
} else {
    ContextCompat.getDrawable(AppContext!!, this)
}

fun Int.res2StringArray(): Array<String>? =
    AppContext?.resources?.getStringArray(this)

fun Int.res2IntArray(): IntArray? = AppContext?.resources?.getIntArray(this)

fun Int.res2Dimension(): Int = AppContext?.resources?.getDimensionPixelSize(this) ?: 0

fun Int.resAttr2Color(theme: Resources.Theme?): Int {
    if (theme?.resolveAttribute(this, sTmpValue, true) != true) {
        return 0
    }

    return if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
        sTmpValue.data.resAttr2Color(theme)
    } else sTmpValue.data
}

fun Int.resAttr2Color(context: Context?): Int {
    return this.resAttr2Color(context?.theme)
}

fun Int.resAttr2FloatValue(context: Context?): Float {
    return this.resAttr2FloatValue(context?.theme)
}

fun Int.resAttr2FloatValue(theme: Resources.Theme?): Float {
    return if (theme?.resolveAttribute(this, sTmpValue, true) != true) {
        0F
    } else sTmpValue.float
}

fun Int.resAttr2String(context: Context?): CharSequence? {
    if (context?.theme?.resolveAttribute(this, sTmpValue, true) != true) {
        return null
    }
    return sTmpValue.string
}

@JvmOverloads
fun Int.resAttr2Int(context: Context?, dftValue: Int = -1): Int {
    if (context?.theme?.resolveAttribute(this, sTmpValue, true) != true) {
        return dftValue
    }
    return sTmpValue.data
}
