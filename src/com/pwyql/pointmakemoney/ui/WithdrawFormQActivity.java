package com.pwyql.pointmakemoney.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.Info;
import com.pwyql.pointmakemoney.service.AppService.LoadInfoParams;
import com.pwyql.pointmakemoney.service.AppService.OnLoadInfoResponseListener;
import com.pwyql.pointmakemoney.service.AppService.OnWithdrawResponseListener;
import com.pwyql.pointmakemoney.service.AppService.WithdrawOrder;
import com.pwyql.pointmakemoney.service.AppService.WithdrawParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.StringUtils;

/**
 * Q币充值
 * @author sparklee
 *
 */
public class WithdrawFormQActivity extends BaseActivity implements OnClickListener {
    EditText mEtAccountNumber, mEtAccountNumberAgain, mEtCashPwd;

    Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_form_q);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = getViewById(R.id.tv_top_title);
	tvTopTitle.setText("提现 - 充值Q币");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	mEtAccountNumber = getViewById(R.id.et_ac_num);
	mEtAccountNumberAgain = getViewById(R.id.et_ac_num_again);
	mEtCashPwd = getViewById(R.id.et_cashpwd);

	TextView tvInfo = getViewById(R.id.tv_info);

	String title = getIntent().getStringExtra("title");

	final int priceType = getIntent().getIntExtra("price_type", 0);
	int[] priceTypes = { 10, 20, 30, 50, 100 };
	int price = priceTypes[priceType];

	int cost = price * (int) MyApplication.exchange_rate;

	tvInfo.setText("名称:" + title + "   价格: ¥" + price + "\n花费积分数:" + cost);

	mBtnSubmit = getViewById(R.id.btn_submit);
	mBtnSubmit.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		submit(priceType); //
	    }
	});
    }

    private void submit(int priceType) {
	String accountNumber = mEtAccountNumber.getText().toString().trim();
	if (0 == accountNumber.length()) {
	    mEtAccountNumber.requestFocus();
	    showToast("请输入帐号");
	    return;
	}
	String accountNumberAgain = mEtAccountNumberAgain.getText().toString().trim();
	if (0 == accountNumberAgain.length()) {
	    mEtAccountNumberAgain.requestFocus();
	    showToast("请输入确认帐号");
	    return;
	}
	if (!accountNumber.equals(accountNumberAgain)) {
	    showToast("两次帐号不一致");
	    return;
	}
	String cashPwd = mEtCashPwd.getText().toString();
	if (0 == cashPwd.length()) {
	    mEtCashPwd.requestFocus();
	    showToast("请输入提现密码");
	    return;
	}

	final ProgressDialog dialog = showProgressDialog("提交中...");

	WithdrawParams postParams = new WithdrawParams();
	postParams.type = 1;
	postParams.priceType = priceType;
	postParams.accountNumber = mEtAccountNumber.getText().toString().trim();
	postParams.cashPwd = cashPwd;
	postParams.sign = StringUtils.md5("" + 1 + priceType + AppConstants.SIGN_KEY);
	AppServiceImpl.getInstance().withdraw(postParams, new OnWithdrawResponseListener() {

	    @Override
	    public void onSuccess(WithdrawOrder order) {
		// TODO Auto-generated method stub
		showToast("订单提交成功! 订单ID:" + order.id);
		AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawFormQActivity.this);
		builder.setTitle("提现成功");
		builder.setMessage("订单进入审核中,请耐心等待处理!\n订单ID:" + order.id);
		builder.setPositiveButton("确定", null);
		builder.create().show();
		dialog.dismiss();
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);

		AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawFormQActivity.this);
		builder.setTitle("提现失败");
		builder.setMessage(errorMsg);
		builder.setPositiveButton("确定", null);
		builder.create().show();
		dialog.dismiss();
	    }
	});
    }

    @Override
    protected void onResume() {
	// TODO Auto-generated method stub
	loadInfos();
	super.onResume();
    }

    private void loadInfos() {
	// TODO Auto-generated method stub
	final ProgressDialog progressDialog = showProgressDialog("加载中...");
	progressDialog.setCancelable(false);
	progressDialog.setCanceledOnTouchOutside(false);

	LoadInfoParams postParams = new LoadInfoParams();
	postParams.sign = "";

	AppServiceImpl.getInstance().loadInfo(postParams, new OnLoadInfoResponseListener() {

	    @Override
	    public void onSuccess(Info info) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();

		TextView tvPoints = getViewById(R.id.tv_current_points);
		tvPoints.setText("当前积分:" + info.points);
		
		mEtAccountNumber.setText(info.qq);
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);
		progressDialog.dismiss();
	    }
	});
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

    @Override
    public void onClick(View arg0) {
	// TODO Auto-generated method stub

    }

}
