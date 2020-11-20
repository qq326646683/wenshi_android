package com.jinxian.wenshi.module_main.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import com.jinxian.wenshi.R
import com.jinxian.wenshi.BR
import com.jinxian.wenshi.base.fragment.BaseDataBindVMFragment
import com.jinxian.wenshi.base.fragment.BaseListVMFragment
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.databinding.FragmentHomeBinding
import com.jinxian.wenshi.databinding.LayoutHomeTabItemBinding
import com.jinxian.wenshi.module_main.model.TalkModel
import com.jinxian.wenshi.module_main.viewmodel.HomeViewModel
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.common_refresh_recyclerview.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseListVMFragment<TalkModel>() {

    private val mViewModel: HomeViewModel by viewModel()

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initRecyclerView() {

        val type = Type<LayoutHomeTabItemBinding>(R.layout.layout_home_tab_item)
            .onClick {

            }

        LastAdapter(mListData, BR.homeTabItem)
            .map<TalkModel>(type)
            .into(mRecyclerView.apply {
                layoutManager = LinearLayoutManager(mActivity)
            })

    }

    override fun initData() {
        getListData()

    }

    override fun getListData() {
        mViewModel.getHomeData(mPage).observe(this, mListObserver)
    }


}