package com.jinxian.wenshi.service.preload;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class PreloadTask implements Runnable {
    private static final String TAG = PreloadTask.class.getSimpleName();
    private static Map<String, ReadWriteLock> sFileLocks = new ConcurrentHashMap<>();

    /**
     * @return 资源缓存文件夹名称
     */
    public abstract String getCacheDir();

    /**
     * @return 资源的网络url
     */
    public abstract String getUrl();

    /**
     * @return 写入文件系统的文件名，需要与url保持双射
     */
    public String getFileName() {
        return md5(getUrl());
    }

    /**
     * 加载预缓存资源成功
     * 在UI线程回调
     *
     * @param path 资源在本地文件系统的路径
     */
    public void onLoadSuccess(String path) {
        try {
            Log.i(TAG, String.format("%s load success in %s", getUrl(), path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean enableProgress() {
        return false;
    }

    /**
     * 加载中 进度
     *
     * @param tempSize  已加载的 长度
     * @param totalSize 总长度
     */
    public void onLoadProgress(long tempSize, long totalSize) {
        Log.i(TAG, String.format("%s load progress in %s, %s", getUrl(), tempSize, totalSize));
    }

    /**
     * 加载过程中错误
     * 在UI线程回调
     *
     * @param e Exception
     */
    public void onLoadError(Exception e) {
        e.printStackTrace();
        Log.e(TAG, e.toString());
    }

    private void dispatchLoadSuccess(final String path) {
        PreloadService.getInstance().UIHandler.post(new Runnable() {
            @Override
            public void run() {
                onLoadSuccess(path);
            }
        });
    }

    private void dispatchLoadError(final Exception e) {
        PreloadService.getInstance().UIHandler.post(new Runnable() {
            @Override
            public void run() {
                onLoadError(e);
            }
        });
    }

    private void dispatchLoadProgress(final long tempSize, final long totalSize) {
        if (!enableProgress()) return;

        PreloadService.getInstance().UIHandler.post(new Runnable() {
            @Override
            public void run() {
                onLoadProgress(tempSize, totalSize);
            }
        });
    }

    @Override
    public void run() {
        String url = getUrl();
        File cacheDir = new File(getCacheDir());
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdir()) {
                dispatchLoadError(new Exception("创建缓存文件夹失败"));
                return;
            }
        }

        String fileName = getFileName();
        File file = new File(cacheDir, fileName);
        if (file.exists()) {
            Log.d(TAG, url + " 已存在，跳过下载");
            dispatchLoadSuccess(file.getAbsolutePath());
            return;
        }
        ReadWriteLock readWriteLock = sFileLocks.get(file.getAbsolutePath());
        if (readWriteLock == null) {
            readWriteLock = new ReentrantReadWriteLock();
            sFileLocks.put(file.getAbsolutePath(), readWriteLock);
        }
        readWriteLock.writeLock().lock();
        File tempFile = new File(cacheDir, "_" + fileName);
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = PreloadService.getInstance().getOKHttpClient().newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null || response.code() != 200) {
                dispatchLoadError(new Exception(url + " 下载失败"));
                return;
            }

            long tempSize = 0L;
            long totalSize = responseBody.contentLength();

            inputStream = responseBody.byteStream();
            fileOutputStream = new FileOutputStream(tempFile);
            byte[] buf = new byte[1024 * 10];
            int length;
            while ((length = inputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, length);

                tempSize += length;
                dispatchLoadProgress(tempSize, totalSize);
            }
            tempFile.renameTo(file);
            dispatchLoadSuccess(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            dispatchLoadError(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            readWriteLock.writeLock().unlock();
            sFileLocks.remove(file.getAbsolutePath());
        }
    }

    private static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}