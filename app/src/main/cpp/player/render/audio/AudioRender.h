
#ifndef WENSHI_ANDROID_AUDIORENDER_H
#define WENSHI_ANDROID_AUDIORENDER_H

class AudioFrame {
public:
    AudioFrame(uint8_t * data, int dataSize, bool hardCopy = true) {
        this->dataSize = dataSize;
        this->data = data;
        this->hardCopy = hardCopy;
        if(hardCopy) {
            this->data = static_cast<uint8_t *>(malloc(this->dataSize));
            memcpy(this->data, data, dataSize);
        }
    }

    ~AudioFrame() {
        if(hardCopy && this->data)
            free(this->data);
        this->data = nullptr;
    }

    uint8_t * data = nullptr;
    int dataSize = 0;
    bool hardCopy = true;
};

class AudioRender {
public:
    virtual ~AudioRender()
    {}
    virtual void Init() = 0;
    virtual void ClearAudioCache() = 0;
    virtual void RenderAudioFrame(uint8_t *pData, int dataSize) = 0;
    virtual void UnInit() = 0;

};

#endif //WENSHI_ANDROID_AUDIORENDER_H
