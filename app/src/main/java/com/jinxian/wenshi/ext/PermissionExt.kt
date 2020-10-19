package com.jinxian.wenshi.ext

import android.app.Activity
import com.afollestad.assent.rationale.RationaleHandler
import com.afollestad.assent.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Activity.runWithPermissions(
    vararg permissions: Permission,
    requestCode: Int = 40,
    rationaleHandler: RationaleHandler? = null,
): Boolean = suspendCancellableCoroutine { continuation ->
    isAllGranted(*permissions).yes {
        continuation.resume(true)
    }.otherwise {
        askForPermissions(
            *permissions,
            requestCode = requestCode,
            rationaleHandler = rationaleHandler
        ) {
            it.isAllGranted(*permissions).yes {
                continuation.resume(true)
            }.otherwise {
                continuation.resume(false)
            }

        }
    }
}
