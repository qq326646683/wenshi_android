package com.jinxian.wenshi.ext

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.startActivity(isFinish: Boolean = false) {
    startActivity(Intent(this, T::class.java))
    isFinish.yes {
        finish()
    }
}