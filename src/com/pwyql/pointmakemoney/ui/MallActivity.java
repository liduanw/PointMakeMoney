package com.pwyql.pointmakemoney.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.webview.MWebViewClient;

/**
 * 兑换商城
 * @author sparklee
 *
 */
public class MallActivity extends BaseActivity implements OnClickListener {
    PullToRefreshWebView mWebView;
    MWebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_mall);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = getViewById(R.id.tv_top_title);
	tvTopTitle.setText("提现 - 兑换商城");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});

	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	mWebView = (PullToRefreshWebView) findViewById(R.id.webview);
	this.mWebView.getRefreshableView().getSettings().setJavaScriptEnabled(true);

	//	this.mWebView.getRefreshableView().loadUrl(msg.getLinkUrl());

	this.mWebView.setMode(Mode.BOTH);

	this.mWebView.setOnRefreshListener(new OnRefreshListener<WebView>() {

	    @Override
	    public void onRefresh(PullToRefreshBase<WebView> refreshView) {
		// TODO Auto-generated method stub
		mWebView.getRefreshableView().loadUrl(AppConstants.URL_MALL);
		mWebView.setState(State.RESET);
	    }
	});

	// 添加cookie 确保session能用
	CookieSyncManager.createInstance(this);
	CookieManager cookieManager = CookieManager.getInstance();
	String cookies = "PHPSESSID=" + MyApplication.PHPSESSID + "";
	cookieManager.setCookie(AppConstants.HOST, cookies);//cookies是在HttpClient中获得的cookie  
	CookieSyncManager.getInstance().sync();

	//

	//自定义WebView的背景颜色
	//	mWebView.setBackgroundColor(Color.TRANSPARENT);//先设置背景色为transparent 
	//mWebView.setBackgroundResource(R.drawable.icon);

	mWebViewClient = new MWebViewClient(mWebView.getRefreshableView(), this);
	mWebView.getRefreshableView().setWebViewClient(mWebViewClient);
	//	MWebChromeClient mWebChromeClient = new MWebChromeClient(getApplicationContext());
	//	mWebView.setWebChromeClient(mWebChromeClient);
	//添加JS调用Android(Java)的方法接口
	//	MJavascriptInterface mJavascriptInterface = new MJavascriptInterface(getApplicationContext());
	//	mWebView.addJavascriptInterface(mJavascriptInterface, "WebViewFunc");

	// 从assets目录下面的加载html
	//	mWebView.loadUrl("file:///android_asset/web.html");
	// 该方法需要设置cookie, 但是cookie由AsyncHttp存储着, 换成先由asyncHttp加载下所有的html内容再给webView
	mWebView.getRefreshableView().loadUrl(AppConstants.URL_MALL);
    }

    @Override
    public void onBackPressed() {
	// TODO Auto-generated method stub
	if (mWebView.getRefreshableView().canGoBack()) {
	    mWebView.getRefreshableView().goBack();
	    return;
	}
	super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

    @Override
    public void onClick(View arg0) {

    }

}
