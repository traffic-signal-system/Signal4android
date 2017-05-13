package com.aiton.administrator.shane_library.shane.upgrade;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aiton.administrator.shane_library.R;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.HTTPUtils;
import com.aiton.administrator.shane_library.shane.utils.VersionUtils;
import com.aiton.administrator.shane_library.shane.utils.VolleyListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class UpgradeUtils extends Activity {
    public static final String APK_UPGRADE = Environment
            .getExternalStorageDirectory() + "/bmcx/upgrade/upgrade.apk";
    private static Context mContext;
    private static NotificationManager mNotifiMgr;
    private static Notification mNotifi;
    private static RemoteViews mNotifiviews;
    private static Upgrade upgrade;
    private static int contentLength;
    /**
     * 使用此方法，json格式参考assets/upgrade.txt文件格式
     *
     * @param context
     * @param url
     */
    public static void checkUpgrade(Context context, String url) {
        mContext = context;
        HTTPUtils.get(context, url, new VolleyListener() {
            public void onResponse(String json) {
                checkUpgrade(json);
            }

            @Override
            public void onErrorResponse(VolleyError arg0) {
            }
        });
    }
    public static void checkUpgradeIsAble(Context context, String url) {
        mContext = context;
        HTTPUtils.get(context, url, new VolleyListener() {
            public void onResponse(String json) {
                checkUpgradeIsAble(json);
            }

            @Override
            public void onErrorResponse(VolleyError arg0) {
            }
        });
    }

    private static void checkUpgrade(String json) {
        upgrade = GsonUtils.parseJSON(json, Upgrade.class);
        int currVersion = VersionUtils.getCurrVersion(mContext);
        if (upgrade.version > currVersion) {
            new AlertDialog.Builder(mContext)
                    .setTitle("升级")
                    .setMessage(upgrade.feature)
                    .setPositiveButton("升级",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        //申请WRITE_EXTERNAL_STORAGE权限
                                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                0);
                                    } else {
                                        upgrade(upgrade);
                                    }
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();

        }
    }

    private static void checkUpgradeIsAble(String json) {
        upgrade = GsonUtils.parseJSON(json, Upgrade.class);
        int currVersion = VersionUtils.getCurrVersion(mContext);
        if (upgrade.version > currVersion) {
            new AlertDialog.Builder(mContext)
                    .setTitle("升级")
                    .setMessage(upgrade.feature)
                    .setPositiveButton("升级",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        //申请WRITE_EXTERNAL_STORAGE权限
                                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                0);
                                    } else {
                                        upgrade(upgrade);
                                        ProgressDialog progressDialog = new ProgressDialog(mContext);
                                        progressDialog.setMessage("正在下载……");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                    }
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();
        }
    }

    protected static void upgrade(Upgrade upgrade) {
        // 下载
        new UpgradeTask().execute(upgrade.apkurl);
    }

    static class UpgradeTask extends AsyncTask<String, Integer, File> {
        @Override
        protected void onPreExecute() {
            // 发送通知显示升级进度
            sendNotify();
        }

        @Override
        protected File doInBackground(String... params) {
            File apkFile = new File(APK_UPGRADE);
            String apkUrl = params[0];
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                // 设置连接超时时间
                conn.setConnectTimeout(25000);
                // 设置下载数据超时时间
                conn.setReadTimeout(25000);
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    // 服务端错误响应
                    return null;
                }

                is = conn.getInputStream();
                contentLength = conn.getContentLength();

                // 如果文件夹不存在则创建
                if (!apkFile.getParentFile().exists()) {
                    apkFile.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(apkFile);
                byte[] buffer = new byte[1024];
                int len = 0;
                int loadedLen = 0;// 当前已下载文件大小
                // 更新13次
//				int updateSize = upgrade.filelen / 13;
                int updateSize = contentLength / 10;
                int num = 0;
                while (-1 != (len = is.read(buffer))) {
                    loadedLen += len;
                    fos.write(buffer, 0, len);
                    if (loadedLen >= updateSize * num) {
                        num++;
                        publishProgress(loadedLen);
                    }
                }
                fos.flush();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // 处理超时异常，提示用户在网络良好情况下重试

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return apkFile;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 更新通知
            updateNotify(values[0]);
        }

        @Override
        protected void onPostExecute(File result) {
            Toast.makeText(mContext, "下载完成，请点击通知栏完成升级", Toast.LENGTH_LONG)
                    .show();
            openFile(result);
            finishNotify();
        }
    }

    private static void sendNotify() {
        Intent intent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        mNotifiviews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notify);
        mNotifiviews.setViewVisibility(R.id.tv_subtitle, View.VISIBLE);
        mNotifiviews.setViewVisibility(R.id.progressBar1, View.VISIBLE);

        mNotifi = new NotificationCompat.Builder(mContext)
                .setContent(mNotifiviews)
                .setAutoCancel(true)
                        // 单击后自动删除
                        // .setOngoing(true)// 无法删除的通知
                        // 定制通知布局
                .setSmallIcon(R.mipmap.ic_logo_noti).setTicker("正在下载")
                .setWhen(System.currentTimeMillis()).setSound(Uri.parse(""))
//				.setVibrate(new long[] { 0, 100, 300, 400 })//设置更新振动
                .setContentIntent(contentIntent).build();
        mNotifiMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifiMgr.notify(12345, mNotifi);
    }

    private static void updateNotify(int loadedLen) {
//		int progress = loadedLen * 100 / upgrade.filelen;
        int progress = (int) (((double) loadedLen / (double) contentLength) * 100);
        mNotifiviews.setTextViewText(R.id.tv_subtitle, progress + "%");
//		mNotifiviews.setProgressBar(R.id.progressBar1, upgrade.filelen,loadedLen, false);
        mNotifiviews.setProgressBar(R.id.progressBar1, contentLength, loadedLen, false);
        // mNotifiviews.setViewVisibility(R.id.tv_title, View.INVISIBLE);
        mNotifiMgr.notify(12345, mNotifi);
    }

    private static void finishNotify() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(APK_UPGRADE)),
                "application/vnd.android.package-archive");
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        mNotifi.contentIntent = contentIntent;
        mNotifiviews.setTextViewText(R.id.tv_title, "下载完成，请点击完成升级");
//		mNotifiviews.setViewVisibility(R.id.tv_subtitle, View.INVISIBLE);
//		mNotifiviews.setViewVisibility(R.id.progressBar1, View.INVISIBLE);
        mNotifiMgr.notify(12345, mNotifi);
    }

    /***
     * 下载完成后自动安装
     *
     * @param file
     */
    private static void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        mNotifiMgr.cancel(12345);
    }

}
