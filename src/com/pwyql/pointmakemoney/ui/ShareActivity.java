package com.pwyql.pointmakemoney.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.ui.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareActivity extends BaseActivity {
    EditText mEtShareContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_share);

	init();

	initUmengSocial();
    }

    private void init() {
	// TODO Auto-generated method stub
	TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);//
	tvTopTitle.setText("分享");
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

	findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		share();
	    }
	});

	final TextView tvInviteCode = getViewById(R.id.tv_invitecode);
	tvInviteCode.setText("你的邀请码: " + MyApplication.getInstance().getCurrentLoginedUser().getInviteCode());

	getViewById(R.id.tv_copy_invitecode).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(tvInviteCode.getText().toString().replace("你的邀请码: ", ""));
		showToast("已复制");
	    }
	});
	//	mEtShareContent = getViewById(R.id.et_share_content);
	//	mEtShareContent.setText(content);
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

    private void share() {
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	// 删除
	//	mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);

	String content = "一分钟赚1元, 你相信吗? 我在用这个APP赚钱, 只需每天动动手指就有钱拿, 现在新注册的用户只需要输入我的邀请码" + MyApplication.getInstance().getCurrentLoginedUser().getInviteCode() + "即可获得1元奖励! APP下载地址: " + AppConstants.URL_APP;

	// 设置基本分享内容
	mController.setShareContent(content);
	// 设置分享图片, 参数2为图片的url地址
	mController.setShareMedia(new UMImage(this, R.drawable.icon));

	//设置微信好友分享内容
	WeiXinShareContent weixinContent = new WeiXinShareContent();
	//设置分享文字
	weixinContent.setShareContent(content);
	//设置title
	weixinContent.setTitle("可以赚钱的APP");
	//设置分享内容跳转URL
	weixinContent.setTargetUrl(AppConstants.URL_APP_WEBSITE);
	//设置分享图片
	weixinContent.setShareImage(new UMImage(this, R.drawable.icon));
	mController.setShareMedia(weixinContent);

	//设置微信朋友圈分享内容
	CircleShareContent circleMedia = new CircleShareContent();
	circleMedia.setShareContent(content);
	circleMedia.setAppWebSite(AppConstants.URL_APP_WEBSITE);
	//设置朋友圈title
	circleMedia.setTitle("可以赚钱的APP");
	circleMedia.setShareImage(new UMImage(this, R.drawable.icon));
	circleMedia.setTargetUrl(AppConstants.URL_APP_WEBSITE);
	mController.setShareMedia(circleMedia);

	//QQ分享内容
	QQShareContent qqShareContent = new QQShareContent();
	//设置分享文字
	qqShareContent.setShareContent(content);
	//设置分享title
	qqShareContent.setTitle("可以赚钱的APP");
	//设置分享图片
	qqShareContent.setShareImage(new UMImage(this, R.drawable.icon));
	//设置点击分享内容的跳转链接
	qqShareContent.setTargetUrl(AppConstants.URL_APP_WEBSITE);
	mController.setShareMedia(qqShareContent);

	// QQ空间分享
	QZoneShareContent qzone = new QZoneShareContent();
	//设置分享文字
	qzone.setShareContent(content);
	//设置点击消息的跳转URL
	qzone.setTargetUrl(AppConstants.URL_APP_WEBSITE);
	//设置分享内容的标题
	qzone.setTitle("可以赚钱的APP");
	//设置分享图片
	qzone.setShareImage(new UMImage(this, R.drawable.icon));
	mController.setShareMedia(qzone);

	// 是否只有已登录用户才能打开分享选择页
	mController.openShare(this, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	/**使用SSO授权必须添加如下代码 */
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
	if (ssoHandler != null) {
	    ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	}
    }

    {
	// 自定义分享按钮列表
	//	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	//
	//	// 提交分享到新浪微博
	//	mController.postShare(getActivity(), SHARE_MEDIA.SINA, new SnsPostListener() {
	//	    @Override
	//	    public void onStart() {
	//		showToast("开始分享.");
	//	    }
	//
	//	    @Override
	//	    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
	//		if (eCode == 200) {
	//		    showToast("分享成功.");
	//		} else {
	//		    String eMsg = "";
	//		    if (eCode == -101) {
	//			eMsg = "没有授权";
	//		    }
	//		    showToast("分享失败[" + eCode + "] " + eMsg);//
	//		}
	//	    }
	//	});
	//
	//	// 分享到微信
	//	mController.postShare(this.getActivity(), SHARE_MEDIA.WEIXIN, new SnsPostListener() {
	//	    @Override
	//	    public void onStart() {
	//		showToast("开始分享.");
	//	    }
	//
	//	    @Override
	//	    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
	//		if (eCode == 200) {
	//		    showToast("分享成功.");
	//		} else {
	//		    String eMsg = "";
	//		    if (eCode == -101) {
	//			eMsg = "没有授权";
	//		    }
	//		    showToast("分享失败[" + eCode + "] " + eMsg);
	//		}
	//	    }
	//	});
	//
	//	// 分享到微信朋友圈
	//	mController.postShare(this.getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {
	//	    @Override
	//	    public void onStart() {
	//		showToast("开始分享.");
	//	    }
	//
	//	    @Override
	//	    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
	//		if (eCode == 200) {
	//		    showToast("分享成功.");
	//		} else {
	//		    String eMsg = "";
	//		    if (eCode == -101) {
	//			eMsg = "没有授权";
	//		    }
	//		    showToast("分享失败[" + eCode + "] " + eMsg);
	//		}
	//	    }
	//	});
    }

    @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
    }
}
