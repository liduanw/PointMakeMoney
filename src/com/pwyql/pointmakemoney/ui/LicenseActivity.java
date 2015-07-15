package com.pwyql.pointmakemoney.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.webview.MWebViewClient;

public class LicenseActivity extends BaseActivity {
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_license);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("用户协议");
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

	mWebView = getViewById(R.id.webview);

	// 设置WebView对JavaScript的支持
	mWebView.getSettings().setJavaScriptEnabled(true);

	// 先让 asyncHttp 加载所有数据, 再将内容交给webView渲染
	//	HttpUtil.get(AppConstants.URL_PROFILE, new AsyncHttpResponseHandler() {
	//
	//	    @Override
	//	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	//		// TODO Auto-generated method stub
	//		String res = new String(arg2);
	//		mWebView.loadDataWithBaseURL(null, res, "text/html", "", null);
	//	    }
	//
	//	    @Override
	//	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
	//		// TODO Auto-generated method stub
	//		mWebView.loadDataWithBaseURL(null, "加载出错,请重试", "text/html", "", null);
	//	    }
	//	});

	// 添加cookie 确保session能用
	CookieSyncManager.createInstance(this);
	CookieManager cookieManager = CookieManager.getInstance();
	String cookies = "PHPSESSID=" + MyApplication.PHPSESSID + "";
	cookieManager.setCookie(AppConstants.HOST, cookies);//cookies是在HttpClient中获得的cookie  
	CookieSyncManager.getInstance().sync();

	//

	//自定义WebView的背景颜色
	//mWebView.setBackgroundColor(Color.TRANSPARENT);//先设置背景色为transparent
	//mWebView.setBackgroundResource(R.drawable.icon);

	MWebViewClient mWebViewClient = new MWebViewClient(mWebView, getApplicationContext());
	mWebView.setWebViewClient(mWebViewClient);
	//	MWebChromeClient mWebChromeClient = new MWebChromeClient(getApplicationContext());
	//	mWebView.setWebChromeClient(mWebChromeClient);
	//添加JS调用Android(Java)的方法接口
	//	MJavascriptInterface mJavascriptInterface = new MJavascriptInterface(getApplicationContext());
	//	mWebView.addJavascriptInterface(mJavascriptInterface, "WebViewFunc");

	// 从assets目录下面的加载html
	//	mWebView.loadUrl("file:///android_asset/web.html");
	// 该方法需要设置cookie, 但是cookie由AsyncHttp存储着, 换成先由asyncHttp加载下所有的html内容再给webView
	mWebView.loadUrl(AppConstants.URL_SYSTEM_LICENSE);

    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

    }
}
