package com.pwyql.pointmakemoney.androidservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppService.NewestAppInfo;
import com.pwyql.pointmakemoney.ui.MainActivity;

public class UpdateService extends Service {

    private NewestAppInfo appInfo;

    NotificationManager updateNotificationManager;
    Notification updateNotification;

    Intent updateIntent;
    PendingIntent updatePendingIntent;

    File downloadedFile;

    //下载状态
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;

    @Override
    public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
	Object object = intent.getExtras().getSerializable("appinfo");
	if (object == null) {
	    return 0;
	}
	appInfo = (NewestAppInfo) object;
	new Thread(new UpdateRunnable()).start();

	this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	this.updateNotification = new Notification();
	//设置下载过程中，点击通知栏，回到主界面
//	updateIntent = new Intent(this, MainActivity.class);
	updateIntent = new Intent(this, MainActivity.class);
	updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
	//设置通知栏显示内容
	updateNotification.icon = R.drawable.icon;
	updateNotification.tickerText = "开始下载";
	updateNotification.setLatestEventInfo(this, "闷头赚", "0%", updatePendingIntent);
	//发出通知
	updateNotificationManager.notify(0, updateNotification);
	return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
    }

    public class UpdateRunnable implements Runnable {

	private Handler updateHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		switch (msg.what) {
		case DOWNLOAD_COMPLETE:
		    //点击安装PendingIntent
		    Uri uri = Uri.fromFile(downloadedFile);
		    Intent installIntent = new Intent(Intent.ACTION_VIEW);
		    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
		    updatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);
		    updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒 
		    updateNotification.setLatestEventInfo(UpdateService.this, getText(R.string.app_name), "下载完成,点击安装。", updatePendingIntent);
		    updateNotificationManager.notify(0, updateNotification);
		    //停止服务
		    stopService(updateIntent);
		    break;
		case DOWNLOAD_FAIL:
		    //下载失败
		    updateNotification.setLatestEventInfo(UpdateService.this, getText(R.string.app_name), "下载完成,点击安装。", updatePendingIntent);
		    updateNotificationManager.notify(0, updateNotification);
		    stopService(updateIntent);
		    break;
		default:
		    stopService(updateIntent);
		    break;
		}
	    }
	};

	public UpdateRunnable() {
	    // TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
	    // TODO Auto-generated method stub
	    try {
		HttpURLConnection conn = (HttpURLConnection) new URL(appInfo.url).openConnection();
		// 使用 RANGE 断点续传 待
		InputStream inputStream = conn.getInputStream();
		byte[] buffer = new byte[1024 << 1];
		int downloadCount = 0;
		int len = 0;

		downloadedFile = createDownFile();
		FileOutputStream outputStream = new FileOutputStream(downloadedFile);

		int current = 0;
		while (0 != (len = (inputStream.read(buffer, 0, buffer.length)))) {
		    outputStream.write(buffer, 0, len);
		    downloadCount += len;

		    //为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
		    if ((downloadCount == 0) || (int) (downloadCount * 100 / appInfo.size) - 1 > current) {
			current += 1;
			updateNotification.setLatestEventInfo(UpdateService.this, "正在下载", (int) downloadCount * 100 / appInfo.size + "%", updatePendingIntent);
			updateNotificationManager.notify(0, updateNotification);
		    }

		    if (downloadCount >= appInfo.size) {
			break;
		    }
		}
		outputStream.flush();
		outputStream.close();
		// 下载完成
		updateHandler.obtainMessage(DOWNLOAD_COMPLETE).sendToTarget();
		conn.disconnect();
	    } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		// 下载失败
		updateHandler.obtainMessage(DOWNLOAD_FAIL).sendToTarget();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		// 下载失败
		updateHandler.obtainMessage(DOWNLOAD_FAIL).sendToTarget();
	    } finally {
	    }
	}

	private File createDownFile() {
	    // TODO Auto-generated method stub
	    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.pointmm/pointmm.apk");
	    if (!f.getParentFile().exists()) {
		f.getParentFile().mkdirs();
	    }
	    return f;
	}
    }

}
