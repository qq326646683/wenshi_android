package com.jinxian.wenshi.module_main.viewmodel

import androidx.lifecycle.LiveData
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.module_main.model.TalkModel
import com.jinxian.wenshi.module_main.repository.MainRepository

class HomeViewModel(private val mMainRepository: MainRepository) : BaseViewModel() {

    fun getHomeData(page: Int): LiveData<List<TalkModel>> = emit {
        mMainRepository.getTalkList(10, 0, "59114399", "video").objs
    }

}