package com.pwyql.pointmakemoney.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;

public class RankingPointsFragment extends BaseFragment {
    private static final String TAG = "TestFragment";

    SwipeRefreshLayout mSwipeRefreshLayout;
    WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Log.d(TAG, "TestFragment-----onCreate");
	// Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	Log.d(TAG, "TestFragment-----onCreateView");
	View view = inflater.inflate(R.layout.fragment_rankingpoints, container, false);
	init(view);
	return view;
    }

    private void init(View v) {

	mWebView = (WebView) v.findViewById(R.id.webview);

	this.mWebView.getSettings().setJavaScriptEnabled(true);

	// 添加cookie 确保session能用
	CookieSyncManager.createInstance(this.getActivity());
	CookieManager cookieManager = CookieManager.getInstance();
	String cookies = "PHPSESSID=" + MyApplication.PHPSESSID + "";
	cookieManager.setCookie(AppConstants.HOST, cookies);//cookies是在HttpClient中获得的cookie  
	CookieSyncManager.getInstance().sync();

	// 
	mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
	mSwipeRefreshLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {

	    @Override
	    public void onRefresh() {
		// TODO Auto-generated method stub
		mWebView.loadUrl(AppConstants.URL_RANKINGUSERS);
	    }
	});
	// 设置进度条颜色
	mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

	mWebView.setWebViewClient(new WebViewClient() {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) { //在内部继续处理链接
		// TODO Auto-generated method stub
		view.loadUrl(url);
		return super.shouldOverrideUrlLoading(view, url);
	    }

	    @Override
	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		// TODO Auto-generated method stub
		String data = "<div style='color:red'>页面加载时出错了,请确保网络连接畅通<br>错误码:" + errorCode + "<br>" + description + "</div>";
		view.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		//	        super.onReceivedError(view, errorCode, description, failingUrl);
	    }
	});
	mWebView.setWebChromeClient(new WebChromeClient() {
	    @Override
	    public void onProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		if (newProgress == 100) {
		    mSwipeRefreshLayout.setRefreshing(false);// 刷新完成
		} else {
		    if (!mSwipeRefreshLayout.isRefreshing()) {
			mSwipeRefreshLayout.setRefreshing(true);
		    }
		}
		super.onProgressChanged(view, newProgress);
	    }
	});

	//自定义WebView的背景颜色
	//	mWebView.setBackgroundColor(Color.TRANSPARENT);//先设置背景色为transparent 
	//mWebView.setBackgroundResource(R.drawable.icon);

	//	MWebChromeClient mWebChromeClient = new MWebChromeClient(getApplicationContext());
	//	mWebView.setWebChromeClient(mWebChromeClient);
	//添加JS调用Android(Java)的方法接口
	//	MJavascriptInterface mJavascriptInterface = new MJavascriptInterface(getApplicationContext());
	//	mWebView.addJavascriptInterface(mJavascriptInterface, "WebViewFunc");

	// 从assets目录下面的加载html
	//	mWebView.loadUrl("file:///android_asset/web.html");
	// 该方法需要设置cookie, 但是cookie由AsyncHttp存储着, 换成先由asyncHttp加载下所有的html内容再给webView
	mWebView.loadUrl(AppConstants.URL_RANKINGUSERS);
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

}
