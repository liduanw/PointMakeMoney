package com.pwyql.pointmakemoney.service.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter.Goods;
import com.pwyql.pointmakemoney.adapter.WordsAdapter.Word;
import com.pwyql.pointmakemoney.entity.User;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService;
import com.pwyql.pointmakemoney.util.HttpUtil;

public class AppServiceImpl implements AppService {
    private static AppService instance;

    private AppServiceImpl() {

    }

    public synchronized static AppService getInstance() {
	if (instance == null) {
	    instance = new AppServiceImpl();
	}
	return instance;
    }

    @Override
    public void reg(RegParams postParams, final OnRegResponseListener listener) {
	// TODO Auto-generated method stub

	String url = AppConstants.URL_REGISTER;
	RequestParams params = new RequestParams();
	params.put("phone", postParams.phoneNumber);
	params.put("invitecode", postParams.invitecode); // 邀请码
	params.put("model", postParams.model); // model
	params.put("token", postParams.phoneNumberToken); // 短信成功验证后返回的token, 确保手机号认证

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("注册:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (status != 1) {
			String msg = jsonObject.getString("msg");
			listener.onFailure(msg, status);
			return;
		    }

		    String userId = jsonObject.getString("uid");
		    String phone = jsonObject.getString("phone");
		    String pwd = jsonObject.getString("pwd");
		    listener.onSuccess(userId, phone, pwd);
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    listener.onFailure("参数错误", -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -1);
	    }
	});
    }

    @Override
    public void login(LoginParams postParams, final OnLoginResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_LOGIN;
	RequestParams params = new RequestParams();
	params.put("phone", postParams.phoneNumber);
	params.put("pwd", postParams.pwd);
	params.put("model", postParams.model);
	params.put("version", postParams.version);
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		for (Header header : arg1) {
		    Log.i("header:" + header.getName(), header.getValue());
		    if ("PHPSESSID".equals(header.getName())) {
			MyApplication.PHPSESSID = header.getValue();
			break;
		    }
		}

		String res = new String(arg2);
		Log.i("login:", res);
		try {
		    JSONObject jsonObject = new JSONObject(res);

		    int status = jsonObject.getInt("status");
		    if (1 != status) {
			String msg = jsonObject.getString("msg");
			listener.onFailure(msg, status);
			return;
		    }

		    User user = new User();
		    int points = jsonObject.getInt("points");
		    String userId = jsonObject.getString("uid");

		    String inviteCode = jsonObject.getString("invitecode");

		    boolean hasCashpwd = jsonObject.getInt("has_cashpwd") == 1;

		    int rate = jsonObject.getInt("rate");

		    user.setPoints(points);
		    user.setUserId(userId);

		    user.setInviteCode(inviteCode);

		    user.setHasCashpwd(hasCashpwd);

		    listener.onSuccess(user, rate);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误", -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -1);
	    }
	});
    }

    @Override
    public void points(final OnPointsResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_POINTS;
	RequestParams params = new RequestParams();

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("points:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 == status) {
			int points = jsonObject.getInt("points");
			listener.onSuccess(points);
			return;
		    }
		    String msg = jsonObject.getString("msg");
		    listener.onFailure(msg, status);
		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //参数错误
		    listener.onFailure(res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -1);
	    }
	});
    }

    /**
     * 提交验证码到APP服务器
     */
    @Override
    public void verifyPhoneCode(VerifyPhoneCodeParams postParams, final OnVerifyPhoneCodeResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_SMSVERIFY_CODE;
	RequestParams params = new RequestParams();
	params.put("appkey", postParams.appKey);
	params.put("phone", postParams.phone);
	params.put("zone", postParams.zone);
	params.put("code", postParams.code);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("sms verify:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 != status) {
			listener.onFailure("验证失败", status);
			return;
		    }
		    String token = jsonObject.getString("token");
		    listener.onSuccess(token);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -1);
	    }
	});
    }

    @Override
    public void resetLoginPassword(ResetLoginPwdParams postParams, final OnResetpwdResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_RESET_PWD_LOGIN;
	RequestParams params = new RequestParams();
	params.put("phone", postParams.phoneNumber);
	params.put("pwd", postParams.password);
	params.put("token", postParams.token);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("重置登录密码:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 != status) { // 修改成功
			String msg = jsonObject.getString("msg");
			listener.onFailure(msg, status);
			return;
		    }
		    listener.onSuccess();

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -1);
	    }
	});
    }

    @Override
    public void resetCashPassword(ResetCashPwdParams postParams, final OnResetcashResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_RESET_PWD_CASH;
	RequestParams params = new RequestParams();
	params.put("phone", postParams.phoneNumber);
	params.put("cashpwd", postParams.password);
	params.put("token", postParams.token);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("重置提现密码:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 != status) {
			String msg = jsonObject.getString("msg");
			listener.onFailure(msg, status);
			return;
		    }
		    listener.onSuccess();

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -1);
	    }
	});
    }

    @Override
    public void checkUpdate(CheckUpdateParams postParams, final OnCheckUpdateResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_CHECK_VERSION;
	RequestParams params = new RequestParams();
	params.put("ver_code", postParams.currentVersionCode);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("checkupdate:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (0 == status) { // 无更新
			listener.onFailure("当前已是最新版本", 0);
			return;
		    }
		    NewestAppInfo appInfo = new NewestAppInfo();
		    String versionName = jsonObject.getString("ver_name");
		    int size = jsonObject.getInt("size");
		    String instruction = jsonObject.getString("instr");
		    String url = jsonObject.getString("url");
		    appInfo.versionName = versionName;
		    appInfo.size = size;
		    appInfo.instruction = instruction;
		    appInfo.url = url;

		    listener.onSuccess(appInfo);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    /**
     * 发布留言
     */
    @Override
    public void publishWord(PublishWordParams postParams, final OnPublishWordResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_PUBLISH_WORD;
	RequestParams params = new RequestParams();
	params.put("content", postParams.content);
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		String res = new String(arg2);
		System.out.println("发布留言:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (0 == status) { // 
			String msg = jsonObject.getString("msg");
			listener.onFailure(msg, 0);
			return;
		    }

		    String id = jsonObject.getString("id");
		    String name = jsonObject.getString("name");
		    String content = jsonObject.getString("word");
		    int time = jsonObject.getInt("time");
		    Word word = new Word(id, name, content, time);

		    listener.onSuccess(word);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void selectWords(SelectWordsParams postParams, final OnSelectWordsResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_WORDS;
	RequestParams params = new RequestParams();
	params.put("page", postParams.page);
	params.put("size", postParams.pageSize);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("留言:" + res);
		try {
		    JSONArray jsonArray = new JSONArray(res);
		    List<Word> words = new ArrayList<Word>();
		    for (int i = 0; i < jsonArray.length(); ++i) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String id = jsonObject.getString("id");
			String name = jsonObject.getString("name");
			String content = jsonObject.getString("word");
			int time = jsonObject.getInt("time");
			Word word = new Word(id, name, content, time);
			words.add(word);
		    }

		    listener.onSuccess(words);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void withdraw(WithdrawParams postParams, final OnWithdrawResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_WITHDRAW;
	RequestParams params = new RequestParams();
	params.put("type", postParams.type);
	params.put("p_type", postParams.priceType);
	params.put("ac_num", postParams.accountNumber);
	params.put("cpd", postParams.cashPwd);
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("提现:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 == status) {
			String id = jsonObject.getString("id");
			WithdrawOrder order = new WithdrawOrder();
			order.id = id;
			listener.onSuccess(order);
			return;
		    }

		    String msg = jsonObject.getString("msg");
		    listener.onFailure(msg, status);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void loadInfo(LoadInfoParams postParams, final OnLoadInfoResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_USERINFO;
	RequestParams params = new RequestParams();
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("info:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 == status) {
			JSONObject useObject = jsonObject.getJSONObject("user");
			Info info = new Info();
			info.name = useObject.getString("name");
			info.qq = useObject.getString("qq");
			info.alipayAccount = useObject.getString("ali_acc");
			info.alipayName = useObject.getString("ali_name");
			info.phone = useObject.getString("phone");
			info.points = useObject.getInt("points");

			info.receAddr = useObject.getString("rece_addr");
			info.receName = useObject.getString("rece_name");
			info.recePhone = useObject.getString("rece_phone");
			listener.onSuccess(info);
			return;
		    }

		    String msg = jsonObject.getString("msg");
		    listener.onFailure(msg, status);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void loadGoods(LoadGoodsParams postParams, final OnLoadGoodsResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_MALL_GOODSLIST;
	RequestParams params = new RequestParams();
	params.put("page", postParams.page);
	params.put("size", postParams.size);
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("goods:" + res);
		try {
		    JSONArray jsonArray = new JSONArray(res);
		    List<Goods> goodsList = new ArrayList<GoodsAdapter.Goods>();
		    for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("id");
			String title = jsonObject.getString("title");
			String imgUrl = jsonObject.getString("img01");
			int price = jsonObject.getInt("price");
			int quantity = jsonObject.getInt("qt");
			Goods goods = new Goods(id, title, imgUrl, price, quantity);
			goodsList.add(goods);
		    }

		    listener.onSuccess(goodsList);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void loadGoodsDetail(LoadGoodsDetailParams postParams, final OnLoadGoodsDetailResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_MALL_GOODSDETAIL;
	RequestParams params = new RequestParams();
	params.put("gid", postParams.goodsId);
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("goodsDetail:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    String imgUrl01 = jsonObject.getString("img01");
		    String imgUrl02 = jsonObject.getString("img02");
		    String imgUrl03 = jsonObject.getString("img03");
		    int quantity = jsonObject.getInt("qt");
		    String description = jsonObject.getString("des");
		    int price = jsonObject.getInt("price");

		    GoodsDetail goodsDetail = new GoodsDetail();
		    goodsDetail.image01 = imgUrl01;
		    goodsDetail.image02 = imgUrl02;
		    goodsDetail.image03 = imgUrl03;
		    goodsDetail.quantity = quantity;
		    goodsDetail.description = description;
		    goodsDetail.price = price;
		    listener.onSuccess(goodsDetail);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void exchange(ExchangeParams postParams, final OnExchangeResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_MALL_EXCHANGE;
	RequestParams params = new RequestParams();
	params.put("gid", postParams.goodsId);
	params.put("cashpwd", postParams.cashPwd);
	params.put("rece_addr", postParams.receAddr);
	params.put("rece_phone", postParams.recePhone);
	params.put("rece_name", postParams.receName);
	params.put("sign", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("兑换商品:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 == status) {
			String orderId = jsonObject.getString("order");
			listener.onSuccess(orderId);
			return;
		    }

		    String msg = jsonObject.getString("msg");
		    listener.onFailure(msg, status);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void adReward(AdRewardParams postParams, final OnAdRewardResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_AD_REWARD;
	RequestParams params = new RequestParams();
	params.put("m", postParams.model);
	params.put("t", postParams.time);
	params.put("s", postParams.sign);

	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("请求广告奖励:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 == status) {
			int reward = jsonObject.getInt("reward");
			int lastRewardTime = jsonObject.getInt("time");
			int nextRewardDelay = jsonObject.getInt("next_delay");
			listener.onSuccess(reward, lastRewardTime, nextRewardDelay);
			return;
		    }

		    //		    if(-1 == status) { // 处于间隔时间段以内
		    //			int remainingSeconds = jsonObject.getInt("remaining_seconds");
		    //			return;
		    //		    }

		    String msg = jsonObject.getString("msg");
		    listener.onFailure(msg, status);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		//		System.out.println(new String(arg2));

		listener.onFailure("连接超时", -9);
	    }
	});
    }

    @Override
    public void uploadCrashLogs(UploadCrashLogsParams postParams, final OnUploadCrashLogsResponseListener listener) {
	// TODO Auto-generated method stub
	String url = AppConstants.URL_UPLOAD_LOGS;
	RequestParams params = new RequestParams();
	//	params.put("content", postParams.traceContent);
	try {
	    params.put("crashlog_file", postParams.file);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	    return;
	}
	HttpUtil.post(url, params, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String res = new String(arg2);
		System.out.println("上传崩溃日志文件:" + res);
		try {
		    JSONObject jsonObject = new JSONObject(res);
		    int status = jsonObject.getInt("status");
		    if (1 == status) {
			listener.onSuccess();
			return;
		    }

		    String msg = jsonObject.getString("msg");
		    listener.onFailure(msg, status);

		} catch (JSONException e) {
		    // TODO Auto-generated catch block 
		    e.printStackTrace();
		    //
		    listener.onFailure("参数错误!--" + res, -2);
		}
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		listener.onFailure("连接超时", -9);
	    }
	});
    }
}
