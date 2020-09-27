package com.jinxian.wenshi.module_user.model

import java.io.Serializable

data class UserModel(val id: Int, val phone: String, val avatar: String, val name: String, val token: String) : Serializable