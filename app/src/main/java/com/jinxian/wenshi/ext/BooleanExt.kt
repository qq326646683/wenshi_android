package com.jinxian.wenshi.ext

// Boolean扩展，舍弃if else


/*
密封类一般可以当枚举用
sealed class MyColor {
    class Yellow : MyColor()

    class Red : MyColor()

    class Black : MyColor()
}
 */
sealed class BooleanExt<out T> //协变+密封类 = 增强版枚举

class Success<T>(val data: T) : BooleanExt<T>() //data是协变点（get 方法）

object OtherWise : BooleanExt<Nothing>() //Nothing是任何类的子类

inline fun <T> Boolean.yes(block: () -> T): BooleanExt<T> = // inline 内联函数： 会把这个函数方法体中的所以代码移动到调用的地方
    when {
        this -> Success(block())
        else -> OtherWise
    }

inline fun <T> Boolean.no(block: () -> T): BooleanExt<T> =
    when {
        this -> OtherWise
        else -> Success(block())
    }

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
    when (this) {
        is Success -> this.data
        OtherWise -> block()
    }