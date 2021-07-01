package com.jinxian.wenshi.util.device

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowManager
import com.jinxian.wenshi.ext.dp2px

object NotchUtil {
    private const val TAG = "NotchUtil"

    private const val NOTCH_IN_SCREEN_VOIO = 0x00000020
    private const val MIUI_NOTCH = "ro.miui.notch"

    private var sHasNotch: Boolean? = null
    private var sRotation0SafeInset: Rect? = null
    private var sRotation90SafeInset: Rect? = null
    private var sRotation180SafeInset: Rect? = null
    private var sRotation270SafeInset: Rect? = null
    private var sNotchSizeInHawei: IntArray? = null
    private var sHuaweiIsNotchSetToShow: Boolean? = null

    @JvmStatic
    fun isNotchOfficialSupport(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    @JvmStatic
    fun hasNotch(view: View?): Boolean? {
        if (sHasNotch == null) {
            if (isNotchOfficialSupport()) {
                if (!attachHasOfficialNotch(view)) {
                    return false
                }
            } else {
                sHasNotch = has3rdNotch(view?.context)
            }
        }
        return sHasNotch
    }

    @JvmStatic
    fun hasNotch(activity: Activity?): Boolean? {
        if (sHasNotch == null) {
            if (isNotchOfficialSupport()) {
                val window = activity?.window ?: return false
                val decorView = window.decorView ?: return false
                if (!attachHasOfficialNotch(decorView)) {
                    return false
                }
            } else {
                sHasNotch = has3rdNotch(activity)
            }
        }
        return sHasNotch
    }

    /**
     * @return false indicates the failure to get the result
     */
    @TargetApi(28)
    private fun attachHasOfficialNotch(view: View?): Boolean {
        val windowInsets = view?.rootWindowInsets
        return if (windowInsets != null) {
            val displayCutout = windowInsets.displayCutout
            sHasNotch = displayCutout != null
            true
        } else { // view not attached, do nothing
            false
        }
    }

    fun has3rdNotch(context: Context?): Boolean {
        context?.run {
            if (DeviceUtil.isHuawei()) {
                return hasNotchInHuawei(this)
            } else if (DeviceUtil.isVivo()) {
                return hasNotchInVivo(this)
            } else if (DeviceUtil.isOppo()) {
                return hasNotchInOppo(this)
            } else if (DeviceUtil.isXiaomi()) {
                return hasNotchInXiaomi(this)
            }
        }
        return false
    }

    fun hasNotchInHuawei(context: Context): Boolean {
        var hasNotch = false
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            hasNotch = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "hasNotchInHuawei ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e(TAG, "hasNotchInHuawei NoSuchMethodException")
        } catch (e: Exception) {
            Log.e(TAG, "hasNotchInHuawei Exception")
        }
        return hasNotch
    }

    @SuppressLint("PrivateApi")
    fun hasNotchInVivo(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val ftFeature = cl.loadClass("android.util.FtFeature")
            val methods = ftFeature.declaredMethods
            if (methods != null) {
                for (i in methods.indices) {
                    val method = methods[i]
                    if (method.name.equals("isFeatureSupport", ignoreCase = true)) {
                        ret = method.invoke(ftFeature, NOTCH_IN_SCREEN_VOIO) as Boolean
                        break
                    }
                }
            }
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "hasNotchInVivo ClassNotFoundException")
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "hasNotchInVivo Exception")
        }
        return ret
    }

    fun hasNotchInOppo(context: Context): Boolean {
        return context.packageManager
            .hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    @SuppressLint("PrivateApi")
    fun hasNotchInXiaomi(context: Context?): Boolean {
        try {
            val spClass = Class.forName("android.os.SystemProperties")
            val getMethod = spClass.getDeclaredMethod(
                "getInt", String::class.java,
                Int::class.javaPrimitiveType
            )
            getMethod.isAccessible = true
            val hasNotch = getMethod.invoke(null, MIUI_NOTCH, 0) as Int
            return hasNotch == 1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getSafeInsetTop(activity: Activity?): Int {
        return if (hasNotch(activity) != true) {
            0
        } else getSafeInsetRect(activity)?.top ?: 0
    }

    fun getSafeInsetTop(view: View?): Int {
        return if (hasNotch(view) != true) {
            0
        } else getSafeInsetRect(view)?.top ?: 0
    }

    fun getSafeInsetLeft(activity: Activity?): Int {
        return if (hasNotch(activity) != true) {
            0
        } else getSafeInsetRect(activity)?.left ?: 0
    }

    @JvmStatic
    fun getSafeInsetLeft(view: View?): Int {
        return if (hasNotch(view) != true) {
            0
        } else getSafeInsetRect(view)?.left ?: 0
    }

    fun getSafeInsetBottom(activity: Activity?): Int {
        return if (hasNotch(activity) != true) {
            0
        } else getSafeInsetRect(activity)?.bottom ?: 0
    }

    fun getSafeInsetBottom(view: View?): Int {
        return if (hasNotch(view) != true) {
            0
        } else getSafeInsetRect(view)?.bottom ?: 0
    }

    @JvmStatic
    fun getSafeInsetRight(view: View?): Int {
        return if (hasNotch(view) != true) {
            0
        } else getSafeInsetRect(view)?.right ?: 0
    }

    private fun getSafeInsetRect(activity: Activity?): Rect? {
        if (isNotchOfficialSupport()) {
            val rect = Rect()
            val decorView = activity?.window?.decorView
            getOfficialSafeInsetRect(decorView, rect)
            return rect
        }
        return get3rdSafeInsetRect(activity)
    }

    private fun getSafeInsetRect(view: View?): Rect? {
        if (isNotchOfficialSupport()) {
            val rect = Rect()
            getOfficialSafeInsetRect(view, rect)
            return rect
        }
        return get3rdSafeInsetRect(view?.context)
    }

    private fun get3rdSafeInsetRect(context: Context?): Rect? { // 全面屏设置项更改
        if (DeviceUtil.isHuawei()) {
            val isHuaweiNotchSetToShow: Boolean =
                ScreenUtil.huaweiIsNotchSetToShowInSetting(context)
            if (sHuaweiIsNotchSetToShow != null && sHuaweiIsNotchSetToShow != isHuaweiNotchSetToShow) {
                clearLandscapeRectInfo()
            }
            sHuaweiIsNotchSetToShow = isHuaweiNotchSetToShow
        }
        val screenRotation: Int = getScreenRotation(context)
        return if (screenRotation == Surface.ROTATION_90) {
            if (sRotation90SafeInset == null) {
                sRotation90SafeInset = getRectInfoRotation90(context)
            }
            sRotation90SafeInset
        } else if (screenRotation == Surface.ROTATION_180) {
            if (sRotation180SafeInset == null) {
                sRotation180SafeInset = getRectInfoRotation180(context)
            }
            sRotation180SafeInset
        } else if (screenRotation == Surface.ROTATION_270) {
            if (sRotation270SafeInset == null) {
                sRotation270SafeInset = getRectInfoRotation270(context)
            }
            sRotation270SafeInset
        } else {
            if (sRotation0SafeInset == null) {
                sRotation0SafeInset = getRectInfoRotation0(context)
            }
            sRotation0SafeInset
        }
    }


    private fun getRectInfoRotation180(context: Context?): Rect? {
        val rect = Rect()
        if (DeviceUtil.isVivo()) {
            rect.top = 0
            rect.bottom = getNotchHeightInVivo(context)
        } else if (DeviceUtil.isOppo()) {
            rect.top = 0
            rect.bottom = StatusBarUtil.getStatusBarHeight(context)
        } else if (DeviceUtil.isHuawei()) {
            val notchSize: IntArray? = getNotchSizeInHuawei(context)
            rect.top = 0
            rect.bottom = notchSize?.get(1) ?: 0
        } else if (DeviceUtil.isXiaomi()) {
            rect.top = 0
            rect.bottom = getNotchHeightInXiaomi(context)
        }
        return rect
    }

    @TargetApi(28)
    private fun getOfficialSafeInsetRect(view: View?, out: Rect) {
        if (view == null) {
            return
        }
        val rootWindowInsets = view.rootWindowInsets ?: return
        val displayCutout = rootWindowInsets.displayCutout
        if (displayCutout != null) {
            out[displayCutout.safeInsetLeft, displayCutout.safeInsetTop,
                    displayCutout.safeInsetRight] = displayCutout.safeInsetBottom
        }
    }

    private fun clearLandscapeRectInfo() {
        sRotation90SafeInset = null
        sRotation270SafeInset = null
    }

    /**
     * this method is private, because we do not need to handle tablet
     *
     * @param context
     * @return
     */
    private fun getScreenRotation(context: Context?): Int {
        val w = context?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            ?: return Surface.ROTATION_0
        val display = w.defaultDisplay ?: return Surface.ROTATION_0
        return display.rotation
    }

    private fun getRectInfoRotation90(context: Context?): Rect? {
        val rect = Rect()
        if (DeviceUtil.isVivo()) {
            rect.left = getNotchHeightInVivo(context)
            rect.right = 0
        } else if (DeviceUtil.isOppo()) {
            rect.left = StatusBarUtil.getStatusBarHeight(context)
            rect.right = 0
        } else if (DeviceUtil.isHuawei()) {
            if (sHuaweiIsNotchSetToShow == true) {
                rect.left = getNotchSizeInHuawei(context)?.get(1) ?: 0
            } else {
                rect.left = 0
            }
            rect.right = 0
        } else if (DeviceUtil.isXiaomi()) {
            rect.left = getNotchHeightInXiaomi(context)
            rect.right = 0
        }
        return rect
    }

    fun getNotchHeightInVivo(context: Context?): Int {
        return 27.dp2px
    }

    fun getNotchSizeInHuawei(context: Context?): IntArray? {
        if (sNotchSizeInHawei == null) {
            sNotchSizeInHawei = intArrayOf(0, 0)
            try {
                val cl = context?.classLoader
                val HwNotchSizeUtil = cl?.loadClass("com.huawei.android.util.HwNotchSizeUtil")
                val get = HwNotchSizeUtil?.getMethod("getNotchSize")
                sNotchSizeInHawei = get?.invoke(HwNotchSizeUtil) as? IntArray
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "getNotchSizeInHuawei ClassNotFoundException")
            } catch (e: NoSuchMethodException) {
                Log.e(TAG, "getNotchSizeInHuawei NoSuchMethodException")
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "getNotchSizeInHuawei Exception")
            }
        }
        return sNotchSizeInHawei
    }

    fun getNotchHeightInXiaomi(context: Context?): Int {
        val resourceId = context?.resources?.getIdentifier(
            "notch_height", "dimen", "android"
        )
        return if (resourceId ?: 0 > 0) {
            context?.resources?.getDimensionPixelSize(resourceId ?: 0) ?: 0
        } else StatusBarUtil.getStatusBarHeight(context)
    }

    private fun getRectInfoRotation270(context: Context?): Rect? {
        val rect = Rect()
        if (DeviceUtil.isVivo()) {
            rect.right = getNotchHeightInVivo(context)
            rect.left = 0
        } else if (DeviceUtil.isOppo()) {
            rect.right = StatusBarUtil.getStatusBarHeight(context)
            rect.left = 0
        } else if (DeviceUtil.isHuawei()) {
            if (sHuaweiIsNotchSetToShow == true) {
                rect.right = getNotchSizeInHuawei(context)?.get(1) ?: 0
            } else {
                rect.right = 0
            }
            rect.left = 0
        } else if (DeviceUtil.isXiaomi()) {
            rect.right = getNotchHeightInXiaomi(context)
            rect.left = 0
        }
        return rect
    }

    private fun getRectInfoRotation0(context: Context?): Rect? {
        val rect = Rect()
        if (DeviceUtil.isVivo()) {
            rect.top = getNotchHeightInVivo(context)
            rect.bottom = 0
        } else if (DeviceUtil.isOppo()) {
            rect.top = StatusBarUtil.getStatusBarHeight(context)
            rect.bottom = 0
        } else if (DeviceUtil.isHuawei()) {
            val notchSize: IntArray? = getNotchSizeInHuawei(context)
            rect.top = notchSize?.get(1) ?: 0
            rect.bottom = 0
        } else if (DeviceUtil.isXiaomi()) {
            rect.top = getNotchHeightInXiaomi(context)
            rect.bottom = 0
        }
        return rect
    }

    /**
     * fitSystemWindows 对小米、vivo挖孔屏横屏挖孔区域无效
     * @param view
     * @return
     */
    @JvmStatic
    fun needFixLandscapeNotchAreaFitSystemWindow(view: View?): Boolean {
        return (DeviceUtil.isXiaomi() || DeviceUtil.isVivo()) && hasNotch(view) ?: false
    }
}