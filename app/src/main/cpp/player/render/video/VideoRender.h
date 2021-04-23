
#ifndef WENSHI_ANDROID_VIDEORENDER_H
#define WENSHI_ANDROID_VIDEORENDER_H

#include "../../../util/ImageDef.h"

#define VIDEO_RENDER_OPENGL             0


class VideoRender {
public:
    VideoRender(int type){
        m_RenderType = type;
    }
    virtual ~VideoRender(){}
    virtual void Init(int videoWidth, int videoHeight, int *dstSize) = 0;
    virtual void RenderVideoFrame(NativeImage *pImage) = 0;
    virtual void UnInit() = 0;

    int GetRenderType() {
        return m_RenderType;
    }
private:
    int m_RenderType = VIDEO_RENDER_OPENGL;
};


#endif //WENSHI_ANDROID_VIDEORENDER_H
