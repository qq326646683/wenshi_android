package com.jinxian.wenshi.ext

import android.app.Activity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.afollestad.assent.rationale.RationaleHandler
import com.afollestad.assent.*

fun Activity.runWithPermissions(
    vararg permissions: Permission,
    requestCode: Int = 40,
    rationaleHandler: RationaleHandler? = null,
    granted: Callback,
    denied: Callback
) {
    isAllGranted(*permissions).yes {
        val permissionList = permissions.asList()
        val grantResultList = IntArray(permissionList.size)
        permissionList.forEachIndexed { index, permission ->
            grantResultList[index] = PERMISSION_GRANTED
        }
        granted.invoke(AssentResult(permissionList, grantResultList))
    }.otherwise {
        askForPermissions(
            *permissions,
            requestCode = requestCode,
            rationaleHandler = rationaleHandler
        ) {
            it.isAllGranted(*permissions).yes {
                granted.invoke(it)
            }.otherwise {
                denied.invoke(it)
            }
        }
    }
}
