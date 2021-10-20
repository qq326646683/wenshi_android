package com.jinxian.wenshi.module_discover.widgets.repeat_viewpager

import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseActivity
import com.jinxian.wenshi.util.device.ScreenUtil
import kotlinx.android.synthetic.main.activity_repeat_viewpager.*

class RepeatViewPagerActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_repeat_viewpager

    val contentDatas = mutableListOf(
        "content_剑侠客.jpeg",
        "content_龙太子.jpeg",
        "content_逍遥生.jpeg",
        "content_骨精灵.jpeg",
        "content_虎头怪.jpeg"
    )
    val navigationDatas =
        mutableListOf("a_剑侠客.jpg", "a_龙太子.jpg", "a_逍遥生.jpg", "a_骨精灵.jpg", "a_虎头怪.jpg")
    private val indexArray = arrayOf(3, 4, 0, 1, 2)

    private var repeatPagerAdapter: RepeatPagerAdapter? = null
    private var currentPosition = 2
    private var lastIdlePositon = 2
    private var navigationOffsetPageSize = 0

    override fun initView() {
        ScreenUtil.setFullScreen(this)
        initViewPager()
        initFragment()
        initNavigation()
    }

    private fun initViewPager() {
        vpRepeat.addOnPageChangeListener(object : ScrollableViewPager.OnPageChangeListener {
            private var isScrolling = false
            private val vpNavigationAnimRunable: Runnable = Runnable {
                if (!isScrolling) {
                    vpNavigationContainer?.animate()?.apply {
                        translationY(vpNavigationContainer.height.toFloat())
                        duration = 500
                    }
                }
            }


            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset != 0f) {
                    if (position == 2 && (currentPosition == 2 || currentPosition == 3)) {
                        vpNavigation.scrollX =
                            (navigationOffsetPageSize * vpNavigation.width + vpNavigation.width * positionOffset).toInt()
                    }

                    if (position == 1 && (currentPosition == 1 || currentPosition == 2)) {
                        vpNavigation.scrollX =
                            (navigationOffsetPageSize * vpNavigation.width - vpNavigation.width * (1 - positionOffset)).toInt()
                    }
                    vpNavigation.doTransformPage()
                }
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                // 移动Fragment,做循环效果
                if (state == ScrollableViewPager.SCROLL_STATE_IDLE) {
                    if (lastIdlePositon != currentPosition) {
                        if (currentPosition > lastIdlePositon) {
                            //滑到了下一个
                            repeatPagerAdapter?.moveFirstDataToLast()
                            navigationOffsetPageSize++

                        } else {
                            //滑到了上一个
                            repeatPagerAdapter?.moveLastDataToFirst()
                            navigationOffsetPageSize--

                        }
                        vpNavigation.setCurrentItem(5 * 250 + navigationOffsetPageSize, false)
                        lastIdlePositon = currentPosition
                    }
                }

                // 导航位移动画
                if (state == ScrollableViewPager.SCROLL_STATE_IDLE) {
                    isScrolling = false
                    vpNavigationContainer.postDelayed(vpNavigationAnimRunable, 1000)
                } else {
                    vpNavigationContainer.removeCallbacks(vpNavigationAnimRunable)
                    if (!isScrolling) {
                        isScrolling = true
                        vpNavigationContainer?.animate()?.apply {
                            translationY(0f)
                            duration = 500
                        }
                    }
                }

                // 防止用户滑动太快，导致IDLE状态不执行
                if (state == ScrollableViewPager.SCROLL_STATE_SETTLING) {
                    vpRepeat.setScrollable(false)
                } else if (state == ScrollableViewPager.SCROLL_STATE_IDLE) {
                    vpRepeat.setScrollable(true)
                }

                // item里播放动画
                if (state == ScrollableViewPager.SCROLL_STATE_IDLE) {
                    repeatPagerAdapter?.getCurrentFragment()?.doAnim()
                }
            }

        })
    }

    private fun initFragment() {
        val datas = mutableListOf<String>()
        indexArray.map {
            datas.add(contentDatas[it])
        }
        repeatPagerAdapter = RepeatPagerAdapter(supportFragmentManager, datas)
        vpRepeat.adapter = repeatPagerAdapter
        vpRepeat.offscreenPageLimit = 3
        vpRepeat.currentItem = 2
    }


    private fun initNavigation() {
        vpNavigation.adapter = NavigationPagerAdapter(navigationDatas)
        vpNavigation.offscreenPageLimit = 3
        vpNavigation.setPageTransformer(true, NavigationScaleTransformer())
        vpNavigation.setCurrentItem(navigationDatas.size * 250, false)
    }

}