package com.jinxian.wenshi.module_user.activity

import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.lifecycle.Observer
import com.jinxian.wenshi.R
import com.jinxian.wenshi.base.activity.BaseDataBindVMActivity
import com.jinxian.wenshi.base.viewmodel.BaseViewModel
import com.jinxian.wenshi.config.Settings
import com.jinxian.wenshi.databinding.ActivityLoginBinding
import com.jinxian.wenshi.ext.otherwise
import com.jinxian.wenshi.ext.yes
import com.jinxian.wenshi.media.FFMediaPlayer
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_DURATION
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_HEIGHT
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_WIDTH
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MSG_DECODER_DONE
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MSG_DECODER_INIT_ERROR
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MSG_DECODER_READY
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MSG_DECODING_TIME
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.MSG_REQUEST_RENDER
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.VIDEO_GL_RENDER
import com.jinxian.wenshi.media.FFMediaPlayer.Companion.VIDEO_RENDER_OPENGL
import com.jinxian.wenshi.module_user.model.UserLoginModel
import com.jinxian.wenshi.module_user.viewmodel.UserInfoUI
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class LoginActivity : BaseDataBindVMActivity<ActivityLoginBinding>(), GLSurfaceView.Renderer,
    FFMediaPlayer.EventCallback {
    private val TAG = "LoginActivity"

    private val mVideoPath =
        Environment.getExternalStorageDirectory().absolutePath + "/byteflow/one_piece.mp4"


    private val mViewModel: UserViewModel by viewModel()

    private val mUserLoginModel: UserLoginModel by lazy { UserLoginModel() }

    private var mMediaPlayer: FFMediaPlayer? = null


    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModel(): BaseViewModel = mViewModel

    override fun initView() {
        mLogin.setOnClickListener {
            clickLogin()
        }

        surfaceView.setEGLContextClientVersion(3)
        surfaceView.setRenderer(this)
        surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        mMediaPlayer = FFMediaPlayer()
        mMediaPlayer?.addEventCallback(this)
        mMediaPlayer?.init(mVideoPath, VIDEO_RENDER_OPENGL, null)
    }

    override fun initData() {
        mDataBind.userLoginModel = mUserLoginModel
    }

    private fun clickLogin() {
        val phone = mUserLoginModel.phone.get().toString()
        val password = mUserLoginModel.password.get().toString()

        phone.isEmpty().yes {
            mPhoneInputLayout.error = getString(R.string.phone_not_null)
            mPhoneInputLayout.isErrorEnabled = true
        }.otherwise {
            mPhoneInputLayout.isErrorEnabled = false
            password.isEmpty().yes {
                mPasswordInputLayout.error = getString(R.string.password_not_null)
                mPasswordInputLayout.isErrorEnabled = true
            }.otherwise {
                mPasswordInputLayout.isErrorEnabled = false
                login()
            }
        }
    }

    private fun login() {
        Log.i("nell-mViewModel-login1", mViewModel.toString())
        mViewModel.login(mUserLoginModel)
        UserInfoUI.mUserModel.observe(this, Observer {
            Log.i("nell-mViewModel-login2", it.toString())
            Settings.Account.token = it.token
            finish()
        })
    }

    override fun onResume() {
        Log.e(TAG, "onResume() called")
        super.onResume()
        mMediaPlayer?.play()
    }

    override fun onPause() {
        super.onPause()
        mMediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.unInit()

    }


    override fun onSurfaceCreated(gl10: GL10?, eglConfig: EGLConfig?) {
        FFMediaPlayer.native_OnSurfaceCreated(VIDEO_GL_RENDER)


    }

    override fun onSurfaceChanged(gl10: GL10?, w: Int, h: Int) {
        Log.d(TAG, "onSurfaceChanged() called with: gl10 = [$gl10], w = [$w], h = [$h]")
        FFMediaPlayer.native_OnSurfaceChanged(VIDEO_GL_RENDER, w, h)
    }

    override fun onDrawFrame(gl10: GL10?) {
        FFMediaPlayer.native_OnDrawFrame(VIDEO_GL_RENDER)
    }


    override fun onPlayerEvent(msgType: Int, msgValue: Float) {
        Log.d(TAG, "onPlayerEvent() called with: msgType = [$msgType], msgValue = [$msgValue]")
        runOnUiThread {
            when (msgType) {
                MSG_DECODER_INIT_ERROR -> {
                }
                MSG_DECODER_READY -> onDecoderReady()
                MSG_DECODER_DONE -> {
                }
                MSG_REQUEST_RENDER -> surfaceView.requestRender()
                MSG_DECODING_TIME -> {

                }
                else -> {
                }
            }
        }
    }

    private fun onDecoderReady() {
        val videoWidth = mMediaPlayer!!.getMediaParams(MEDIA_PARAM_VIDEO_WIDTH).toInt()
        val videoHeight = mMediaPlayer!!.getMediaParams(MEDIA_PARAM_VIDEO_HEIGHT).toInt()
        if (videoHeight * videoWidth != 0) surfaceView.setAspectRatio(videoWidth, videoHeight)
        Log.i("nell-videoWidth", "$videoWidth, videoHeight:$videoHeight")
        val duration = mMediaPlayer!!.getMediaParams(MEDIA_PARAM_VIDEO_DURATION).toInt()
    }


}