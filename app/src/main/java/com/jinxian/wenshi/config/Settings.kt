package com.jinxian.wenshi.config

import com.jinxian.wenshi.constant.Constant
import com.jinxian.wenshi.data.storage.Preference

object Settings {
    object Account {
        var token by Preference(Constant.USER_TOKEN, "")
    }
}