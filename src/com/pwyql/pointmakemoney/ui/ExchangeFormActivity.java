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

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.ExchangeParams;
import com.pwyql.pointmakemoney.service.AppService.Info;
import com.pwyql.pointmakemoney.service.AppService.LoadInfoParams;
import com.pwyql.pointmakemoney.service.AppService.OnExchangeResponseListener;
import com.pwyql.pointmakemoney.service.AppService.OnLoadInfoResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.StringUtils;

/**
 * 提现 - 兑换商品
 * @author sparklee
 *
 */
public class ExchangeFormActivity extends BaseActivity implements OnClickListener {

    EditText mEtReceAddr, mEtReceName, mEtRecePhone, mEtCashPwd;

    Button mBtnSubmit;

    String goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_form_goods);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = getViewById(R.id.tv_top_title);
	tvTopTitle.setText("提现 - 兑换商品");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	mEtRecePhone = getViewById(R.id.et_rece_phone);
	mEtReceAddr = getViewById(R.id.et_rece_addr);
	mEtReceName = getViewById(R.id.et_rece_name);

	mEtCashPwd = getViewById(R.id.et_cashpwd);

	TextView tvInfo = getViewById(R.id.tv_info);

	goodsId = getIntent().getStringExtra("gid");

	String title = getIntent().getStringExtra("title");

	int cost = getIntent().getIntExtra("cost", 0);

	tvInfo.setText("名称:" + title + "    花费积分数:" + cost);

	mBtnSubmit = getViewById(R.id.btn_submit);
	mBtnSubmit.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		submit(goodsId); //
	    }
	});
    }

    private void submit(String goodsId) {
	String accountNumber = mEtReceAddr.getText().toString().trim();
	if (0 == accountNumber.length()) {
	    mEtReceAddr.requestFocus();
	    showToast("请输入收货地址");
	    return;
	}
	String accountNumberAgain = mEtReceName.getText().toString().trim();
	if (0 == accountNumberAgain.length()) {
	    mEtReceName.requestFocus();
	    showToast("请输入收货人姓名");
	    return;
	}

	String recePhone = mEtRecePhone.getText().toString().trim();
	if (0 == recePhone.length()) {
	    mEtRecePhone.requestFocus();
	    showToast("请输入联系人手机号");
	    return;
	}

	String cashPwd = mEtCashPwd.getText().toString();
	if (0 == cashPwd.length()) {
	    mEtCashPwd.requestFocus();
	    showToast("请输入提现密码");
	    return;
	}

	final ProgressDialog dialog = showProgressDialog("提交中...");

	ExchangeParams postParams = new ExchangeParams();
	postParams.goodsId = goodsId;
	postParams.receAddr = postParams.receAddr;
	postParams.receName = postParams.receName;
	postParams.recePhone = postParams.recePhone;
	postParams.cashPwd = cashPwd;
	postParams.sign = StringUtils.md5("" + 2 + goodsId + AppConstants.SIGN_KEY);
	AppServiceImpl.getInstance().exchange(postParams, new OnExchangeResponseListener() {

	    @Override
	    public void onSuccess(String orderId) {
		// TODO Auto-generated method stub
		//		showToast("订单提交成功! 订单ID:" + order.id);
		AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeFormActivity.this);
		builder.setTitle("兑换成功");
		builder.setMessage("订单进入审核中,请耐心等待处理!\n订单ID:" + orderId);
		builder.setPositiveButton("确定", null);
		builder.create().show();
		dialog.dismiss();
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);

		AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeFormActivity.this);
		builder.setTitle("兑换失败");
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

		mEtReceAddr.setText(info.receAddr);
		mEtReceName.setText(info.receName);
		mEtRecePhone.setText(info.recePhone);
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
