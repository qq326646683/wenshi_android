package com.jinxian.wenshi.module_main.model
import com.jinxian.wenshi.module_user.model.UserModel
import java.io.Serializable

data class TalkModel(
    val id: String,
    val mediaType: String,
    val describe: String,
    val mediaUrl: String,
    val subUrl: String,
    val likeCount: Int,
    val status: Int,
    val user: UserModel
): Serializable