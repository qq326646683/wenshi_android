package com.jinxian.wenshi.service.preload;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class PreloadService {
    private ExecutorService mBackgroundExecutor;
    private ExecutorService mForegroundExecutor;
    final Handler UIHandler = new Handler(Looper.getMainLooper());
    private OkHttpClient mOKHttpClient = new OkHttpClient.Builder().build();


    private PreloadService() {
        mBackgroundExecutor = Executors.newSingleThreadExecutor();
    }

    private ExecutorService getBackgroundExecutor() {
        if (mBackgroundExecutor == null) {
            synchronized (this) {
                if (mBackgroundExecutor == null) {
                    mBackgroundExecutor = Executors.newSingleThreadExecutor();
                }
            }
        }
        return mBackgroundExecutor;
    }

    private ExecutorService getForegroundExecutor() {
        if (mForegroundExecutor == null) {
            synchronized (this) {
                if (mForegroundExecutor == null) {
                    int cpuCounts = Runtime.getRuntime().availableProcessors();
                    mForegroundExecutor = new ThreadPoolExecutor(Math.max(2, Math.min(cpuCounts - 1, 4)),
                            cpuCounts * 2 + 1,
                            0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>());
                }
            }
        }
        return mForegroundExecutor;
    }

    private static class Inner {
        private static final PreloadService sInstance = new PreloadService();
    }

    public static PreloadService getInstance() {
        return Inner.sInstance;
    }

    /**
     * 提交需立即执行的下载任务
     * 并行执行，用于前台展示的时机
     *
     * @param task 创建的预缓存task,
     */
    public void foregroundExecute(Runnable task) {
        getForegroundExecutor().execute(task);
    }

    /**
     * 在预缓存队列中提交一个预缓存Task
     * 预缓存线程串行执行，用于后台下载时机
     *
     * @param task 创建的预缓存任务
     */
    public void backgroundExecute(Runnable task) {
        getBackgroundExecutor().execute(task);
    }

    OkHttpClient getOKHttpClient() {
        return mOKHttpClient;
    }
}
