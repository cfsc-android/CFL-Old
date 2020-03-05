package com.xiandao.android.utils;


import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 谭勇 on 2017/11/9.
 */
public class DownloadHelper {

    public static void download(String url, String localPath, OnDownloadListener listener) {
        DownloadAsyncTask task = new DownloadAsyncTask(url, localPath, listener);
        task.execute();
    }

    private static class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

        private String mFailInfo;

        private String mUrl;
        private String mFilePath;
        private OnDownloadListener mListener;

        DownloadAsyncTask(String mUrl, String mFilePath, OnDownloadListener mListener) {
            this.mUrl = mUrl;
            this.mFilePath = mFilePath;
            this.mListener = mListener;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String pdfUrl = mUrl;

            try {
                URL url = new URL(pdfUrl);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();
                int contentLength = urlConnection.getContentLength();

                File file = new File(mFilePath);

                //判断目标文件所在的目录是否存在
                if (!file.getParentFile().exists()) {
                    //如果目标文件所在的目录不存在，则创建父目录
                    if (!file.getParentFile().mkdirs()) {
                        return false;
                    }
                }

                if (file.exists()) {
                    boolean result = file.delete();
                    if (!result) {
                        mFailInfo = "存储路径下的同名文件删除失败！";
                        return false;
                    }
                }
                int downloadSize = 0;
                byte[] bytes = new byte[1024];
                int length;
                OutputStream out = new FileOutputStream(mFilePath);
                while ((length = in.read(bytes)) != -1) {
                    out.write(bytes, 0, length);
                    downloadSize += length;
                    publishProgress(downloadSize / contentLength * 100);
                }

                in.close();
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
                mFailInfo = e.getMessage();
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mListener != null) {
                mListener.onStart();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (mListener != null) {
                if (aBoolean) {
                    mListener.onSuccess(new File(mFilePath));
                } else {
                    mListener.onFail(new File(mFilePath), mFailInfo);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values.length > 0) {
                if (mListener != null) {
                    mListener.onProgress(values[0]);
                }
            }
        }
    }

    public interface OnDownloadListener {
        void onStart();

        void onSuccess(File file);

        void onFail(File file, String failInfo);

        void onProgress(int progress);
    }
}