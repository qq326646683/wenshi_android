package com.jinxian.wenshi.module_discover.widgets.repeat_viewpager

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class NavigationScaleTransformer : ScrollableViewPager.PageTransformer {
    val MAX_SCALE = 1.0f
    val MIN_SCALE = 0.8f

    override fun transformPage(page: View, position: Float) {
        page.pivotY = page.height.toFloat()

        if (position != 0f) {
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE
        } else {
            page.scaleX = MAX_SCALE
            page.scaleY = MAX_SCALE
        }
        if (abs(position) < 4) {
            val scaleFactor = MIN_SCALE + (1 - abs(position)) * (MAX_SCALE - MIN_SCALE)

            // 动态设置缩放中心点
            if (position > 0) {
                page.pivotX = page.width * 2 * scaleFactor * scaleFactor
            } else if (position < 0) {
                page.pivotX = page.width - page.width * 2 * scaleFactor * scaleFactor
            }

            page.scaleX = scaleFactor
            page.scaleY = scaleFactor

        } else {
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE

        }
    }

}