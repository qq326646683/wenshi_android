package com.jinxian.wenshi.module_discover.cocos

import android.app.AlertDialog
import android.os.Bundle
import com.cocos.bridge.CocosBridgeHelper
import com.cocos.bridge.CocosDataListener
import com.cocos.lib.CocosActivity

class CocosGameActivity: CocosActivity() {
    private val showArray = arrayOf("刘德华", "周华健")
    private val cocosListenerInCocos: CocosDataListener = CocosDataListener { action, argument, callbackId ->
        CocosBridgeHelper.log("接收InCocos", action)
        if (action == "action_showStarDialog") {
            runOnUiThread {
                AlertDialog.Builder(this)
                    .setTitle("选择")
                    .setItems(showArray) { _, index ->
                        CocosBridgeHelper.getInstance().nativeCallCocos(action, showArray[index], callbackId)
                    }
                    .create()
                    .show()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CocosBridgeHelper.getInstance().addCocosListener(cocosListenerInCocos)
    }

    override fun onDestroy() {
        super.onDestroy()
        CocosBridgeHelper.getInstance().removeCocosListener(cocosListenerInCocos)
    }

    override fun filePath(): String = intent.getStringExtra("path")
}