package com.pwyql.pointmakemoney.ui;

import java.io.File;

import net.youmi.android.offers.EarnPointsOrderList;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsEarnNotify;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.aow.android.DAOW;
import cn.guomob.android.intwal.GMTestLog;
import cn.guomob.android.intwal.OpenIntegralWall;
import cn.waps.AppConnect;

import com.bb.dd.BeiduoPlatform;
import com.dc.wall.DianCai;
import com.dc.wall.IEarnMoneyNotifier;
import com.dlnetwork.DevInit;
import com.eadver.offer.sdk.YjfSDK;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.androidservice.UpdateService;
import com.pwyql.pointmakemoney.fragment.MallFragment;
import com.pwyql.pointmakemoney.fragment.PersonalFragment;
import com.pwyql.pointmakemoney.fragment.PointTaskFragment;
import com.pwyql.pointmakemoney.fragment.RankingFragment;
import com.pwyql.pointmakemoney.service.AppService.CheckUpdateParams;
import com.pwyql.pointmakemoney.service.AppService.NewestAppInfo;
import com.pwyql.pointmakemoney.service.AppService.OnCheckUpdateResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.pwyql.pointmakemoney.util.ActivityManagerUtils;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.ApkSign;
import com.pwyql.pointmakemoney.util.ImageLoaderOptions;
import com.pwyql.pointmakemoney.util.PreferencesUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class MainActivity extends BaseActivity {

    Fragment[] fragments;

    Button[] mTabs;

    TextView mTvTopTitle, mTvTopAction;

    Fragment personalFragment, pointTaskFragment, rankingFragment, /*settingsFragment,*/mallFragment;

    ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	mRootView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_main, null);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(mRootView);

	initTabs();
	initAds();

	// 初始化友盟分享
	initUmengSocial();

	// 初始化UniversalImageLoader
	initImageLoader();

	System.out.println("签名:" + ApkSign.getAPPSecretString(this));
	//	showToast(ApkSign.getAPPSecretString(this));

    }

    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private void initUmengSocial() {
	// TODO Auto-generated method stub

	//	// 设置分享内容
	//	mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
	//	// 设置分享图片, 参数2为图片的url地址
	//	mController.setShareMedia(new UMImage(this, "http://www.umeng.com/images/pic/banner_module_social.png"));
	//	// 设置分享图片，参数2为本地图片的资源引用
	mController.setShareMedia(new UMImage(this, R.drawable.icon));
	// 设置分享图片，参数2为本地图片的路径(绝对路径)
	//	mController.setShareMedia(new UMImage(this, BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

	// 设置分享音乐
	UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
	uMusic.setAuthor("GuGu");
	uMusic.setTitle("天籁之音");
	// 设置音乐缩略图
	//uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
	//mController.setShareMedia(uMusic);

	// 设置分享视频
	//UMVideo umVideo = new UMVideo(
	//	          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
	// 设置视频缩略图
	//umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
	//umVideo.setTitle("友盟社会化分享!");
	//mController.setShareMedia(umVideo);

	// 微信
	String appID = "wxe6148939346980ec";
	String appSecret = "f7f4c9576042feafc28855545cce1499";
	// 添加微信平台
	UMWXHandler wxHandler = new UMWXHandler(this, appID, appSecret);
	wxHandler.addToSocialSDK();
	// 添加微信朋友圈
	UMWXHandler wxCircleHandler = new UMWXHandler(this, appID, appSecret);
	wxCircleHandler.setToCircle(true);
	wxCircleHandler.addToSocialSDK();
	// 分享给QQ好友
	//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
	UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104654390", "klKSrcCzvOoOeiOU");
	qqSsoHandler.addToSocialSDK();
	// Sso 免登录分享到qq空间
	//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
	QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1104654390", "klKSrcCzvOoOeiOU");
	qZoneSsoHandler.addToSocialSDK();
	// SSO（免登录）分享到新浪微博 // 需要重写 onActivityResult方法
	//设置新浪SSO handler
	mController.getConfig().setSsoHandler(new SinaSsoHandler());
	// SSO（免登录）分享到腾讯微博
	//设置腾讯微博SSO handler
	mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

	// 注册微信回调方法
	// 在AndroidManifest.xml中下注册下面的回调WXEntryActivity
	// 微信分享回调
	SnsPostListener mSnsPostListener = new SnsPostListener() {

	    @Override
	    public void onStart() {

	    }

	    @Override
	    public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
		if (stCode == 200) {
		    showToast("分享成功");
		} else {
		    showToast("分享失败 : error code : " + stCode);
		}
	    }
	};
	mController.registerListener(mSnsPostListener);
    }

    /**
     * 初始化UniversalImageLoader
     * 
     */
    private void initImageLoader() {
	// TODO Auto-generated method stub
	File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCache(new LruMemoryCache(5 * 1024 * 1024)).memoryCacheSize(10 * 1024 * 1024).diskCache(new UnlimitedDiscCache(cacheDir)).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).defaultDisplayImageOptions(ImageLoaderOptions.normalOptions()).build();

	ImageLoader.getInstance().init(config);
    }

    // 在对应的activity中实现onActivityResult方法, 并添加如下代码

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	/**使用SSO授权必须添加如下代码 */
	UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
	if (ssoHandler != null) {
	    ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	}
    }

    private void initAds() {
	// TODO Auto-generated method stub
	try {
	    initAdYoumi();
	} finally {

	}

	initAdDomob(); //
	initAdGuomob();
	initAdWaps();//
	//	initAdMobsmar();
	//initAdYijifen();
	initAdDianjoy();
	initAdDiancai();
	initAdBeiduo();

    }

    /**
     * 贝多
     */
    private void initAdBeiduo() {
	// TODO Auto-generated method stub
	String appid = "13712";
	String appkey = "14d90bb5eca1112";
	BeiduoPlatform.setAppId(this, appid, appkey);
	BeiduoPlatform.setUserId(MyApplication.getInstance().getCurrentLoginedUser().getUserId());
    }

    /**
     * 点财
     */
    private void initAdDiancai() {
	// TODO Auto-generated method stub
	DianCai.initApp(this, "11209", "701053a1deec45cca965490e85b94394");
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	DianCai.setUserId(userId);

	//激活APP或签到成功后的，回调奖励的通知。
	DianCai.setEarnMoneyListener(new IEarnMoneyNotifier() {

	    @Override
	    public void earnSuccess(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		//参数介绍
		//arg0: 等于0 表示首次激活App得到的奖励；若大于0 表示 第arg0次签到赚取的奖励；
		//arg1: 赚取的虚拟货币数量；
		//arg2: 广告的包名称；

		if (arg0 == 0) {//来自安装激活通知
		    showToast("任务激活成功,赚取了" + arg1 + "积分;");
		    Log.i("广告激活成功", "赚取了" + arg1 + "积分;");

		} else if (arg0 > 0) {//来自签到的通知

		    Log.i("第" + arg0 + "次签到成功", "赚取了" + arg1 + "积分;");

		}
	    }

	    @Override
	    public void earnFailed(int arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		//参数介绍
		//arg0: 等于0 表示首次激活App得到的奖励；若大于0 表示 第arg0次签到赚取的奖励；
		//arg1: 赚取的虚拟货币数量；
		//arg2: 失败的错误信息；

		Log.i("广告激活(签到)奖励失败", arg2);

	    }
	});
    }

    /**
     * 点乐
     */
    private void initAdDianjoy() {
	// TODO Auto-generated method stub
	String appID = "97727f1b04ebffbc4d9bec0b8b2eb64d";
	//	String appID = "TEST_DIANJOY_APP_ID";
	DevInit.initGoogleContext(this, appID);
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	DevInit.setCurrentUserID(this, userId);
    }

    /**
     * 易积分
     */
    private void initAdYijifen() {
	// TODO Auto-generated method stub
	//初始化,当Activity第一次创建时调用,第二个this为UpdateScordNotifier接口，如果不需要回调接口则可以写null
	YjfSDK.getInstance(this, null).initInstance("76810", "EMHVPFS6T9QPANBS7W4EJJ7E9PZWUBEC9D", "243", "sdk 5.0.0");
	//	YjfSDK.getInstance(this, null).initInstance("76312","EMYFMJZ4ESY70FM9L36SD9FGPNVK8Y7QIF","243","sdk 5.0.0");
	//设定做服务器端回调必要的参数,如果参数传空，则广告激活后不给服务端回调； 不可以所有用户传递同一个值，如果不需要服务端回调，可以不调用改方法，
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	YjfSDK.getInstance(this, null).setCoopInfo(userId);
	//通知栏上通知当天的新任务和可完成任务的个数
	YjfSDK.getInstance(this, null).setDoNotify(true);
    }

    /**
     * 指盟
     */
    private void initAdMobsmar() {
	// TODO Auto-generated method stub
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	com.ZMAD.conne.AdManager ad = new com.ZMAD.conne.AdManager(this, "9dd2b41c397a2592c5037cf6a4b538a8", userId);
	ad.init();
    }

    /**
     * 万普
     */
    private void initAdWaps() {
	// TODO Auto-generated method stub
	AppConnect.getInstance("108ec67a5b3c8cd82df2ad6b3edab662", "108ec67a5b3c8cd82df2ad6b3edab662", this);
    }

    /**
     * 果盟
     */
    private void initAdGuomob() {
	// TODO Auto-generated method stub

	GMTestLog.setTest();
	OpenIntegralWall.initServiceType(this); // 服务器积分回调

    }

    /**
     * 多盟
     */
    private void initAdDomob() {
	// TODO Auto-generated method stub
	// 初始化积分墙
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	DAOW.getInstance(this).init(this, "96ZJ1VWAze7q/wTMgo", userId);
    }

    /**
     * 有米初始化
     */
    private void initAdYoumi() {
	// TODO Auto-generated method stub
	String appId = "7104dc18e08f0342";
	String appSecret = "8b6fc3d7c9e222c6";

	net.youmi.android.AdManager.getInstance(this).init(appId, appSecret, false);

	// 积分墙
	net.youmi.android.offers.OffersManager.getInstance(this).onAppLaunch();
	// 设置用户标识
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	net.youmi.android.offers.OffersManager.getInstance(this).setCustomUserId(userId);
	// 开启用户数据统计
	net.youmi.android.AdManager.getInstance(this).setUserDataCollect(true);
	// 开启积分墙通知提醒
	net.youmi.android.offers.PointsManager.setEnableEarnPointsNotification(true);
	// 开启积分到账悬浮框提示功能
	net.youmi.android.offers.PointsManager.setEnableEarnPointsToastTips(true);
	// 让UI刷新积分(需要在onDestroy 方法中销毁
	net.youmi.android.offers.PointsManager.getInstance(this).registerNotify(pointsChangeNotify);

	// 设置积分墙样式
	net.youmi.android.offers.OffersBrowserConfig.setBrowserTitleText("免费赚取积分");
	net.youmi.android.offers.OffersBrowserConfig.setBrowserTitleBackgroundColor(readTopBackgroundColor());

	// showToast("积分:" +
	// net.youmi.android.offers.PointsManager.getInstance(this).queryPoints()
	// + "");

	// 在onDestroy时 调用exit
	// net.youmi.android.offers.OffersManager.getInstance(this).onAppExit();

	// 注册积分更新监听(在onDestroy中解除注册)
	net.youmi.android.offers.PointsManager.getInstance(this).registerPointsEarnNotify(pointsEarnNotify);

	// 使用服务器通知
	net.youmi.android.offers.OffersManager.setUsingServerCallBack(true);
    }

    /**
     * 有米
     */

    PointsEarnNotify pointsEarnNotify = new PointsEarnNotify() {

	@Override
	public void onPointEarn(Context arg0, EarnPointsOrderList arg1) {
	    // TODO Auto-generated method stub
	    showToast(arg1.get(0).getPoints() + "积分");
	}
    };

    // 积分变动通知
    PointsChangeNotify pointsChangeNotify = new PointsChangeNotify() {

	@Override
	public void onPointBalanceChange(int arg0) {
	    // TODO Auto-generated method stub
	    //
	    showToast("当前积分:" + arg0);
	}
    };

    private void initTabs() {

	mTvTopTitle = (TextView) findViewById(R.id.layout_nav_top).findViewById(R.id.tv_top_title);
	mTvTopAction = (TextView) findViewById(R.id.layout_nav_top).findViewById(R.id.tv_top_action);
	// 右侧滑动菜单
	initSlidingMenu();

	mTabs = new Button[4];
	mTabs[0] = (Button) findViewById(R.id.btn_01); // 个人
	mTabs[1] = (Button) findViewById(R.id.btn_02); // 积分获取通道
	mTabs[2] = (Button) findViewById(R.id.btn_03); // 排名
	mTabs[3] = (Button) findViewById(R.id.btn_04); // 商城
	//	mTabs[4] = (Button) findViewById(R.id.btn_05); // 设置
	//	mTabs[4] = (Button) findViewById(R.id.btn_05); // 商城

	personalFragment = new PersonalFragment(); // 个人信息
	//	settingsFragment = new SettingsFragment();// 应用设置
	pointTaskFragment = new PointTaskFragment(); // 赚积分 各平台广告列表
	rankingFragment = new RankingFragment(); // 赚积分 各平台广告列表
	mallFragment = new MallFragment(); // 商城
	fragments = new Fragment[] { personalFragment, pointTaskFragment, rankingFragment, mallFragment };

	// 默认app启动时选中第一项
	mTabs[0].setSelected(true);
	mTvTopTitle.setText("个人");
	mTvTopAction.setVisibility(View.GONE); // 
	findViewById(R.id.layout_nav_top).setVisibility(View.GONE); // 默认个人页隐藏标题栏

	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	// 启动时只添加首页的 fragement, 其他页面需要时再add
	transaction.add(R.id.fragment_container, personalFragment);
	// 为了接收消息 所以得先创建
	//	transaction.add(R.id.fragment_container, settingsFragment);
	transaction.add(R.id.fragment_container, pointTaskFragment);
	transaction.add(R.id.fragment_container, mallFragment);
	transaction.add(R.id.fragment_container, rankingFragment);

	transaction.show(personalFragment);
	transaction.hide(pointTaskFragment);
	//	transaction.hide(settingsFragment);
	transaction.hide(mallFragment);
	transaction.hide(rankingFragment);

	transaction.commit();
    }

    private SlidingMenu menu;

    /**
     * 侧滑菜单
     */
    private void initSlidingMenu() {
	// TODO Auto-generated method stub
	menu = new SlidingMenu(this);
	menu.setMode(SlidingMenu.LEFT);
	// 设置触摸屏幕的模式
	menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 边界滑动, 全屏滑动TOUCHMODE_FULLSCREEN
	menu.setShadowWidthRes(R.dimen.shadow_width);
	menu.setShadowDrawable(R.drawable.icon); // shadow
	// 设置滑动菜单视图的宽度
	menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	// 设置渐入渐出效果的值
	menu.setFadeDegree(0.35f);
	/**
	* SLIDING_WINDOW will include the Title/ActionBar in the content
	* section of the SlidingMenu, while SLIDING_CONTENT does not.
	*/
	menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	//为侧滑菜单设置布局
	menu.setMenu(R.layout.layout_menu);

	//	// 显示/隐藏 侧滑菜单
	findViewById(R.id.tv_left_icon).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		menu.toggle(true);
	    }
	});

	View.OnClickListener listener = new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.layout_share:
		    startActivity(new Intent(MainActivity.this, ShareActivity.class));
		    break;
		case R.id.layout_notices:
		    startActivity(new Intent(MainActivity.this, com.pwyql.pointmakemoney.ui.NoticesActivity.class));
		    break;
		case R.id.layout_checkupdate:
		    checkUpdate();
		    break;
		case R.id.layout_help:
		    startActivity(new Intent(MainActivity.this, com.pwyql.pointmakemoney.ui.HelpActivity.class));
		    break;
		case R.id.layout_about:
		    startActivity(new Intent(MainActivity.this, AboutActivity.class));
		    break;
		case R.id.layout_logout:
		    finish();
		    // TODO Auto-generated method stub
		    MyApplication.getInstance().logout();
		    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		    intent.putExtra("from", "logout");
		    startActivity(intent);
		    break;
		default:
		    break;
		}
	    }
	};
	// share
	getViewById(R.id.layout_share).setOnClickListener(listener);
	getViewById(R.id.layout_notices).setOnClickListener(listener);
	getViewById(R.id.layout_checkupdate).setOnClickListener(listener);
	getViewById(R.id.layout_help).setOnClickListener(listener);
	getViewById(R.id.layout_about).setOnClickListener(listener);
	getViewById(R.id.layout_logout).setOnClickListener(listener);

	// 自动检查更新
	String checkupdateWhenStartUp = PreferencesUtil.read(this, "checkupdate_when_startup");
	if (null == checkupdateWhenStartUp || "1".equals(checkupdateWhenStartUp)) {
	    CheckUpdateParams postParams = new CheckUpdateParams();
	    postParams.currentVersionCode = ActivityUtil.getVersionCode(this);

	    AppServiceImpl.getInstance().checkUpdate(postParams, new OnCheckUpdateResponseListener() {

		@Override
		public void onSuccess(NewestAppInfo app) {
		    // TODO Auto-generated method stub
		    showDownloadDialog(app);// 询问下载
		}

		@Override
		public void onFailure(String errorMsg, int status) {
		    // TODO Auto-generated method stub
		    if (status == -9) {
			showToast("connection timeout!");
		    }
		}
	    });
	}
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
	if (!ActivityUtil.hasNetwork(this)) {
	    showToast("请开启网络");
	    return;
	}
	final ProgressDialog progressDialog = new ProgressDialog(this);
	progressDialog.setMessage("检查最新版本...");
	progressDialog.show();
	CheckUpdateParams postParams = new CheckUpdateParams();
	postParams.currentVersionCode = ActivityUtil.getVersionCode(this);

	AppServiceImpl.getInstance().checkUpdate(postParams, new OnCheckUpdateResponseListener() {

	    @Override
	    public void onSuccess(NewestAppInfo app) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		showDownloadDialog(app);// 询问下载
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		if (status == -9) {
		    showToast("connection timeout!");
		} else if (status == 0) {
		    showToast("当前已是最新版本");
		}
		progressDialog.dismiss();
	    }
	});
    }

    /**
     * 询问下载更新
     * @param app
     */
    private void showDownloadDialog(final NewestAppInfo app) {
	// TODO Auto-generated method stub
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("有新版本需要更新");
	builder.setMessage("更新版本: " + app.versionName + "\n更新内容说明如下:\n" + app.instruction);
	builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		//开启更新服务UpdateService
		//这里为了把update更好模块化，可以传一些updateService依赖的值
		//如布局ID，资源ID，动态获取的标题,这里以app_name为例
		if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) {
		    showToast("SD卡已移出,无法下载!");
		    return;
		}

		Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
		Bundle extras = new Bundle();
		extras.putSerializable("appinfo", app);
		updateIntent.putExtras(extras);
		startService(updateIntent);
	    }
	});
	builder.setNegativeButton("取消", null);
	builder.create().show();
    }

    private int currentIndex;

    public void onTabSelect(View v) {
	int index = 0;
	switch (v.getId()) {
	case R.id.btn_01:
	    index = 0;
	    mTvTopTitle.setText(R.string.myinfo);
	    mTvTopAction.setVisibility(View.GONE);
	    findViewById(R.id.layout_nav_top).setVisibility(View.GONE);
	    break;
	case R.id.btn_02:
	    index = 1;
	    mTvTopTitle.setText(R.string.tasks);
	    // mTvTopAction.setText("++");
	    mTvTopAction.setVisibility(View.GONE);
	    findViewById(R.id.layout_nav_top).setVisibility(View.VISIBLE);
	    break;
	case R.id.btn_03: // 排名
	    index = 2;
	    mTvTopTitle.setText(R.string.ranking);
	    // mTvTopAction.setText("++");
	    mTvTopAction.setVisibility(View.GONE);
	    findViewById(R.id.layout_nav_top).setVisibility(View.VISIBLE);
	    break;

	case R.id.btn_04:
	    index = 3;
	    mTvTopTitle.setText(R.string.exchange_mall);
	    mTvTopAction.setVisibility(View.GONE);
	    findViewById(R.id.layout_nav_top).setVisibility(View.VISIBLE);
	    break;
	}

	if (currentIndex != index) {
	    FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
	    trans.hide(fragments[currentIndex]);
	    if (!fragments[index].isAdded()) {
		trans.add(R.id.fragment_container, fragments[index]);
	    }
	    trans.show(fragments[index]).commit();
	}

	mTabs[currentIndex].setSelected(false);
	currentIndex = index;
	mTabs[currentIndex].setSelected(true);
    }

    private static long firstTime;

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
	//	// 判断当前页面是否为 商城 页面 , webview需要按返回键回退
	//	if (currentIndex == 3) {
	//	    if (((MallFragment) mallFragment).onBackkeyPressed()) {
	//		return;
	//	    }
	//	}

	// TODO Auto-generated method stub
	// 按返回键关闭菜单
	if (menu.isMenuShowing()) {
	    menu.toggle();
	    return;
	}

	if (firstTime + 2000 > System.currentTimeMillis()) {
	    //	    Intent intent = new Intent(Intent.ACTION_MAIN);
	    //	    intent.addCategory(Intent.CATEGORY_HOME);
	    //	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    //	    startActivity(intent);
	    ActivityManagerUtils.getInstance().removeAllActivity();
	    android.os.Process.killProcess(android.os.Process.myPid());

	    // super.onBackPressed();
	} else {
	    //	    showToast("再按一次返回HOME");
	    showToast("再按一次退出");
	}
	firstTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

	// 销毁 广告
	// 有米
	net.youmi.android.offers.OffersManager.getInstance(this).onAppExit();

	net.youmi.android.offers.PointsManager.getInstance(this).unRegisterPointsEarnNotify(pointsEarnNotify);

	net.youmi.android.offers.PointsManager.getInstance(this).unRegisterNotify(pointsChangeNotify);

	// 万普
	AppConnect.getInstance(this).close();

    }

}
