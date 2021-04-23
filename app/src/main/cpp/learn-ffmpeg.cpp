#include <cstdio>
#include <cstring>
#include <FFMediaPlayer.h>
#include <render/video/VideoGLRender.h>
#include <render/audio/OpenSLRender.h>
#include "util/LogUtil.h"
#include "jni.h"


extern "C" {
#include <libavcodec/version.h>
#include <libavcodec/avcodec.h>
#include <libavformat/version.h>
#include <libavutil/version.h>
#include <libavfilter/version.h>
#include <libswresample/version.h>
#include <libswscale/version.h>
};


extern "C" JNIEXPORT jstring  JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_00024Companion_native_1GetFFmpegVersion(JNIEnv *env,
                                                                                    jobject thiz) {
    char strBuffer[1024 * 4] = {0};
    strcat(strBuffer, "libavcodec : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVCODEC_VERSION));
    strcat(strBuffer, "\nlibavformat : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFORMAT_VERSION));
    strcat(strBuffer, "\nlibavutil : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVUTIL_VERSION));
    strcat(strBuffer, "\nlibavfilter : ");
    strcat(strBuffer, AV_STRINGIFY(LIBAVFILTER_VERSION));
    strcat(strBuffer, "\nlibswresample : ");
    strcat(strBuffer, AV_STRINGIFY(LIBSWRESAMPLE_VERSION));
    strcat(strBuffer, "\nlibswscale : ");
    strcat(strBuffer, AV_STRINGIFY(LIBSWSCALE_VERSION));
    strcat(strBuffer, "\navcodec_configure : \n");
    strcat(strBuffer, avcodec_configuration());
    strcat(strBuffer, "\navcodec_license : ");
    strcat(strBuffer, avcodec_license());
    LOGCATE("GetFFmpegVersion\n%s", strBuffer);
    return env->NewStringUTF(strBuffer);
}


extern "C" JNIEXPORT jlong JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1Init(JNIEnv *env, jobject obj, jstring jurl, jint renderType,
                                                         jobject surface) {
    const char* url = env->GetStringUTFChars(jurl, nullptr);
    FFMediaPlayer *player = new FFMediaPlayer();
    player->Init(env, obj, const_cast<char *>(url), renderType, surface);
    env->ReleaseStringUTFChars(jurl, url);
    return reinterpret_cast<jlong>(player);

}

extern "C" JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1Play(JNIEnv *env, jobject thiz,
                                                         jlong player_handle) {
    if (player_handle != 0) {
        FFMediaPlayer *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->Play();
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1SeekToPosition(JNIEnv *env, jobject thiz,
                                                                   jlong player_handle,
                                                                   jfloat position) {
    if (player_handle != 0) {
        FFMediaPlayer *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->SeekToPosition(position);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1Pause(JNIEnv *env, jobject thiz,
                                                          jlong player_handle) {
    if (player_handle != 0) {
        FFMediaPlayer *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->Pause();
    }
}


extern "C" JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1Stop(JNIEnv *env, jobject thiz,
                                                         jlong player_handle) {
    if (player_handle != 0) {
        FFMediaPlayer *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->Stop();
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1UnInit(JNIEnv *env, jobject thiz,
                                                           jlong player_handle) {
    if (player_handle != 0) {
        FFMediaPlayer *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->UnInit();
    }
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_native_1GetMediaParams(JNIEnv *env, jobject thiz,
                                                                   jlong player_handle,
                                                                   jint param_type) {
    long value = 0;
    if (player_handle != 0) {
        FFMediaPlayer *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        value = ffMediaPlayer->GetMediaParams(param_type);
    }
    return value;
}extern "C"
JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_00024Companion_native_1OnSurfaceCreated(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jint render_type) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->OnSurfaceCreated();
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->OnSurfaceCreated();
            break;
        default:
            break;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_00024Companion_native_1OnSurfaceChanged(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jint render_type,
                                                                                    jint width,
                                                                                    jint height) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->OnSurfaceChanged(width, height);
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->OnSurfaceChanged(width, height);
            break;
        default:
            break;
    }}extern "C"
JNIEXPORT void JNICALL
Java_com_jinxian_wenshi_media_FFMediaPlayer_00024Companion_native_1OnDrawFrame(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jint render_type) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->OnDrawFrame();
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->OnDrawFrame();
            break;
        default:
            break;
    }}