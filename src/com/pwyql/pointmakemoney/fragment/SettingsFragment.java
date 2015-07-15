package com.pwyql.pointmakemoney.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.androidservice.UpdateService;
import com.pwyql.pointmakemoney.service.AppService.CheckUpdateParams;
import com.pwyql.pointmakemoney.service.AppService.NewestAppInfo;
import com.pwyql.pointmakemoney.service.AppService.OnCheckUpdateResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.AboutActivity;
import com.pwyql.pointmakemoney.ui.LoginActivity;
import com.pwyql.pointmakemoney.ui.ShareActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.PreferencesUtil;

public class SettingsFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "TestFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Log.d(TAG, "TestFragment-----onCreate");
	//	Bundle args = getArguments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	Log.d(TAG, "TestFragment-----onCreateView");
	View view = inflater.inflate(R.layout.fragment_settings, container, false);
	init(view);
	return view;
    }

    private void init(View v) {

	v.findViewById(R.id.layout_share).setOnClickListener(this); // 分享
	v.findViewById(R.id.layout_logout).setOnClickListener(this); // 注销,进入登录
	v.findViewById(R.id.layout_checkupdate).setOnClickListener(this); // 检查版本更新
	v.findViewById(R.id.layout_help).setOnClickListener(this); // 帮助,FAQ
	v.findViewById(R.id.layout_settings).setOnClickListener(this); // 设置
	v.findViewById(R.id.layout_about).setOnClickListener(this); // 关于
	v.findViewById(R.id.layout_settings).setVisibility(View.GONE);

	v.findViewById(R.id.layout_system_notices).setOnClickListener(this); // 通知

	// 自动检查更新
	String checkupdateWhenStartUp = PreferencesUtil.read(getActivity(), "checkupdate_when_startup");
	if (null == checkupdateWhenStartUp || "1".equals(checkupdateWhenStartUp)) {
	    CheckUpdateParams postParams = new CheckUpdateParams();
	    postParams.currentVersionCode = ActivityUtil.getVersionCode(getActivity());

	    AppServiceImpl.getInstance().checkUpdate(postParams, new OnCheckUpdateResponseListener() {

		@Override
		public void onSuccess(NewestAppInfo app) {
		    // TODO Auto-generated method stub
		    showDownloadDialog(app);// 询问下载
		}

		@Override
		public void onFailure(String errorMsg, int status) {
		    // TODO Auto-generated method stub
		    if (status == -9) {
			showToast("connection timeout!");
		    }
		}
	    });
	}

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	Log.d(TAG, "TestFragment-----onDestroy");
    }

    @Override
    public void onClick(View arg0) {
	// TODO Auto-generated method stub
	switch (arg0.getId()) {
	case R.id.layout_share:
	    share();
	    break;
	case R.id.layout_checkupdate:
	    checkUpdate();
	    break;
	case R.id.layout_help:
	    toHelp();
	    break;
	case R.id.layout_settings:
	    toSettings();
	    break;
	case R.id.layout_about:
	    toAbout();
	    break;
	case R.id.layout_system_notices:
	    toSystemNotices();
	    break;
	case R.id.layout_logout:
	    logout();
	    break;
	}
    }

    private void share() {
	startActivity(new Intent(getActivity(), ShareActivity.class));
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
	if (!ActivityUtil.hasNetwork(getActivity())) {
	    showToast("请开启网络");
	    return;
	}
	final ProgressDialog progressDialog = new ProgressDialog(getActivity());
	progressDialog.setMessage("检查最新版本...");
	progressDialog.show();
	CheckUpdateParams postParams = new CheckUpdateParams();
	postParams.currentVersionCode = ActivityUtil.getVersionCode(getActivity());

	AppServiceImpl.getInstance().checkUpdate(postParams, new OnCheckUpdateResponseListener() {

	    @Override
	    public void onSuccess(NewestAppInfo app) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		showDownloadDialog(app);// 询问下载
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		if (status == -9) {
		    showToast("connection timeout!");
		} else if (status == 0) {
		    showToast("当前已是最新版本");
		}
		progressDialog.dismiss();
	    }
	});
    }

    /**
     * 询问下载更新
     * @param app
     */
    private void showDownloadDialog(final NewestAppInfo app) {
	// TODO Auto-generated method stub
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setTitle("有新版本需要更新");
	builder.setMessage("更新版本: " + app.versionName + "\n更新内容说明如下:\n" + app.instruction);
	builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		//开启更新服务UpdateService
		//这里为了把update更好模块化，可以传一些updateService依赖的值
		//如布局ID，资源ID，动态获取的标题,这里以app_name为例
		if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) {
		    showToast("SD卡已移出,无法下载!");
		    return;
		}

		Intent updateIntent = new Intent(getActivity(), UpdateService.class);
		Bundle extras = new Bundle();
		extras.putSerializable("appinfo", app);
		updateIntent.putExtras(extras);
		getActivity().startService(updateIntent);
	    }
	});
	builder.setNegativeButton("取消", null);
	builder.create().show();
    }

    /**
     * 帮助
     */
    private void toHelp() {
	startActivity(new Intent(this.getActivity(), com.pwyql.pointmakemoney.ui.HelpActivity.class));
    }

    /**
     * 设置
     */
    private void toSettings() {
	showToast("设置");
    }

    /**
     * 关于
     */
    private void toAbout() {
	startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    /**
     * 通知
     */
    private void toSystemNotices() {
	startActivity(new Intent(this.getActivity(), com.pwyql.pointmakemoney.ui.NoticesActivity.class));
    }

    // 注销
    private void logout() {
	getActivity().finish();
	// TODO Auto-generated method stub
	MyApplication.getInstance().logout();
	Intent intent = new Intent(getActivity(), LoginActivity.class);
	intent.putExtra("from", "logout");
	startActivity(intent);
    }

}
