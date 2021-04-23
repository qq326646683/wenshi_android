//
// Created by YPP on 2021/4/23.
//

#ifndef WENSHI_ANDROID_AUDIOGLRENDER_H
#define WENSHI_ANDROID_AUDIOGLRENDER_H

#include "thread"
#include "AudioRender.h"
#include <GLES3/gl3.h>
#include <detail/type_mat.hpp>
#include <detail/type_mat4x4.hpp>
#include <render/BaseGLRender.h>

using namespace glm;

#define MAX_AUDIO_LEVEL 5000
#define RESAMPLE_LEVEL  40


class AudioGLRender : public BaseGLRender {
public:
    static AudioGLRender* GetInstance();
    static void ReleaseInstance();

    virtual void OnSurfaceCreated();
    virtual void OnSurfaceChanged(int w, int h);
    virtual void OnDrawFrame();
    virtual void UpdateMVPMatrix(int angleX, int angleY, float scaleX, float scaleY){};
    virtual void SetTouchLoc(float touchX, float touchY) {}

    void UpdateAudioFrame(AudioFrame *audioFrame);

private:
    void Init();
    void UnInit();
    AudioGLRender(){
        Init();
    }
    ~AudioGLRender(){
        UnInit();
    }

    void UpdateMesh();

    static AudioGLRender *m_pInstance;
    static std::mutex m_Mutex;
    AudioFrame *m_pAudioBuffer = nullptr;

    GLuint m_ProgramObj = 0;
    GLuint m_VaoId;
    GLuint m_VboIds[2];
    glm::mat4 m_MVPMatrix;

    vec3 *m_pVerticesCoords = nullptr;
    vec2 *m_pTextureCoords = nullptr;

    int m_RenderDataSize;
};


#endif //WENSHI_ANDROID_AUDIOGLRENDER_H
