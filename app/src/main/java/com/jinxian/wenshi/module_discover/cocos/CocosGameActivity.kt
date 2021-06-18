package com.jinxian.wenshi.module_discover.cocos

import com.cocos.lib.CocosActivity

class CocosGameActivity: CocosActivity() {
    override fun filePath(): String = intent.getStringExtra("path")
}