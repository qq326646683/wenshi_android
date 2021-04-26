package com.jinxian.wenshi.module_main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.constant.Constant.PAGE_SIZE
import com.jinxian.wenshi.module_main.model.TalkModel
import com.jinxian.wenshi.module_main.repository.MainRepository

class HomeViewModel(private val mMainRepository: MainRepository) : BaseViewModel() {
    var talkModelList: LiveData<List<TalkModel>> = MutableLiveData<List<TalkModel>>()


    fun getHomeData(page: Int): LiveData<List<TalkModel>> {
        talkModelList = emit {
            mMainRepository.getTalkList(PAGE_SIZE, (page - 1) * PAGE_SIZE, null, "video").objs
        }
        return talkModelList
    }


}