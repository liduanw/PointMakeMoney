package com.pwyql.pointmakemoney.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.OnRegResponseListener;
import com.pwyql.pointmakemoney.service.AppService.OnVerifyPhoneCodeResponseListener;
import com.pwyql.pointmakemoney.service.AppService.RegParams;
import com.pwyql.pointmakemoney.service.AppService.VerifyPhoneCodeParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.PreferencesUtil;
import com.pwyql.pointmakemoney.util.StringUtils;

public class RegisterActivity extends BaseActivity {

    EditText mEtPhoneNumber, mEtVerifyCode, mEtInviteCode;

    TextView mTvTips;

    Button mBtnGetVerifyCode;

    CheckBox mCbLicense;

    boolean isVerified; //当前输入的手机号 是否已短信验证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_register);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("注册");
	// 操作按钮不可见
	findViewById(R.id.tv_top_action).setVisibility(View.GONE);

	mTvTips = getViewById(R.id.tv_reg_instrucation); // 提示,说明

	mEtPhoneNumber = getViewById(R.id.et_phonenumber); // 手机号码
	mEtVerifyCode = getViewById(R.id.et_verifycode);// 输入的 验证码
	mEtInviteCode = getViewById(R.id.et_invite_code); // 邀请码

	mBtnGetVerifyCode = getViewById(R.id.btn_get_verifycode);
	mBtnGetVerifyCode.setEnabled(false); // 默认按钮不可用

	// 查看使用协议
	findViewById(R.id.tv_reg_license).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		startActivity(new Intent(RegisterActivity.this, LicenseActivity.class));
	    }
	});

	mCbLicense = getViewById(R.id.cb_agress_license); // 协议
	mCbLicense.setChecked(true);

	// 
	mEtPhoneNumber.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		isVerified = false;

		String input = mEtPhoneNumber.getText().toString().trim();

		boolean status = input.length() == 11 && StringUtils.isPhoneNumber(input);
		findViewById(R.id.btn_get_verifycode).setEnabled(status);

	    }
	});

	// 获取验证码
	getViewById(R.id.btn_get_verifycode).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		getVerifyCode(); // 获取验证码
	    }
	});

	// 注册
	getViewById(R.id.tv_reg_submit).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		submitReg();
	    }
	});

	// 进入登录
	findViewById(R.id.tv_login_enter).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
		startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
	    }
	});

	initSMSSDK(); // 初始化 短信 sdk
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
			mTvTips.setText("我们已将验证码通过短信发送给您,请注意查收");
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

    String lastPhoneNumber, lastPwd, lastPhoneNumberToken;

    ProgressDialog progressDialog;

    private void getVerifyCode() {
	lastPhoneNumber = mEtPhoneNumber.getText().toString().trim();
	if (lastPhoneNumber.length() != 11 || !StringUtils.isPhoneNumber(lastPhoneNumber)) { // 非(中国大陆)手机号码
	    showToast("手机号码格式不正确");
	    mEtPhoneNumber.requestFocus();
	    return;
	}
	progressDialog = showProgressDialog("验证码获取中");
	
	isVerified = false;
	SMSSDK.getVerificationCode("86", lastPhoneNumber);

	// 清空原输入并focus
	mEtVerifyCode.setText("");
	mEtVerifyCode.requestFocus();

	//设置 获取按钮不可用
	//findViewById(R.id.btn_get_verifycode).setEnabled(false);
    }

    /**
     * 提交注册
     */
    private void submitReg() {
	String phone = mEtPhoneNumber.getText().toString().trim();
	if (phone.length() == 0) {
	    mEtPhoneNumber.requestFocus();
	    showToast("请输入手机号码");
	    return;
	}
	if (phone.length() != 11 || !StringUtils.isPhoneNumber(phone)) {
	    mEtPhoneNumber.requestFocus();
	    showToast("手机号码格式不正确");
	    return;
	}

	lastPhoneNumber = phone;

	String code = mEtVerifyCode.getText().toString().trim();
	if (code.length() == 0) {
	    mEtVerifyCode.requestFocus();
	    showToast("请输入验证码");
	    return;
	}

	if (!mCbLicense.isChecked()) {
	    showToast("您必须同意使用协议才能继续注册");
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
		    reg(token); // 连接服务器进行注册

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
	reg(lastPhoneNumberToken);

	// 提交到服务器进行处理

    }

    /**
     * 注册
     */
    private void reg(String token) {
	if (!ActivityUtil.hasNetwork(this)) {
	    showToast("请开启网络");
	    return;
	}

	final ProgressDialog progress = new ProgressDialog(this);
	progress.setMessage("注册中...");
	progress.show();

	RegParams postParams = new RegParams();
	postParams.phoneNumber = lastPhoneNumber;
	postParams.model = ActivityUtil.getModel();
	postParams.invitecode = mEtInviteCode.getText().toString().trim();
	postParams.phoneNumberToken = token;
	AppServiceImpl.getInstance().reg(postParams, new OnRegResponseListener() {

	    @Override
	    public void onSuccess(String userId, String phone, String pwd) {
		// TODO Auto-generated method stub
		// 保存注册帐号到本地
		PreferencesUtil.save(RegisterActivity.this, "phone", phone);
		PreferencesUtil.save(RegisterActivity.this, "pwd", pwd);
		// 转入登录页, 自动登录
		Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);

		progress.dismiss();

		finish();
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		progress.dismiss();

		showToast(errorMsg);
	    }
	});
    }

    private static long firstTime;

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
	// TODO Auto-generated method stub
	if (firstTime + 2000 > System.currentTimeMillis()) {
	    finish();
	    android.os.Process.killProcess(android.os.Process.myPid());
	} else {
	    showToast("再按一次退出");
	}
	firstTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

	// 注销 handler
	SMSSDK.unregisterAllEventHandler();
    }

}
