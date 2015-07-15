package com.pwyql.pointmakemoney.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter.Goods;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter.OnGoodsOnClickListener;
import com.pwyql.pointmakemoney.service.AppService.LoadGoodsParams;
import com.pwyql.pointmakemoney.service.AppService.OnLoadGoodsResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.GoodsDetailActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.ImageLoaderOptions;

public class MallFragment extends BaseFragment {
    //    PullToRefreshLayout refreshLayout;
    PullToRefreshGridView mGridView;
    GoodsAdapter mAdapter;

    android.support.v4.view.ViewPager mTopViewPager;
    TopPagerAdapter mTopPagerAdapter;

    List<ImageView> mViews = new ArrayList<ImageView>();

    List<Goods> mGoodsList = new ArrayList<Goods>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//	Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_mall, container, false);
	init(view);
	return view;
    }

    //    float downY;

    private void init(View v) {
	mGridView = (PullToRefreshGridView) v.findViewById(R.id.gridview);
	//			final View topView = v.findViewById(R.id.layout_pager_top);//(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_top_slide, null);
	//	final View topView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_top_slide, null);
	//	mGridView.addHeaderView(topView); // 必须在 setAdapter之前

	// 顶部推荐商品 幻灯
	//	mTopViewPager = (ViewPager) topView.findViewById(R.id.pager);
	// 加载顶部推荐商品
	//	loadTopGoods();
	//	RelativeLayout.LayoutParams params = (LayoutParams) mGridView.getLayoutParams();
	//	params.topMargin = 220;
	//	mGridView.setLayoutParams(params);
	// 滚动, 
	//	this.mGridView.setOnScrollListener(new OnScrollListener() {
	//
	//	    @Override
	//	    public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
	//		// TODO Auto-generated method stub
	//
	//	    }
	//
	//	    @Override
	//	    public void onScroll(PLA_AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	//		// TODO Auto-generated method stub
	//		if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
	//		    // 当前滚动到了最后一行
	//		    loadGoods();
	//		}
	//	    }
	//	});
	mAdapter = new GoodsAdapter(getActivity(), new ArrayList<Goods>());
	mGridView.setAdapter(mAdapter);

	mAdapter.setOnGoodsOnClickListener(new OnGoodsOnClickListener() {

	    @Override
	    public void onClick(Goods goods) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
		Bundle extras = new Bundle();
		extras.putSerializable("goods", goods);
		intent.putExtras(extras);
		startActivity(intent);
	    }
	});

	//		mGridView.setOnTouchListener(new OnTouchListener() {
	//	
	//		    @Override
	//		    public boolean onTouch(View arg0, MotionEvent arg1) {
	//			// TODO Auto-generated method stub
	//			switch (arg1.getAction()) {
	//			case MotionEvent.ACTION_DOWN:
	//			    downY = arg1.getRawY();
	//			    break;
	//			case MotionEvent.ACTION_MOVE:
	//			    float distance = Math.abs(downY - arg1.getRawY());
	//			    if (distance < 20) { // 滑动距离<20忽略
	//				return false;
	//			    }
	//			    // 向上滑动
	//			    if (downY - arg1.getRawY() > 0) {
	//				if (topView.getVisibility() == View.GONE) {
	//				    return false;
	//				}
	//				topView.setVisibility(View.GONE);
	//				topView.bringToFront();
	//				TranslateAnimation animation = new TranslateAnimation(0, 0, 120, 0);
	//				animation.setDuration(200);
	//				topView.startAnimation(animation);
	//	
	//			    } else if (downY - arg1.getRawY() < 0) { // 向下滑动
	//				if (topView.getVisibility() == View.VISIBLE) {
	//				    return false;
	//				}
	//				topView.setVisibility(View.VISIBLE);
	//				topView.bringToFront();
	//				TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 120);
	//				animation.setDuration(200);
	//				//			animation.setFillAfter(true);
	//				topView.startAnimation(animation);
	//	
	//			    }
	//			    break;
	//			}
	//			return false;
	//		    }
	//		});

	// 带下拉刷新上拉加载的 layout
	//	refreshLayout = (PullToRefreshLayout) v.findViewById(R.id.pulltorefreshlayout);
	//	refreshLayout.setOnRefreshListener(new OnRefreshListener() {
	//
	//	    @Override
	//	    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
	//		// TODO Auto-generated method stub
	//		page = 1;
	//		loadGoods();
	//	    }
	//
	//	    @Override
	//	    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
	//		// TODO Auto-generated method stub
	//		loadGoods();
	//	    }
	//	});
	mGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

	    @Override
	    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		page = 1;
		loadGoods();
	    }

	    @Override
	    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		// TODO Auto-generated method stub
		loadGoods();
	    }
	});
	//	mGridView.setOnRefreshListener(new OnRefreshListener() {
	//
	//	    @Override
	//	    public void onRefresh() {
	//		// TODO Auto-generated method stub
	//		page = 1;
	//		loadGoods();
	//	    }
	//	});

	// 加载商品
	loadGoods();
    }

    /**
     * 加载顶部推荐商品数据
     */
    private void loadTopGoods() {
	// TODO Auto-generated method stub

	mTopViewPager.bringToFront(); // 置于顶层

	mViews.add(new ImageView(getActivity()));
	mViews.get(0).setScaleType(ScaleType.CENTER_CROP);
	mViews.add(new ImageView(getActivity()));
	mViews.get(1).setScaleType(ScaleType.CENTER_CROP);
	mViews.add(new ImageView(getActivity()));
	mViews.get(2).setScaleType(ScaleType.CENTER_CROP);
	mTopPagerAdapter = new TopPagerAdapter(mViews);
	mTopViewPager.setAdapter(mTopPagerAdapter);
	mTopViewPager.setCurrentItem(0);

	// test
	Goods goods = new Goods("2", "闪迪（SanDisk）8GB Class4 移动MicroSDHC（TF）存储卡", "http://img10.360buyimg.com/da/jfs/t1513/205/214128616/148314/56179937/55652a9cNd82a8045.jpg", 155, 99);
	Goods goods2 = new Goods("3", "闪迪（SanDisk）8GB Class4 移动MicroSDHC（TF）存储卡", "http://img30.360buyimg.com/da/jfs/t1354/52/128150061/96740/91a29eb1/555d8bdeN198e5ffb.jpg", 155, 99);
	Goods goods3 = new Goods("4", "闪迪（SanDisk）8GB Class4 移动MicroSDHC（TF）存储卡", "http://i.mmcdn.cn/simba/img/TB19gDjHVXXXXXKXFXXSutbFXXX.jpg", 155, 99);
	mTopPagerAdapter.setOnPagerClickListener(new OnPagerClickListener() {

	    @Override
	    public void onClick(int position, View v) {
		// TODO Auto-generated method stub

	    }
	});

	List<ImageView> views = mTopPagerAdapter.getViews();
	com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(goods.getImgUrl(), views.get(0), ImageLoaderOptions.normalOptions());
	com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(goods2.getImgUrl(), views.get(1), ImageLoaderOptions.normalOptions());
	com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(goods3.getImgUrl(), views.get(2), ImageLoaderOptions.normalOptions());
	final Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case 0:
		    int currentItem = (mTopViewPager.getCurrentItem() + 1) % mViews.size();
		    mTopViewPager.setCurrentItem(currentItem, true);
		    sendEmptyMessageDelayed(0, 1000 * 2);
		    break;
		}
		super.handleMessage(msg);
	    }
	};
	handler.sendEmptyMessageDelayed(0, 1000 * 2);
    }

    private int page = 1;

    private boolean isRefreshing;//

    private void loadGoods() {
	if (!ActivityUtil.hasNetwork(getActivity())) {
	    showToast("请开启网络");
	    return;
	}

	if (isRefreshing) {
	    return;
	}

	isRefreshing = true;
	// TODO Auto-generated method stub
	LoadGoodsParams postParams = new LoadGoodsParams();
	postParams.page = page;
	postParams.size = 20;
	AppServiceImpl.getInstance().loadGoods(postParams, new OnLoadGoodsResponseListener() {

	    @Override
	    public void onSuccess(List<Goods> goodsList) {

		mGridView.onRefreshComplete();
		if (goodsList.size() != 0) {
		    if (page == 1) {
			mAdapter.clear();
		    }
		    //mGridView.scrollTo(0, 100 * 2);
		    mAdapter.addAll(goodsList);
		    //		    mAdapter.notifyDataSetInvalidated();
		    //	

		    page++;
		} else {
		    showToast(mAdapter.getList().size() != 0 ? "没有更多的了" : "暂无数据");
		}

		isRefreshing = false;
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);

		mGridView.onRefreshComplete();

		isRefreshing = false;
	    }
	});
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	// 加载数据
	loadGoods();
    }

    //    public boolean onBackkeyPressed() {
    //	if (mWebView.canGoBack()) {
    //	    mWebView.goBack();
    //	    return true;
    //	}
    //	return false;
    //    }

    @Override
    public void onDestroy() {
	super.onDestroy();
    }

    public class TopPagerAdapter extends PagerAdapter {
	List<ImageView> views;
	OnPagerClickListener onPagerClickListener;

	public TopPagerAdapter(List<ImageView> views) {
	    this.views = views;
	    initViews();
	}

	private void initViews() {
	    // TODO Auto-generated method stub
	    for (int i = 0; i < views.size(); ++i) {
		final ImageView v = views.get(i);
		final int pos = i;
		v.setOnClickListener(new View.OnClickListener() {

		    @Override
		    public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (onPagerClickListener != null) {
			    onPagerClickListener.onClick(pos, v);
			}
		    }
		});
	    }
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
	    initViews();
	    notifyDataSetChanged();
	}

	public List<ImageView> getViews() {
	    return this.views;
	}

	public OnPagerClickListener getOnPagerClickListener() {
	    return onPagerClickListener;
	}

	public void setOnPagerClickListener(OnPagerClickListener onPagerClickListener) {
	    this.onPagerClickListener = onPagerClickListener;
	}

    }

    public static interface OnPagerClickListener {
	void onClick(int position, View v);
    }

}
