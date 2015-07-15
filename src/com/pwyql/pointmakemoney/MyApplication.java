package com.pwyql.pointmakemoney;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.pwyql.pointmakemoney.entity.User;
import com.pwyql.pointmakemoney.service.AppService.OnUploadCrashLogsResponseListener;
import com.pwyql.pointmakemoney.service.AppService.UploadCrashLogsParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.SplashActivity;
import com.pwyql.pointmakemoney.util.ActivityManagerUtils;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.HttpUtil;
import com.pwyql.pointmakemoney.util.PreferencesUtil;

public class MyApplication extends Application {
    private static MyApplication instance;

    private User currentLoginedUser;

    public static float exchange_rate;// 兑换率

    @Override
    public void onCreate() {
	super.onCreate();

	instance = this;

	// 创建快捷方式
	if (!"1".equals(PreferencesUtil.read(this, "shortcut_added"))) {
	    ActivityUtil.addShortCut(this, SplashActivity.class);
	    PreferencesUtil.save(this, "shortcut_added", "1");
	}

	init();

	// 捕获程序异常
	Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

	    @Override
	    public void uncaughtException(Thread arg0, final Throwable arg1) {
		// TODO Auto-generated method stub

		new Thread() {
		    public void run() {

			final File f = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/pointmm");
			if (!f.exists()) {
			    f.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			final String t = sdf.format(new Date());
			final File crashFile = new File(f.getAbsolutePath() + "/crash_" + t + ".log");

			Log.e("crash:", f.getAbsolutePath());

			try {
			    PrintWriter writer = new PrintWriter(crashFile);
			    writer.write("model:" + ActivityUtil.getModel() + "\nversionCode:" + ActivityUtil.getVersionCode(getApplicationContext()) + "\nVersionName:" + ActivityUtil.getVersionName(getApplicationContext()) + "\n");
			    arg1.printStackTrace(writer);
			    writer.close();

			} catch (FileNotFoundException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

			Looper.prepare();
			AlertDialog.Builder builder = new AlertDialog.Builder(getTopActivity());
			builder.setTitle("很抱歉,程序出现了异常!");
			builder.setMessage("异常信息已保存到文件" + crashFile.getAbsolutePath() + "\n请将此异常信息提交给我们,以助后续版本修复该问题");
			//			builder.setMessage(arg1.getMessage());
			builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//android.os.Process.killProcess(android.os.Process.myPid());
				if (!ActivityUtil.hasNetwork(getApplicationContext())) {
				    Toast.makeText(getTopActivity(), "请开启网络", Toast.LENGTH_LONG).show();
				    return;
				}

				final ProgressDialog progressDialog = new ProgressDialog(getTopActivity());
				progressDialog.setMessage("提交中...");
				progressDialog.show();
				UploadCrashLogsParams postParams = new UploadCrashLogsParams();
				postParams.file = crashFile;
				AppServiceImpl.getInstance().uploadCrashLogs(postParams, new OnUploadCrashLogsResponseListener() {

				    @Override
				    public void onSuccess() {
					// TODO Auto-generated method stub
					progressDialog.dismiss();
					Toast.makeText(getTopActivity(), "提交成功!", Toast.LENGTH_LONG).show();
					// 重启
					//					restartApp();
					toHome();
					//					System.exit(0);
					ActivityManagerUtils.getInstance().removeAllActivity();
					android.os.Process.killProcess(android.os.Process.myPid());
				    }

				    @Override
				    public void onFailure(String errorMsg, int status) {
					// TODO Auto-generated method stub
					progressDialog.dismiss();
					// 进入home
					toHome();
					Toast.makeText(getTopActivity(), errorMsg, Toast.LENGTH_LONG).show();
					//					System.exit(0);
					ActivityManagerUtils.getInstance().removeAllActivity();
					android.os.Process.killProcess(android.os.Process.myPid());
				    }
				});

			    }
			});
			builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				// 转到home
				toHome();
				ActivityManagerUtils.getInstance().removeAllActivity();
				//				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			    }
			});
			builder.create().show();
			//			Toast.makeText(getTopActivity(), crashFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
			Looper.loop();

		    };
		}.start();

		//		android.os.Process.killProcess(android.os.Process.myPid());
	    }
	});

    }

    private void init() {
	// TODO Auto-generated method stub
	// AsyncHttpClient 使用 cookie ,存储到 应用 Perference 目录下的xml文件中
	// PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
	HttpUtil.setCookieStore(this);
    }

    /**
     * 进入home桌面
     */
    void toHome() {
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory(Intent.CATEGORY_HOME);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
    }

    /**
     * 重启APP
     */
    void restartApp() {
	// 重启
	Intent intent = new Intent(this, SplashActivity.class);
	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	startActivity(intent);
    }

    public Activity getTopActivity() {
	return ActivityManagerUtils.getInstance().getTopActivity();
    }

    public static MyApplication getInstance() {
	return instance;
    }

    public void addActivity(Activity baseActivity) {
	// TODO Auto-generated method stub
	ActivityManagerUtils.getInstance().addActivity(baseActivity);
    }

    public User getCurrentLoginedUser() {
	return currentLoginedUser;
    }

    public void setCurrentLoginedUser(User user) {
	this.currentLoginedUser = user;
    }

    // 注销登录
    public void logout() {
	// TODO Auto-generated method stub

	// 注销

	// dosometing
	ActivityManagerUtils.getInstance().removeAllActivity();

    }

    // 方便webView使用 cookie, session
    public static String PHPSESSID;

}
