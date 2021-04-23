package com.jinxian.wenshi.media

import android.view.Surface

class FFMediaPlayer {
    companion object {
        init {
            System.loadLibrary("learn-ffmpeg")
        }
        external fun native_GetFFmpegVersion(): String
        external fun native_OnSurfaceCreated(renderType: Int)
        external fun native_OnSurfaceChanged(renderType: Int, width: Int, height: Int)
        external fun native_OnDrawFrame(renderType: Int)

        const val VIDEO_GL_RENDER = 0

        const val MSG_DECODER_INIT_ERROR = 0
        const val MSG_DECODER_READY = 1
        const val MSG_DECODER_DONE = 2
        const val MSG_REQUEST_RENDER = 3
        const val MSG_DECODING_TIME = 4

        const val MEDIA_PARAM_VIDEO_WIDTH = 0x0001
        const val MEDIA_PARAM_VIDEO_HEIGHT = 0x0002
        const val MEDIA_PARAM_VIDEO_DURATION = 0x0003

        const val VIDEO_RENDER_OPENGL = 0
    }

    private var mNativePlayerHandle: Long = 0
    private var mEventCallback: EventCallback? = null


    fun init(url: String, videoRenderType: Int, surface: Surface?) {
        mNativePlayerHandle = native_Init(url, videoRenderType, surface)
    }

    fun play() {
        native_Play(mNativePlayerHandle)
    }

    fun pause() {
        native_Pause(mNativePlayerHandle)
    }

    fun seekToPosition(position: Float) {
        native_SeekToPosition(mNativePlayerHandle, position)
    }

    fun stop() {
        native_Stop(mNativePlayerHandle)
    }

    fun unInit() {
        native_UnInit(mNativePlayerHandle)
    }

    fun addEventCallback(callback: EventCallback) {
        mEventCallback = callback
    }

    fun getMediaParams(paramType: Int): Long {
        return native_GetMediaParams(mNativePlayerHandle, paramType)
    }

    private fun playerEventCallback(msgType: Int, msgValue: Float) {
        mEventCallback?.onPlayerEvent(msgType, msgValue)
    }


    external fun stringFromJNI(): String

    private external fun native_Init(
        url: String,
        videoRenderType: Int,
        surface: Surface?
    ): Long

    private external fun native_Play(playerHandle: Long)

    private external fun native_SeekToPosition(
        playerHandle: Long,
        position: Float
    )

    private external fun native_Pause(playerHandle: Long)

    private external fun native_Stop(playerHandle: Long)

    private external fun native_UnInit(playerHandle: Long)

    private external fun native_GetMediaParams(
        playerHandle: Long,
        paramType: Int
    ): Long


    interface EventCallback {
        fun onPlayerEvent(msgType: Int, msgValue: Float)
    }

}