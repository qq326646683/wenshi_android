package com.jinxian.wenshi.module_discover.widgets.repeat_viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jinxian.wenshi.R
import kotlinx.android.synthetic.main.fragment_repeat_viewpager.*

class RepeatFragment : Fragment() {
    var mImgUrl: String? = null

    companion object {
        fun newInstance(imgUrl: String): RepeatFragment {
            val args = Bundle().apply {
                putString("imgUrl", imgUrl)
            }
            return RepeatFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImgUrl = arguments?.getString("imgUrl")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repeat_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("nell-RepeatFragment", mImgUrl)
        Glide.with(requireContext())
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .centerCrop()
            )
            .load("http://file.jinxianyun.com/$mImgUrl")
            .into(iv)

        Glide.with(requireContext())
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .centerCrop()
            )
            .load("http://file.jinxianyun.com/$mImgUrl")
            .into(ivAnim)
    }

    fun doAnim() {
        ivAnim?.animate()?.apply {
            alpha(1f)
            scaleX(1f)
            scaleY(1f)
            duration = 500
        }?.withEndAction {
            ivAnim.alpha = 0f
            ivAnim.scaleX = 0f
            ivAnim.scaleY = 0f
        }
    }
}