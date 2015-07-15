package com.pwyql.pointmakemoney.dao;


public interface UserDao {

    void insertUser(String userId, String phone, String pwd, int lastTime, int createTime);

    void updateUser(String phone, String pwd, int lastTime, String whereUserId);

    void findUser(String userId);

    RegedUser findLastUser();

    boolean isEmpty();

    public static class RegedUser {
	public String userId, phoneNumber, pwd;
    }

}
