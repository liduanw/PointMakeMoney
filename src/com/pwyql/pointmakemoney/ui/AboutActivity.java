package com.pwyql.pointmakemoney.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;

public class AboutActivity extends BaseActivity {

    TextView mTvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_about);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("关于");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
	// 操作按钮不可见
	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	// app name
	TextView tvAppName = getViewById(R.id.tv_about_appname);
	tvAppName.setText(getResources().getString(R.string.app_name) + " v" + ActivityUtil.getVersionName(this));

	// 访问官网
	getViewById(R.id.tv_app_website).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(AppConstants.URL_APP_WEBSITE);
		intent.setData(content_url);
		startActivity(intent);
	    }
	});
	// 征婚交友网站重新开发
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

    }
}
