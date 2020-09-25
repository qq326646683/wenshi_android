package com.jinxian.wenshi.ext

import android.widget.Toast
import com.jinxian.wenshi.AppContext
import es.dmoral.toasty.Toasty

fun infoToast(success: String) {
    Toasty.info(AppContext, success, Toast.LENGTH_SHORT, true).show()
}
fun infoToast(success: Int) {
    Toasty.info(AppContext, success, Toast.LENGTH_SHORT, true).show()
}
fun errorToast(error: String) {
    Toasty.info(AppContext, error, Toast.LENGTH_SHORT, true).show()
}
fun errorToast(error: Int) {
    Toasty.info(AppContext, error, Toast.LENGTH_SHORT, true).show()
}