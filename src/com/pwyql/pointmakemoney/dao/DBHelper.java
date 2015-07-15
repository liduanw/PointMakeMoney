package com.pwyql.pointmakemoney.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ClassName:DBHelper
 * @author sparklee liduanwei_911@163.com
 * @date Nov 7, 2014 7:06:36 PM
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    public static DBHelper getInstance(Context context, String name, CursorFactory factory, int version) {
	if (null == instance) {
	    instance = new DBHelper(context, name, factory, version);
	}
	return instance;
    }

    public static final String DB_NAME = "db_wei_pointmm3.db";
    public static final String TABLE_USER = "t_wei_user";
    // 本地已注册用户ID,手机号,密码,最后登录/注册时间,创建时间
    static final String CREATE_TABLE_USER = "create table t_wei_user(id integer primary key autoincrement,user_id varchar not null unique,user_phone varchar(11) not null unique ,user_pwd char(20) not null, last_time integer,create_time integer)";

    private DBHelper(Context context, String name, CursorFactory factory, int version) {
	super(context, name, factory, version);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(CREATE_TABLE_USER);

	//	db.execSQL(CREATE_TABLE_EDITOR_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub

	String sql = "drop table t_wei_user";
	getWritableDatabase().execSQL(sql);
	//	String sql2 = "drop table tb_wei_user_editor_new";
	//	getWritableDatabase().execSQL(sql2);

    }

    /**
     * 清空所有数据
     */
    public void deleteAllTables() {
	// 不支持 truncate?
	String sql2 = "delete from tb_wei_user";
	getWritableDatabase().execSQL(sql2);
    }

    /**
     * 添加注册成功的用户
     * @param userId
     * @param password
     * @param nickname
     */
    public void insertUser(String userId, String password) {
	String sql = "insert into t_wei_user(user_id,user_pwd) VALUES('{USER_ID)','{USER_PWD}')".replace("{USER_ID}", userId + "").replace("{USER_PWD}", password);
	getWritableDatabase().execSQL(sql);
    }
}
