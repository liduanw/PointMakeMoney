package com.pwyql.pointmakemoney.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;

public class FirstGuideActivity extends BaseActivity {
    RelativeLayout mContentView;

    ViewPager mViewPager;
    FirstPagerAdapter mAdapter;

    int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	mContentView = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_firstguide, null);
	setContentView(mContentView);

	init();

    }

    private void init() {
	// TODO Auto-generated method stub
	List<View> views = new ArrayList<View>();

	mViewPager = (ViewPager) findViewById(R.id.viewpager);
	views.add(getLayoutInflater().inflate(R.layout.guide_page1, null));
	views.add(getLayoutInflater().inflate(R.layout.guide_page2, null));
	views.add(getLayoutInflater().inflate(R.layout.guide_page3, null));
	views.add(getLayoutInflater().inflate(R.layout.guide_page4, null));
	views.add(getLayoutInflater().inflate(R.layout.guide_page5, null));

	// 最后一个页面点击按钮开启主应用
	views.get(4).findViewById(R.id.tv_contacts).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
		// 进入注册页
		Intent intent = new Intent(FirstGuideActivity.this, RegisterActivity.class);
		startActivity(intent);
	    }
	});

	mAdapter = new FirstPagerAdapter(views);
	mViewPager.setAdapter(mAdapter);

	final ImageView[] dots = new ImageView[5];
	dots[0] = getViewById(R.id.iv_dot1);
	dots[1] = getViewById(R.id.iv_dot2);
	dots[2] = getViewById(R.id.iv_dot3);
	dots[3] = getViewById(R.id.iv_dot4);
	dots[4] = getViewById(R.id.iv_dot5);

	for (int i = 0, n = dots.length; i < n; ++i) {
	    dots[i].setEnabled(false);
	}

	mViewPager.setCurrentItem(0);
	dots[0].setEnabled(true);

	mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

	    @Override
	    public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		if (mCurrentIndex != arg0) {
		    dots[mCurrentIndex].setEnabled(false);
		    mCurrentIndex = arg0;
		    dots[mCurrentIndex].setEnabled(true);
		}

	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	    }
	});

    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

    }

    public class FirstPagerAdapter extends PagerAdapter {
	List<View> views;
	OnPagerClickListener onPagerClickListener;

	public FirstPagerAdapter(List<View> views) {
	    this.views = views;
	    initViews();
	}

	private void initViews() {
	    // TODO Auto-generated method stub
	    for (int i = 0; i < views.size(); ++i) {
		final View v = views.get(i);
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

	public void setViews(List<View> views) {
	    this.views = views;
	    initViews();
	    notifyDataSetChanged();
	}

	public List<View> getViews() {
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
