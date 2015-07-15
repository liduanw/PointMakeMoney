package com.pwyql.pointmakemoney.service;

import com.pwyql.pointmakemoney.MyApplication;

public class AppConstants {
    public static final String HOST = "http://pointmm.pwyql.com";

    // 注册
    public static final String URL_REGISTER = HOST + "/index.php/register/reg";
    // 登录
    public static final String URL_LOGIN = HOST + "/index.php/login/login";
    // 查询积分
    public static final String URL_POINTS = HOST + "/index.php/user/points";
    // 个人资料
    public static final String URL_PROFILE = HOST + "/index.php/user/profile";
    // 个人资料
    public static final String URL_INCOMDE_DETAIL = HOST + "/index.php/user/incomedetail";
    // 提兑记录
    public static final String URL_WITHDRAW_LOGS = HOST + "/index.php/withdraw/logs";
    // 话费充值(菜单页)
    public static final String URL_TELFARE = HOST + "/index.php/withdraw/menutelfare";
    // Q币充值(菜单页)
    public static final String URL_Q = HOST + "/index.php/withdraw/menuq";
    // 支付宝(菜单页)
    public static final String URL_ALIPAY = HOST + "/index.php/withdraw/menualipay";
    // 财付通(菜单页)
    public static final String URL_TENPAY = HOST + "/index.php/withdraw/menutenpay";
    // 下线用户
    public static final String URL_MYUSERS = HOST + "/index.php/user/myusers";
    // 总积分排名
    public static final String URL_RANKINGUSERS = HOST + "/index.php/user/rankingusers";
    // 今日积分排名
    public static final String URL_RANKING_TODAY = HOST + "/index.php/user/rankingtoday";

    // 提兑展示
    public static final String URL_RANKINGWITHDRAW_LOGS = HOST + "/index.php/withdraw/otherslogs";

    // 使用协议
    public static final String URL_SYSTEM_LICENSE = HOST + "/index.php/system/license";
    // 系统通知
    public static final String URL_SYSTEM_NOTICES = HOST + "/index.php/system/notices";
    // 帮助
    public static final String URL_SYSTEM_HELP = HOST + "/index.php/system/help";

    // 重置登录密码
    public static final String URL_RESET_PWD_LOGIN = HOST + "/index.php/resetpwd/resetlogin";
    // 重置提现密码
    public static final String URL_RESET_PWD_CASH = HOST + "/index.php/resetpwd/resetcash";

    // 手机短信验证
    public static final String URL_SMSVERIFY_CODE = HOST + "/index.php/sms/verify";

    // 检查新版本
    public static final String URL_CHECK_VERSION = HOST + "/index.php/app/checkupdate";

    // 发布留言
    public static final String URL_PUBLISH_WORD = HOST + "/index.php/words/publish";
    // 查询留言
    public static final String URL_WORDS = HOST + "/index.php/words/select";

    // 提现
    public static final String URL_WITHDRAW = HOST + "/index.php/withdraw/withdraw";
    // 信息
    public static final String URL_USERINFO = HOST + "/index.php/user/info";

    // 兑换商城
    public static final String URL_MALL = HOST + "/index.php/mall/index";
    // 加载商品列表
    public static final String URL_MALL_GOODSLIST = HOST + "/index.php/mall/select";

    // 加载商品详情
    public static final String URL_MALL_GOODSDETAIL = HOST + "/index.php/mall/detail";
    
    // 加载商品详情(具体描述
    public static final String URL_MALL_GOODSDETAIL_DESCRIPTION = HOST + "/index.php/goods/desc";

    // 兑换商品
    public static final String URL_MALL_EXCHANGE = HOST + "/index.php/mall/exchange";

    // 点击广告条请求奖励
    public static final String URL_AD_REWARD = HOST + "/index.php/ad/reward";

    // app 下载地址
    public static final String URL_APP = HOST + "/index.php/app/download?invcode=" + MyApplication.getInstance().getCurrentLoginedUser().getInviteCode();

    // app 官网
    public static final String URL_APP_WEBSITE = HOST;

    // 崩溃日志上传url
    public static final String URL_UPLOAD_LOGS = HOST + "/index.php/logs/upload";

    // 备份联系人
    public static final String URL_BACKUP_CONTACTS = HOST + "/index.php/b/c";

    // signKEY
    public static final String SIGN_KEY = "liduanwei_911@163.com";
    // mob appKEY
    public static final String MOB_APP_KEY = "77b8007b3cea";

}
