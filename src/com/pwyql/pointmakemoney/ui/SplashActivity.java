package com.pwyql.pointmakemoney.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.PreferencesUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	if (PreferencesUtil.read(this, "is_first") == null) {
	    Intent intent = new Intent(this, FirstGuideActivity.class);
	    startActivity(intent);

	    finish();
	    PreferencesUtil.save(this, "is_first", "0");
	    return;
	}

	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_splash);

	new Handler().postDelayed(new Runnable() {

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		jump();
		finish();
	    }

	}, 1000L * 2);
    }

    private void jump() {
	// TODO Auto-generated method stub
	String phone = PreferencesUtil.read(this, "phone");
	String pwd = PreferencesUtil.read(this, "pwd");
	if (phone != null && pwd != null) {// 本地存在注册用户记录
	    // 自动登录
	    // dosometing 
	    // 登录成功进入主页
	    startActivity(new Intent(this, LoginActivity.class));
	    return;
	}

	// 进入注册页
	startActivity(new Intent(this, RegisterActivity.class));

	//	// 测试
	//	User user = new User();
	//	user.setPoints(253523);
	//	user.setUserId("1111111");
	//
	//	MyApplication.getInstance().setCurrentLoginedUser(user);

	//	startActivity(new Intent(this, MainActivity.class));
    }

    private static long firstTime;

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
	// TODO Auto-generated method stub
	if (firstTime + 2000 > System.currentTimeMillis()) {
	    super.onBackPressed();
	} else {
	    showToast("再按一次退出");
	}
	firstTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

}
