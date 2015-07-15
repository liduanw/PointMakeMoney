package com.pwyql.pointmakemoney.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.entity.User;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.LoginParams;
import com.pwyql.pointmakemoney.service.AppService.OnLoginResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.PreferencesUtil;
import com.pwyql.pointmakemoney.util.StringUtils;

public class LoginActivity extends BaseActivity {

    EditText mEtPhoneNumber, mEtPassword;

    TextView mTvTips;

    CheckBox mCbShowpassword; // 显示/隐藏密码

    CheckBox mCbRememberAutologin, mCbRememberPWD;

    boolean isVerified; // 记录最后输入的手机号码是否已验证过

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_login);

	init();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("登录");
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

	mEtPhoneNumber = getViewById(R.id.et_phonenumber); // 手机号码
	mEtPassword = getViewById(R.id.et_pwd); // 登录密码
	mCbShowpassword = getViewById(R.id.cb_showpassword);// 显示/隐藏 密码

	mCbRememberAutologin = getViewById(R.id.cb_remember_autologin);
	mCbRememberPWD = getViewById(R.id.cb_remember_pwd);

	// 读取设置值
	String autoLogin = PreferencesUtil.read(this, "autoLogin");
	String rememberPWD = PreferencesUtil.read(this, "rememberPWD");
	mCbRememberAutologin.setChecked(autoLogin == null || autoLogin.equals("1"));
	mCbRememberPWD.setChecked(rememberPWD == null || rememberPWD.equals("1"));
	// 自动登录
	mCbRememberAutologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		PreferencesUtil.save(LoginActivity.this, "autoLogin", arg1 ? "1" : "0");
	    }
	});

	// 记住密码
	mCbRememberPWD.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		PreferencesUtil.save(LoginActivity.this, "rememberPWD", arg1 ? "1" : "0");
	    }
	});

	// 显示/隐藏 密码
	mCbShowpassword.setChecked(false);
	mCbShowpassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		// TODO Auto-generated method stub
		if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
		    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
		    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
	    }
	});
	mCbShowpassword.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub

	    }
	});

	String phone = PreferencesUtil.read(this, "phone");
	String pwd = PreferencesUtil.read(this, "pwd");

	if (phone != null) {
	    mEtPhoneNumber.setText(phone);
	}

	if (pwd != null && mCbRememberPWD.isChecked()) {
	    mEtPassword.setText(pwd);
	}

	// 当intent 接收到来自注销操作时停止自动登录
	String from = getIntent().getStringExtra("from");
	//	showToast("来源:" + from);
	if (mCbRememberAutologin.isChecked() && !"logout".equals(from)) { // 自动登录
	    String num = mEtPhoneNumber.getText().toString().trim();
	    String passwd = mEtPassword.getText().toString().trim();
	    if (num.length() != 0 && passwd.length() != 0) {
		submitLogin();
	    }
	}

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
		isVerified = false; // 输入改变后重新设为未验证

		String input = mEtPhoneNumber.getText().toString().trim();

		boolean status = input.length() == 11 && StringUtils.isPhoneNumber(input);

	    }
	});

	// 登录
	getViewById(R.id.tv_login_submit).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		submitLogin();
	    }
	});

	// 进入注册
	findViewById(R.id.tv_reg_enter).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
		finish();
	    }
	});

	// 重设密码
	findViewById(R.id.tv_resetpwd_enter).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(LoginActivity.this, ResetLoginPasswordActivity.class);
		intent.putExtra("phone", mEtPhoneNumber.getText().toString().trim());
		startActivity(intent);
	    }
	});

    }

    /**
     * 提交登录
     */
    private void submitLogin() {
	String phone = mEtPhoneNumber.getText().toString().trim();
	if (phone.length() != 11 || !StringUtils.isPhoneNumber(phone)) {
	    mEtPhoneNumber.requestFocus();
	    showToast("手机号码格式不正确");
	    return;
	}

	String pwd = mEtPassword.getText().toString().trim();

	if (pwd.length() == 0) {
	    showToast("请输入密码");
	    mEtPassword.requestFocus();
	    return;
	}

	// 判断是否需要重新保存

	// 每次成功登录后重新保存
	PreferencesUtil.save(this, "phone", phone);
	PreferencesUtil.save(this, "pwd", pwd);

	//	if (pwd.length() < 6 || pwd.length() > 20) {
	//	    showToast("密码长度必须为6~20");
	//	    mEtPassword.requestFocus();
	//	    return;
	//	}

	// 提交到服务器进行处理
	login(phone, pwd);
    }

    /**
     * 登录
     */
    private void login(String phone, String pwd) {
	if (!ActivityUtil.hasNetwork(this)) {
	    showToast("请开启网络");
	    return;
	}

	final ProgressDialog progress = new ProgressDialog(this);
	progress.setMessage("登录中...");
	progress.setCancelable(false);
	progress.setCanceledOnTouchOutside(false);
	progress.show();

	LoginParams postParams = new LoginParams();
	postParams.phoneNumber = phone;
	postParams.pwd = pwd;
	postParams.model = ActivityUtil.getModel();
	postParams.version = ActivityUtil.getVersionCode(this) + "," + ActivityUtil.getVersionName(this);
	postParams.sign = StringUtils.md5(phone + pwd + AppConstants.SIGN_KEY);

	AppServiceImpl.getInstance().login(postParams, new OnLoginResponseListener() {

	    @Override
	    public void onSuccess(User user, int rate) {
		// TODO Auto-generated method stub
		// 转入主页
		MyApplication.getInstance().setCurrentLoginedUser(user);

		MyApplication.exchange_rate = rate; // 兑换率

		startActivity(new Intent(LoginActivity.this, MainActivity.class));

		progress.dismiss();

		// 中止当前页面
		finish();
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);
		progress.dismiss();
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
	    // super.onBackPressed();
	} else {
	    showToast("再按一次退出");
	}
	firstTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	// 注销 handler
	super.onDestroy();
    }

}
