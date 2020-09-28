package com.jinxian.wenshi.ext

import com.jinxian.wenshi.config.Settings

fun isLogin(): Boolean = !Settings.Account.token.isBlank()