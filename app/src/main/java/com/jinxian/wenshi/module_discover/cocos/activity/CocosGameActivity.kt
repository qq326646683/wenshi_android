package com.jinxian.wenshi.module_discover.cocos.activity

import android.os.Bundle
import com.cocos.lib.CocosActivity

class CocosGameActivity: CocosActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot) return

    }
}