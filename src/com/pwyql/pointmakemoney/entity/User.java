package com.pwyql.pointmakemoney.entity;

public class User {
    private String userId;
    private int points;

    private String inviteCode; // 个人邀请码, 接受推广成功奖励(新用户注册)

    private boolean hasCashpwd;// 是否已设置提现密码

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public int getPoints() {
	return points;
    }

    public void setPoints(int points) {
	this.points = points;
    }

    public String getInviteCode() {
	return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
	this.inviteCode = inviteCode;
    }

    public boolean isHasCashpwd() {
	return hasCashpwd;
    }

    public void setHasCashpwd(boolean hasCashpwd) {
	this.hasCashpwd = hasCashpwd;
    }
}
