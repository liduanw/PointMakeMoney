package com.pwyql.pointmakemoney.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;

/**
 * 支付宝转账
 * @author sparklee
 *
 */
public class WithdrawMenuAlipayActivity extends BaseActivity implements OnClickListener {
    TextView mTvCost10, mTvCost20, mTvCost30, mTvCost50, mTvCost100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_alipay);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = getViewById(R.id.tv_top_title);
	tvTopTitle.setText("提现 - 支付宝转账");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	mTvCost10 = getViewById(R.id.tv_cost_10);
	mTvCost20 = getViewById(R.id.tv_cost_20);
	mTvCost30 = getViewById(R.id.tv_cost_30);
	mTvCost50 = getViewById(R.id.tv_cost_50);
	mTvCost100 = getViewById(R.id.tv_cost_100);

	int rate = (int) MyApplication.exchange_rate;
	mTvCost10.setText(10 * rate + "");
	mTvCost20.setText(20 * rate + "");
	mTvCost30.setText(30 * rate + "");
	mTvCost50.setText(50 * rate + "");
	mTvCost100.setText(100 * rate + "");

	findViewById(R.id.layout_pay_10).setOnClickListener(this);
	findViewById(R.id.layout_pay_20).setOnClickListener(this);
	findViewById(R.id.layout_pay_30).setOnClickListener(this);
	findViewById(R.id.layout_pay_50).setOnClickListener(this);
	findViewById(R.id.layout_pay_100).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

    @Override
    public void onClick(View arg0) {
	// TODO Auto-generated method stub
	Intent intent = new Intent(this, WithdrawFormAlipayActivity.class);

	if (arg0.getId() == R.id.layout_pay_10) {
	    intent.putExtra("title", "支付宝+10");
	    intent.putExtra("price_type", 0);
	    startActivity(intent);
	}
	if (arg0.getId() == R.id.layout_pay_20) {
	    intent.putExtra("title", "支付宝+20");
	    intent.putExtra("price_type", 1);
	    startActivity(intent);
	}
	if (arg0.getId() == R.id.layout_pay_30) {
	    intent.putExtra("title", "支付宝+30");
	    intent.putExtra("price_type", 2);
	    startActivity(intent);
	}
	if (arg0.getId() == R.id.layout_pay_50) {
	    intent.putExtra("title", "支付宝+50");
	    intent.putExtra("price_type", 3);
	    startActivity(intent);
	}
	if (arg0.getId() == R.id.layout_pay_100) {
	    intent.putExtra("title", "支付宝+100");
	    intent.putExtra("price_type", 4);
	    startActivity(intent);
	}
    }

}
