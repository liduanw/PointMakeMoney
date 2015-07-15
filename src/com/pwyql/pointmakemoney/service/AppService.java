package com.pwyql.pointmakemoney.service;

import java.io.File;
import java.util.List;

import com.pwyql.pointmakemoney.adapter.GoodsAdapter.Goods;
import com.pwyql.pointmakemoney.adapter.WordsAdapter.Word;
import com.pwyql.pointmakemoney.entity.User;

public interface AppService {
    /**
     * 注册
     * @param phoneNumber
     * @param pwd
     * @param sign
     * @param listener
     */
    void reg(RegParams postParams, OnRegResponseListener listener);

    public static class RegParams {
	public String phoneNumber;
	public String invitecode;

	public String model;

	public String phoneNumberToken;
    }

    public static interface OnRegResponseListener {
	void onSuccess(String userId, String phone, String pwd);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 登录
     * @param postParams
     * @param listener
     */
    void login(LoginParams postParams, OnLoginResponseListener listener);

    public static class LoginParams {
	public String phoneNumber;
	public String pwd;

	public String model;
	public String sign;

	public String version;
    }

    public static interface OnLoginResponseListener {
	void onSuccess(User user, int rate);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 查询当前用户的积分
     * @param listener
     */
    void points(OnPointsResponseListener listener);

    public static interface OnPointsResponseListener {
	void onSuccess(int points);

	void onFailure(String errorMsg, int status);
    }

    /**验证手机短信验证码
     * 
     * @param postParams
     * @param listener
     */
    void verifyPhoneCode(VerifyPhoneCodeParams postParams, OnVerifyPhoneCodeResponseListener listener);

    public static class VerifyPhoneCodeParams {
	public String appKey, phone, zone, code;
    }

    public static interface OnVerifyPhoneCodeResponseListener {
	void onSuccess(String token);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 重设登录密码
     * @param postParams
     * @param listener
     */
    void resetLoginPassword(ResetLoginPwdParams postParams, OnResetpwdResponseListener listener);

    public static class ResetLoginPwdParams {
	public String phoneNumber, password, token;
    }

    public static interface OnResetpwdResponseListener {
	void onSuccess();

	void onFailure(String errorMsg, int status);
    }

    /**
     * 重设提现密码
     * @param postParams
     * @param listener
     */
    void resetCashPassword(ResetCashPwdParams postParams, OnResetcashResponseListener listener);

    public static class ResetCashPwdParams {
	public String phoneNumber, password, token;
    }

    public static interface OnResetcashResponseListener {
	void onSuccess();

	void onFailure(String errorMsg, int status);
    }

    /**
     * 检查更新
     * @param postParams
     * @param listener
     */
    void checkUpdate(CheckUpdateParams postParams, OnCheckUpdateResponseListener listener);

    public static class CheckUpdateParams {
	public int currentVersionCode;
    }

    public static interface OnCheckUpdateResponseListener {
	void onSuccess(NewestAppInfo app);

	void onFailure(String errorMsg, int status);
    }

    public static class NewestAppInfo implements java.io.Serializable {
	public String versionName;
	public int size;
	public String instruction;// 说明
	public String url;
    }

    /**
     * 发布留言
     * @param postParams
     * @param listener
     */
    void publishWord(PublishWordParams postParams, OnPublishWordResponseListener listener);

    public static class PublishWordParams {
	public String content;
	public String sign;
    }

    public static interface OnPublishWordResponseListener {
	void onSuccess(Word word);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 查询留言
     * @param postParams
     * @param listener
     */
    void selectWords(SelectWordsParams postParams, OnSelectWordsResponseListener listener);

    public static class SelectWordsParams {
	public int page, pageSize;
    }

    public static interface OnSelectWordsResponseListener {
	void onSuccess(List<Word> words);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 提现
     * @param postParams
     * @param listener
     */
    void withdraw(WithdrawParams postParams, OnWithdrawResponseListener listener);

    public static class WithdrawParams {
	public int type;
	public int priceType;
	public String accountNumber;
	public String cashPwd;
	public String sign;
    }

    public static interface OnWithdrawResponseListener {
	void onSuccess(WithdrawOrder order);

	void onFailure(String errorMsg, int status);
    }

    public static class WithdrawOrder {
	public String id;
    }

    /**
     * 加载当前登录用户的一些信息
     * @param postParams
     * @param listener
     */
    void loadInfo(LoadInfoParams postParams, OnLoadInfoResponseListener listener);

    public static class LoadInfoParams {
	public String sign;
    }

    public static interface OnLoadInfoResponseListener {
	void onSuccess(Info info);

	void onFailure(String errorMsg, int status);
    }

    public static class Info {
	public String name;
	public String qq;
	public String alipayAccount,alipayName;
	public String phone;
	public int points;

	public String receAddr, receName, recePhone;
    }

    /**
     * 加载商品
     * @param postParams
     * @param listener
     */
    void loadGoods(LoadGoodsParams postParams, OnLoadGoodsResponseListener listener);

    public static class LoadGoodsParams {
	public int page, size;
	public String sign;
    }

    public static interface OnLoadGoodsResponseListener {
	void onSuccess(List<Goods> goodsList);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 加载商品详情
     * @param postParams
     * @param listener
     */
    void loadGoodsDetail(LoadGoodsDetailParams postParams, OnLoadGoodsDetailResponseListener listener);

    public static class LoadGoodsDetailParams {
	public String goodsId;
	public String sign;
    }

    public static interface OnLoadGoodsDetailResponseListener {
	void onSuccess(GoodsDetail detail);

	void onFailure(String errorMsg, int status);
    }

    public static class GoodsDetail {
	public String image01, image02, image03;
	public String description;
	public int quantity;
	public int price;
    }

    /**
     * 兑换商品
     * @param postParams
     * @param listener
     */
    void exchange(ExchangeParams postParams, OnExchangeResponseListener listener);

    public static class ExchangeParams {
	public String goodsId;
	public String receAddr;//收货地址
	public String receName;//收货人
	public String recePhone;//收货人手机号
	public String cashPwd;
	public String sign;
    }

    public static interface OnExchangeResponseListener {
	void onSuccess(String orderId);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 点击广告条时请求奖励
     * @param postParams
     * @param listener
     */
    void adReward(AdRewardParams postParams, OnAdRewardResponseListener listener);

    public static class AdRewardParams {
	public String model;
	public String time;
	public String sign;
    }

    public static interface OnAdRewardResponseListener {
	/**
	 *  
	 * @param reward 奖励积分数
	 * @param lastRewardTime 本次领取奖励的时间(秒)
	 * @param nextRewardDelay 下次领取间隔时间(秒)
	 */
	void onSuccess(int reward, int lastRewardTime, int nextRewardDelay);

	void onFailure(String errorMsg, int status);
    }

    /**
     * 上传程序崩溃日志到服务器
     * @param postParams
     * @param listener
     */
    void uploadCrashLogs(UploadCrashLogsParams postParams, OnUploadCrashLogsResponseListener listener);

    public static class UploadCrashLogsParams {
	//	public String model;
	//	public String versionName, versionCode;
	//	public String os;
	//	public String traceContent;

	public File file;

    }

    public static interface OnUploadCrashLogsResponseListener {
	void onSuccess();

	void onFailure(String errorMsg, int status);
    }
}
