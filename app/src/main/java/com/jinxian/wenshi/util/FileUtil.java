package com.jinxian.wenshi.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {
    public static boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            String[] children = directory.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(new File(directory, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return directory.delete();
    }

    public static File copy(Context context, String asset) throws IOException {
        InputStream source = context.getAssets().open(new File(asset).getPath());
        File destinationFile = new File(context.getCacheDir().getAbsolutePath() + "/cocos/1", asset);

        if (destinationFile.exists()) {
            return destinationFile;
        }

        destinationFile.getParentFile().mkdirs();
        OutputStream destination = new FileOutputStream(destinationFile);
        byte[] buffer = new byte[1024];
        int nread;

        while ((nread = source.read(buffer)) != -1) {
            if (nread == 0) {
                nread = source.read();
                if (nread < 0)
                    break;
                destination.write(nread);
                continue;
            }
            destination.write(buffer, 0, nread);
        }
        destination.close();

        return destinationFile;
    }

    public static void unzip(String zipFileString, String outPathString, ZipProgress iProgress) throws Exception {
        long zipLength = getZipSize(zipFileString);
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        long count = 0;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (szName != null && szName.contains("../")) {
                // 解压路径存在路径穿越问题，直接过滤
                continue;
            }
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inZip.read(buffer)) != -1) {
                    count += len;
                    int progress = (int) ((count * 100) / zipLength);
                    if (iProgress != null) {
                        iProgress.onProgress(progress);
                    }
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        if (iProgress != null) {
            iProgress.onDone();
        }
        inZip.close();
    }


    public interface ZipProgress {
        void onProgress(int progress);

        void onDone();
    }

    public static long getZipSize(String filePath) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            size = 0;
        }
        return size;
    }

    /**
     * 复制文件夹(使用缓冲字节流)
     *
     * @param sourcePath 源文件夹路径
     * @param targetPath 目标文件夹路径
     */
    public static void copyFolder(String sourcePath, String targetPath) throws Exception {
        //源文件夹路径
        File sourceFile = new File(sourcePath);
        //目标文件夹路径
        File targetFile = new File(targetPath);

        if (!sourceFile.exists()) {
            throw new Exception("文件夹不存在");
        }
        if (!sourceFile.isDirectory()) {
            throw new Exception("源文件夹不是目录");
        }
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        if (!targetFile.isDirectory()) {
            throw new Exception("目标文件夹不是目录");
        }

        File[] files = sourceFile.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            //文件要移动的路径
            String movePath = targetFile + File.separator + file.getName();
            if (file.isDirectory()) {
                //如果是目录则递归调用
                copyFolder(file.getAbsolutePath(), movePath);
            } else {
                //如果是文件则复制文件
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(movePath));

                byte[] b = new byte[1024];
                int temp = 0;
                while ((temp = in.read(b)) != -1) {
                    out.write(b, 0, temp);
                }
                out.close();
                in.close();
            }
        }
    }
}
