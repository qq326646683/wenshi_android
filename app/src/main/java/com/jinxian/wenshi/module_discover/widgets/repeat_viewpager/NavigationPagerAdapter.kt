package com.jinxian.wenshi.module_discover.widgets.repeat_viewpager

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jinxian.wenshi.R

class NavigationPagerAdapter(val mData: List<String>) : ScrollablePagerAdapter() {
    override fun getCount(): Int {
        return mData.size * 500
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(container.context, R.layout.layout_repeat_navigation_item, null)
        val iv = view.findViewById<ImageView>(R.id.ivNavigation)
        val ivBorder = view.findViewById<ImageView>(R.id.ivBorder)
        Glide.with(container.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .transform(RoundedCorners(30))
            )
            .load("http://file.jinxianyun.com/${mData[position % mData.size]}")
            .into(iv)
        Glide.with(container.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .transform(RoundedCorners(30))
            )
            .load(R.mipmap.navigation_item_border)
            .into(ivBorder)
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)

    }

}