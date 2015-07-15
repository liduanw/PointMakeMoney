package com.pwyql.pointmakemoney.ad;

import org.json.JSONObject;

import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobads.AdView;

public class AdsManager {
    private static AdsManager instance;
    Activity mAct;

    private AdsManager(Activity act) {
	mAct = act;
	init();

    }

    private void init() {
	// TODO Auto-generated method stub
	initBaidu();
	//	initYoumi();
    }

    private void initBaidu() {
	// TODO Auto-generated method stub
	com.baidu.mobads.AdView.setAppSid(this.mAct, "ef8bb5a9");
	com.baidu.mobads.AdView.setAppSec(this.mAct, "ef8bb5a9");
    }

    public static AdsManager getInstance(Activity act) {
	if (instance == null) {
	    instance = new AdsManager(act);
	}
	return instance;
    }

    public com.baidu.mobads.AdView createAdViewBaidu(LinearLayout layout) {
	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	return createAdViewBaidu(layout, params);
    }

    public com.baidu.mobads.AdView createAdViewBaidu(ViewGroup v, ViewGroup.LayoutParams params) {
	// TODO Auto-generated method stub
	final com.baidu.mobads.AdView adView = new com.baidu.mobads.AdView(this.mAct);
	//	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	v.addView(adView, params);

	// 间隔一定时间才能再次显示广告
	//	String lastTimeStr = PreferencesUtil.read(mAct, "adbaidu_last_click_time");
	//	if (lastTimeStr != null) {
	//	    long lastTime = Long.parseLong(lastTimeStr);
	//	    long maxDelay = 1000L * 60 * 30;
	//	    if (System.currentTimeMillis() - lastTime > maxDelay) {
	//		adView.setVisibility(View.GONE);
	//		return null;
	//	    }
	//	}
	// 默认广告不可见
	//adView.setVisibility(View.GONE);
	return adView;
    }

    public View createAdViewYoumi(LinearLayout layout) {
	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
	// 设置广告条的悬浮位置
	params.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角

	return createAdViewYoumi(layout, params);
    }

    public View createAdViewYoumi(ViewGroup v, ViewGroup.LayoutParams params) {
	// 广告条接口调用（适用于应用）
	// 将广告条adView添加到需要展示的layout控件中
	// LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
	// AdView adView = new AdView(this, AdSize.FIT_SCREEN);
	// adLayout.addView(adView);

	// 广告条接口调用（适用于游戏）

	// 实例化LayoutParams(重要)
	//	FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
	//	// 设置广告条的悬浮位置
	//	layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
	// 实例化广告条
	final net.youmi.android.banner.AdView adView = new net.youmi.android.banner.AdView(this.mAct, net.youmi.android.banner.AdSize.FIT_SCREEN);

	// 点击广告
	adView.setOnTouchListener(new View.OnTouchListener() {

	    @Override
	    public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(mAct, "测试...点击有米广告", Toast.LENGTH_SHORT).show();
		return false;
	    }
	});
	adView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		adView.setVisibility(View.GONE);
		Toast.makeText(mAct, "测试...点击广告条获得积分奖励", Toast.LENGTH_SHORT).show();
	    }
	});

	//adView.setVisibility(View.GONE);
	// 监听广告条接口
	adView.setAdListener(new net.youmi.android.banner.AdViewListener() {

	    @Override
	    public void onFailedToReceivedAd(net.youmi.android.banner.AdView arg0) {
		// TODO Auto-generated method stub
		adView.setVisibility(View.GONE);
	    }

	    @Override
	    public void onReceivedAd(net.youmi.android.banner.AdView arg0) {
		// TODO Auto-generated method stub
		adView.setVisibility(View.VISIBLE);
	    }

	    @Override
	    public void onSwitchedAd(net.youmi.android.banner.AdView arg0) {
		// TODO Auto-generated method stub

	    }
	});

	v.addView(adView, params);
	//	Toast.makeText(mAct, "测试...有米广告" + adView.getParent(), Toast.LENGTH_SHORT).show();
	//	System.out.println("youmi:" +adView.getParent());
	return adView;
    }

    public static class BaiduAdAdapter implements com.baidu.mobads.AdViewListener {

	@Override
	public void onAdClick(JSONObject arg0) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onAdFailed(String arg0) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onAdReady(AdView arg0) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onAdShow(JSONObject arg0) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onAdSwitch() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onVideoClickAd() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onVideoClickClose() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onVideoClickReplay() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onVideoError() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onVideoFinish() {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onVideoStart() {
	    // TODO Auto-generated method stub

	}

    }

}
