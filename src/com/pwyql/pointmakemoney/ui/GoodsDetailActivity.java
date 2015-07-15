package com.pwyql.pointmakemoney.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter.Goods;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.GoodsDetail;
import com.pwyql.pointmakemoney.service.AppService.LoadGoodsDetailParams;
import com.pwyql.pointmakemoney.service.AppService.OnLoadGoodsDetailResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.ImageLoaderOptions;

public class GoodsDetailActivity extends BaseActivity {
    ViewPager mTopViewPager;
    TopPagerAdapter mTopPagerAdapter;
    List<ImageView> mViews = new ArrayList<ImageView>();

    TextView mTvTitle, mTvPrice, mTvQuantity;

    WebView mWebViewDescription;
    Goods goods;

    ProgressBar mPbLoading;
    TextView mTvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_goodsdetail);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("商品详情");
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

	findViewById(R.id.btn_enter_withdraw).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(GoodsDetailActivity.this, ExchangeFormActivity.class);
		intent.putExtra("gid", goods.getId());
		intent.putExtra("title", "商品-" + goods.getTitle());
		intent.putExtra("cost", goods.getPrice());

		startActivity(intent);
	    }
	});

	mTopViewPager = (ViewPager) findViewById(R.id.pager);
	mViews.add(new ImageView(this));
	mViews.get(0).setScaleType(ScaleType.CENTER_CROP);
	mViews.add(new ImageView(this));
	mViews.get(1).setScaleType(ScaleType.CENTER_CROP);
	mViews.add(new ImageView(this));
	mViews.get(2).setScaleType(ScaleType.CENTER_CROP);
	mTopPagerAdapter = new TopPagerAdapter(mViews);
	mTopViewPager.setAdapter(mTopPagerAdapter);
	mTopViewPager.setCurrentItem(0);

	mTvTitle = getViewById(R.id.tv_goods_title);
	mWebViewDescription = getViewById(R.id.webview_goods_description);
	mTvPrice = getViewById(R.id.tv_price);

	mTvQuantity = getViewById(R.id.tv_stockquantity); // 库存

	this.goods = (Goods) getIntent().getSerializableExtra("goods");

	mTvTitle.setText(goods.getTitle());
	mTvPrice.setText("积分 " + goods.getPrice());

	// 加载进度条
	mPbLoading = getViewById(R.id.pb_loading);
	mTvLoading = getViewById(R.id.tv_loading);
	//
	initWebView();

	// 默认显示商品列表图
	com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(goods.getImgUrl(), mTopPagerAdapter.getViews().get(0), ImageLoaderOptions.normalOptions());
	loadDetail();
    }

    private void initWebView() {
	// TODO Auto-generated method stub
	mWebViewDescription.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

	//	mSwipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.swipe_container);
	//	mSwipeRefreshLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
	//
	//	    @Override
	//	    public void onRefresh() {
	//		// TODO Auto-generated method stub
	//		mWebViewDescription.loadUrl(AppConstants.URL_MALL_GOODSDETAIL_DESCRIPTION + "?gid=" + goods.getId());
	//	    }
	//	});
	//	// 设置进度条颜色
	//	mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
	mWebViewDescription.loadDataWithBaseURL(null, goods.getTitle(), "text/html", "UTF-8", null);//();
	mWebViewDescription.loadUrl(AppConstants.URL_MALL_GOODSDETAIL_DESCRIPTION + "?gid=" + goods.getId());

	//	mWebViewDescription.getSettings().setUseWideViewPort(true);
	//	mWebViewDescription.getSettings().setLoadWithOverviewMode(true);

	mWebViewDescription.setWebViewClient(new WebViewClient() {
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
	mWebViewDescription.setWebChromeClient(new WebChromeClient() {
	    @Override
	    public void onProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		if (newProgress == 100) { // 加载完成
		    mPbLoading.setVisibility(View.GONE);
		    mTvLoading.setVisibility(View.GONE);
		} else {
		    mTvLoading.setText(newProgress + "%" + "加载中...");
		}
		super.onProgressChanged(view, newProgress);
	    }
	});
    }

    private void loadDetail() {
	// TODO Auto-generated method stub

	// 加载标题,库存等
	LoadGoodsDetailParams postParams = new LoadGoodsDetailParams();
	postParams.goodsId = this.goods.getId();
	AppServiceImpl.getInstance().loadGoodsDetail(postParams, new OnLoadGoodsDetailResponseListener() {

	    @Override
	    public void onSuccess(GoodsDetail detail) {
		// TODO Auto-generated method stub
		List<ImageView> views = mTopPagerAdapter.getViews();
		List<String> imageUrlList = new ArrayList<String>();
		imageUrlList.add(detail.image01);
		imageUrlList.add(detail.image02);
		imageUrlList.add(detail.image03);

		for (int i = 0; i < views.size(); ++i) {
		    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(imageUrlList.get(i), views.get(i), ImageLoaderOptions.normalOptions());
		}

		mTvQuantity.setText("库存 " + detail.quantity);
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);
	    }
	});

	//	    mWebViewDescription.loadDataWithBaseURL(null, detail.description, "text/html", "UTF-8", null);//();
	//mTvDescription.setText(detail.description);
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

    }

    public class TopPagerAdapter extends PagerAdapter {
	List<ImageView> views;

	public TopPagerAdapter(List<ImageView> views) {
	    this.views = views;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	    // TODO Auto-generated method stub
	    View view = views.get(position);
	    container.addView(view);
	    return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    // TODO Auto-generated method stub
	    View view = views.get(position);
	    container.removeView(view);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
	    // TODO Auto-generated method stub
	    return arg0 == arg1;
	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return views.size();
	}

	public void setViews(List<ImageView> views) {
	    this.views = views;
	    notifyDataSetChanged();
	}

	public List<ImageView> getViews() {
	    return this.views;
	}

    }

}
