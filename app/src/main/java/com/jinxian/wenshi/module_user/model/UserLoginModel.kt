package com.jinxian.wenshi.module_user.model

import androidx.databinding.ObservableField

data class UserLoginModel (
    val phone: ObservableField<String> = ObservableField(""),
    val password: ObservableField<String> = ObservableField("")
)