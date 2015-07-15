package com.pwyql.pointmakemoney.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.OnResetpwdResponseListener;
import com.pwyql.pointmakemoney.service.AppService.OnVerifyPhoneCodeResponseListener;
import com.pwyql.pointmakemoney.service.AppService.ResetLoginPwdParams;
import com.pwyql.pointmakemoney.service.AppService.VerifyPhoneCodeParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.PreferencesUtil;
import com.pwyql.pointmakemoney.util.StringUtils;

public class ResetLoginPasswordActivity extends BaseActivity {
    Button mBtnGetVerifyCode;
    EditText mEtPhonenumber, mEtVerifyCode, mEtPassword;
    TextView mTvTips, mTvSubmitResetpwd;

    boolean isVerified; // 当前号码是否已通过了验证

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_resetloginpwd);
	init();
    }

    private void init() {
	// TODO Auto-generated method stub

	String phone = getIntent().getStringExtra("phone"); // 号码

	TextView tvTopTitle = getViewById(R.id.tv_top_title);
	tvTopTitle.setText("重置登录密码");
	TextView tvLeftIcon = getViewById(R.id.tv_left_icon);
	tvLeftIcon.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	    }
	});
	TextView tvTopAction = getViewById(R.id.tv_top_action);
	tvTopAction.setVisibility(View.GONE);

	mTvTips = getViewById(R.id.tv_instrucation);

	mEtPhonenumber = getViewById(R.id.et_phonenumber);
	mEtVerifyCode = getViewById(R.id.et_verifycode);
	mEtPassword = getViewById(R.id.et_pwd);

	mEtPhonenumber.setText(phone); // 

	mBtnGetVerifyCode = getViewById(R.id.btn_get_verifycode);
	// 获取验证码
	mBtnGetVerifyCode.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		getVerifyCode();
	    }
	});

	CheckBox cbShowOrhide = getViewById(R.id.cb_showpassword);// 显示/隐藏 密码
	cbShowOrhide.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
		    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
		    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
	    }
	});

	mTvSubmitResetpwd = getViewById(R.id.tv_resetpwd_submit);
	mTvSubmitResetpwd.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		submitResetpwd();
	    }
	});

	// 初始化 smsSDK
	initSMSSDK();

    }

    String lastPhoneNumber, lastPwd, lastPhoneNumberToken;

    private void getVerifyCode() {
	isVerified = false;

	lastPhoneNumber = mEtPhonenumber.getText().toString().trim();
	if (lastPhoneNumber.length() != 11 || !StringUtils.isPhoneNumber(lastPhoneNumber)) { // 非(中国大陆)手机号码
	    showToast("手机号码格式不正确");
	    mEtPhonenumber.requestFocus();
	    return;
	}
	progressDialog = showProgressDialog("验证码获取中");
	SMSSDK.getVerificationCode("86", lastPhoneNumber);

	// 清空原输入并focus
	mEtVerifyCode.setText("");
	mEtVerifyCode.requestFocus();

	//设置 获取按钮不可用
	findViewById(R.id.btn_get_verifycode).setEnabled(false);
    }

    private int mRegetCodeDelay = 59; // 

    /**
     * 初始化 短信 sdk
     */
    private void initSMSSDK() {
	// TODO Auto-generated method stub
	final Handler countdownHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case 0:
		    if (mRegetCodeDelay-- > 1) {
			mBtnGetVerifyCode.setText(mRegetCodeDelay + "s重发");
			mTvTips.setText("我们已将验证码通过短信发送给您,请注意查收");
			sendEmptyMessageDelayed(0, 1000L * 1);
		    } else {
			mTvTips.setText("");
			mBtnGetVerifyCode.setText("获取验证码");
			findViewById(R.id.btn_get_verifycode).setEnabled(true);
		    }
		    break;
		}
		super.handleMessage(msg);
	    }
	};
	final Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {
		    //短信注册成功后，返回MainActivity,然后提示新好友
		    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功(交由应用服务器代理验证)
			//			showToast("验证成功");
			//			mTvTips.setText("");
			//			isVerified = true; // 已通过短信验证
			//			reg(); // 连接服务器进行注册
		    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
			if (progressDialog != null) {
			    progressDialog.dismiss();
			}
			showToast("验证码已通过短信发送,请注意查收");
			findViewById(R.id.btn_get_verifycode).setEnabled(false);// 验证码已通过短信发送,等待重发
			mEtVerifyCode.setText("");
			mEtVerifyCode.requestFocus();
			mRegetCodeDelay = 60;
			countdownHandler.sendEmptyMessageDelayed(0, 1000L * 1);
		    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
			//				Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
			//				countryTextView.setText(data.toString());

		    }
		} else {
		    if (progressDialog != null)
			progressDialog.dismiss();
		    ((Throwable) data).printStackTrace();
		    showToast("验证码错误");
		    isVerified = false;
		    mRegetCodeDelay = -1;
		    mTvTips.setText("");
		    findViewById(R.id.btn_get_verifycode).setEnabled(true);
		    //		int resId = getStringRes(MainActivity.this, "smssdk_network_error");
		    //		Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
		    //		if (resId > 0) {
		    //		    Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
		    //		}
		}

	    }
	};

	String appkey = "77b8007b3cea";
	String appSecret = "d0abe4052f4815ed6017653d3a360eda";
	SMSSDK.initSDK(this, appkey, appSecret);

	EventHandler eh = new EventHandler() {

	    @Override
	    public void afterEvent(int event, int result, Object data) {

		Message msg = new Message();
		msg.arg1 = event;
		msg.arg2 = result;
		msg.obj = data;
		handler.sendMessage(msg);
	    }

	};
	SMSSDK.registerEventHandler(eh);
    }

    private void submitResetpwd() {
	String phone = mEtPhonenumber.getText().toString().trim();
	if (phone.length() == 0) {
	    mEtPhonenumber.requestFocus();
	    showToast("请输入手机号码");
	    return;
	}
	if (phone.length() != 11 || !StringUtils.isPhoneNumber(phone)) {
	    mEtPhonenumber.requestFocus();
	    showToast("手机号码格式不正确");
	    return;
	}

	lastPhoneNumber = phone;

	String pwd = mEtPassword.getText().toString().trim();
	lastPwd = pwd;

	if (pwd.length() == 0) {
	    showToast("请输入密码");
	    mEtPassword.requestFocus();
	    return;
	}
	if (pwd.length() < 6 || pwd.length() > 20) {
	    showToast("密码长度必须为6~20");
	    mEtPassword.requestFocus();
	    return;
	}

	String code = mEtVerifyCode.getText().toString().trim();
	if (code.length() == 0) {
	    mEtVerifyCode.requestFocus();
	    showToast("请输入验证码");
	    return;
	}

	// 提交验证码
	if (!isVerified) { // 未通过验证就提交验证
	    final ProgressDialog progressDialog = showProgressDialog("提交验证...");
	    //SMSSDK.submitVerificationCode("86", phone, code); // 通过应用服务器代理请求验证
	    // 应用服务器验证
	    VerifyPhoneCodeParams postParams = new VerifyPhoneCodeParams();
	    postParams.appKey = AppConstants.MOB_APP_KEY;
	    postParams.phone = lastPhoneNumber;
	    postParams.zone = "86";
	    postParams.code = code;
	    AppServiceImpl.getInstance().verifyPhoneCode(postParams, new OnVerifyPhoneCodeResponseListener() {

		@Override
		public void onSuccess(String token) {
		    // TODO Auto-generated method stub
		    showToast("验证通过");
		    progressDialog.dismiss();

		    showToast("验证成功");
		    mTvTips.setText("");
		    isVerified = true; // 已通过短信验证
		    resetpwd(token); // 连接服务器进行注册

		    lastPhoneNumberToken = token;
		}

		@Override
		public void onFailure(String errorMsg, int status) {
		    // TODO Auto-generated method stub
		    showToast(errorMsg);
		    progressDialog.dismiss();
		}
	    });
	    return;
	}

	// 
	resetpwd(lastPhoneNumberToken);

	// 提交到服务器进行处理
    }

    private void resetpwd(String token) {
	final ProgressDialog progressDialog = showProgressDialog("修改...");
	ResetLoginPwdParams postParams = new ResetLoginPwdParams();
	postParams.phoneNumber = lastPhoneNumber;
	postParams.password = lastPwd;
	postParams.token = token;
	AppServiceImpl.getInstance().resetLoginPassword(postParams, new OnResetpwdResponseListener() {

	    @Override
	    public void onSuccess() {
		// TODO Auto-generated method stub
		showToast("修改登录密码成功! 重新登录");
		progressDialog.dismiss();
		PreferencesUtil.save(ResetLoginPasswordActivity.this, "phone", lastPhoneNumber);
		PreferencesUtil.save(ResetLoginPasswordActivity.this, "pwd", lastPwd);
		finish();
		startActivity(new Intent(ResetLoginPasswordActivity.this, LoginActivity.class));
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

}
