package com.pwyql.pointmakemoney.ui;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.ad.AdsManager;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService;
import com.pwyql.pointmakemoney.service.AppService.AdRewardParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.PreferencesUtil;
import com.pwyql.pointmakemoney.util.StringUtils;

public class AdsActivity extends BaseActivity {
    LinearLayout mContentView;
    TextView mTvNextRewardCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	mContentView = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_ads, null);
	setContentView(mContentView);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("快速积分通道");
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

	// 下次奖励倒计时
	mTvNextRewardCountdown = getViewById(R.id.tv_nextgetreward_countdown);

	// 读取领取奖励时间,倒计时显示
	loadCountDown();
	initAds();

    }

    private void loadCountDown() {
	// TODO Auto-generated method stub
	String nextRewardTimeStr = PreferencesUtil.read(this, MyApplication.getInstance().getCurrentLoginedUser().getUserId() + "_next_reward_time");
	if (null != nextRewardTimeStr) {

	    final int nextRewardTime = Integer.parseInt(nextRewardTimeStr);
	    int currentTime = (int) (System.currentTimeMillis() / 1000L);

	    final int countdown = nextRewardTime - currentTime;

	    //showToast(ActivityUtil.getDateStr(nextRewardTime, "yyyy-MM-dd HH:mm"));

	    if (countdown <= 0) { // 可以再次领取
		mTvNextRewardCountdown.setText(getString(R.string.click_to_reward));
	    } else {
		mTvNextRewardCountdown.setText("");
		final Handler handler = new Handler() {
		    @Override
		    public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
			    int currentTime = (int) (System.currentTimeMillis() / 1000L);
			    final int countdown = nextRewardTime - currentTime;
			    //				sendMessageDelayed(obtainMessage(0, 0, 0), delayMillis)
			    if (countdown <= 0) { // 可以再次领取
				mTvNextRewardCountdown.setText(getString(R.string.click_to_reward));
			    } else {
				sendEmptyMessageDelayed(0, 1000 * 1);
				mTvNextRewardCountdown.setText(countdown + "秒后可再次领取积分奖励");
			    }
			    break;
			}
			super.handleMessage(msg);
		    }
		};
		handler.sendEmptyMessageDelayed(0, 0);
		//		mTvNextRewardCountdown.setText("下次领取奖励时间:" + ActivityUtil.getDateStr(nextRewardTime, "yyyy-MM-dd HH:mm"));
	    }
	} else {
	    mTvNextRewardCountdown.setText(R.string.click_to_reward);
	}
    }

    private void initAds() {
	// 读取已记录的下次领取时间, 显示倒计时

	// TODO Auto-generated method stub
	final com.baidu.mobads.AdView adViewBaidu = AdsManager.getInstance(this).createAdViewBaidu(mContentView);
	// 点击广告条得积分
	adViewBaidu.setListener(new AdsManager.BaiduAdAdapter() {
	    @Override
	    public void onAdClick(JSONObject arg0) {
		// TODO Auto-generated method stub

		//adView.setVisibility(View.GONE);
		// 请求奖励积分
		final ProgressDialog progressDialog = new ProgressDialog(MyApplication.getInstance().getTopActivity());
		progressDialog.setMessage("点击广告条,等待系统积分奖励...");
		progressDialog.show();

		AdRewardParams postParams = new AdRewardParams();
		postParams.model = ActivityUtil.getModel();
		postParams.time = System.currentTimeMillis() + "";
		postParams.sign = StringUtils.md5(postParams.model + postParams.time + AppConstants.SIGN_KEY);
		AppServiceImpl.getInstance().adReward(postParams, new AppService.OnAdRewardResponseListener() {

		    @Override
		    public void onSuccess(int reward, int lastRewardTime, int nextRewardDelay) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage("恭喜,获得奖励积分" + reward);
			builder.setPositiveButton("确定", null);
			builder.create().show();

			progressDialog.dismiss();

			// 记录下获得奖励的时间
			PreferencesUtil.save(AdsActivity.this, MyApplication.getInstance().getCurrentLoginedUser().getUserId() + "_next_reward_time", (int) (System.currentTimeMillis() / 1000L) + nextRewardDelay + "");

			// 重新倒计时
			loadCountDown();
		    }

		    @Override
		    public void onFailure(String errorMsg, int status) {
			// TODO Auto-generated method stub
			Toast.makeText(AdsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage(errorMsg);
			builder.setPositiveButton("确定", null);
			builder.create().show();
			progressDialog.dismiss();
		    }
		});
	    }
	});

	final com.baidu.mobads.AdView adViewBaidu2 = AdsManager.getInstance(this).createAdViewBaidu(mContentView);
	// 点击广告条得积分
	adViewBaidu2.setListener(new AdsManager.BaiduAdAdapter() {
	    @Override
	    public void onAdClick(JSONObject arg0) {
		// TODO Auto-generated method stub

		//adView.setVisibility(View.GONE);
		// 请求奖励积分
		final ProgressDialog progressDialog = new ProgressDialog(MyApplication.getInstance().getTopActivity());
		progressDialog.setMessage("点击广告条,等待系统积分奖励...");
		progressDialog.show();

		AdRewardParams postParams = new AdRewardParams();
		postParams.model = ActivityUtil.getModel();
		postParams.time = System.currentTimeMillis() + "";
		postParams.sign = StringUtils.md5(postParams.model + postParams.time + AppConstants.SIGN_KEY);
		AppServiceImpl.getInstance().adReward(postParams, new AppService.OnAdRewardResponseListener() {

		    @Override
		    public void onSuccess(int reward, int lastRewardTime, int nextRewardDelay) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage("恭喜,获得奖励积分" + reward);
			builder.setPositiveButton("确定", null);
			builder.create().show();

			progressDialog.dismiss();

			// 记录下获得奖励的时间
			PreferencesUtil.save(AdsActivity.this, MyApplication.getInstance().getCurrentLoginedUser().getUserId() + "_next_reward_time", (int) (System.currentTimeMillis() / 1000L) + nextRewardDelay + "");

			// 重新倒计时
			loadCountDown();
		    }

		    @Override
		    public void onFailure(String errorMsg, int status) {
			// TODO Auto-generated method stub
			Toast.makeText(AdsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage(errorMsg);
			builder.setPositiveButton("确定", null);
			builder.create().show();
			progressDialog.dismiss();
		    }
		});
	    }
	});
	final com.baidu.mobads.AdView adViewBaidu3 = AdsManager.getInstance(this).createAdViewBaidu(mContentView);
	// 点击广告条得积分
	adViewBaidu3.setListener(new AdsManager.BaiduAdAdapter() {
	    @Override
	    public void onAdClick(JSONObject arg0) {
		// TODO Auto-generated method stub

		//adView.setVisibility(View.GONE);
		// 请求奖励积分
		final ProgressDialog progressDialog = new ProgressDialog(MyApplication.getInstance().getTopActivity());
		progressDialog.setMessage("点击广告条,等待系统积分奖励...");
		progressDialog.show();

		AdRewardParams postParams = new AdRewardParams();
		postParams.model = ActivityUtil.getModel();
		postParams.time = System.currentTimeMillis() + "";
		postParams.sign = StringUtils.md5(postParams.model + postParams.time + AppConstants.SIGN_KEY);
		AppServiceImpl.getInstance().adReward(postParams, new AppService.OnAdRewardResponseListener() {

		    @Override
		    public void onSuccess(int reward, int lastRewardTime, int nextRewardDelay) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage("恭喜,获得奖励积分" + reward);
			builder.setPositiveButton("确定", null);
			builder.create().show();

			progressDialog.dismiss();

			// 记录下获得奖励的时间
			PreferencesUtil.save(AdsActivity.this, MyApplication.getInstance().getCurrentLoginedUser().getUserId() + "_next_reward_time", (int) (System.currentTimeMillis() / 1000L) + nextRewardDelay + "");

			// 重新倒计时
			loadCountDown();
		    }

		    @Override
		    public void onFailure(String errorMsg, int status) {
			// TODO Auto-generated method stub
			Toast.makeText(AdsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage(errorMsg);
			builder.setPositiveButton("确定", null);
			builder.create().show();

			progressDialog.dismiss();
		    }
		});
	    }
	});

	//	View adViewBaidu3 = AdsManager.getInstance(this).createAdViewYoumi(mContentView);
	//	View adViewBaidu4 = AdsManager.getInstance(this).createAdViewYoumi(mContentView);
	//	View adViewBaidu5 = AdsManager.getInstance(this).createAdViewYoumi(mContentView);

	//	View adViewBaidu8 = AdsManager.getInstance(this).createAdViewYoumi(mContentView);
	//	View adViewBaidu9 = AdsManager.getInstance(this).createAdViewYoumi(mContentView);

    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

    }
}
