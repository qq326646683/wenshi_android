package com.jinxian.wenshi.base.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

typealias LaunchBlock = suspend CoroutineScope.() -> Unit

typealias EmitBlock<T> = suspend LiveDataScope<T>.() -> T

typealias Cancel = (e: Exception) -> Unit

open class BaseViewModel : ViewModel() {

    val mStateLiveData = MutableLiveData<StateActionEvent>()

    //使用协程封装统一的网络请求处理
    fun launch(cancel: Cancel? = null, block: LaunchBlock) {
        // ViewModel自带的viewModelScope.launch,会在页面销毁时自动取消请求，防止内存泄漏
        viewModelScope.launch {
            try {
                mStateLiveData.value = LoadState
                block()
                mStateLiveData.value = SuccessState
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> cancel?.invoke(e)
                    else -> mStateLiveData.value = ErrorState(e.message)
                }
            }
        }
    }

    fun <T> emit(cancel: Cancel? = null, block: EmitBlock<T>): LiveData<T> = liveData {
        try {
            mStateLiveData.value = LoadState
            emit(block())
            mStateLiveData.value = SuccessState
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> cancel?.invoke(e)
                else -> mStateLiveData.value = ErrorState(e.message)
            }
        }
    }


}

