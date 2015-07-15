package com.pwyql.pointmakemoney.dao.impl;

import android.content.Context;
import android.database.Cursor;

import com.pwyql.pointmakemoney.dao.DBHelper;
import com.pwyql.pointmakemoney.dao.UserDao;

public class UserDaoImpl implements UserDao {
    private static UserDaoImpl instance;

    private Context context;

    private DBHelper dbHelper;

    private UserDaoImpl(Context context) {
	this.context = context;
	dbHelper = DBHelper.getInstance(context, DBHelper.DB_NAME, null, 1);

    }

    public static UserDao getInstance(Context context) {
	if (null == instance) {
	    instance = new UserDaoImpl(context);
	}

	return instance;
    }

    @Override
    public void findUser(String userId) {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isEmpty() {
	// TODO Auto-generated method stub
	//	String[] columns = null;
	//	String selection = null;//"user_id=?";
	//	String[] selectionArgs = null;//new String[] {};
	//	String groupBy = null;
	//	String having = null;
	//	String orderBy = null;
	//	String limit = "";
	//Cursor cursor = dbHelper.getReadableDatabase().query(DBHelper.TABLE_USER, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	String sql = "select * FROM " + DBHelper.TABLE_USER + " limit 0,1";
	Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);

	if (cursor == null) {
	    return true;
	}

	if (cursor.moveToFirst()) {
	    String userId = cursor.getString(cursor.getColumnIndex("user_id"));
	    cursor.close();
	    return userId == null;
	}

	cursor.close();
	return true;
    }

    @Override
    public void insertUser(String userId, String phone, String pwd, int lastTime, int createTime) {
	// TODO Auto-generated method stub
	String sql = "INSERT INTO " + DBHelper.TABLE_USER + "(user_id,user_phone,user_pwd,last_time,create_time) VALUES(?,?,?,?,?)";
	dbHelper.getWritableDatabase().execSQL(sql, new String[] { userId, phone, pwd, lastTime + "", createTime + "" });
    }

    @Override
    public RegedUser findLastUser() {
	// TODO Auto-generated method stub
	String sql = "SELECT * FROM " + DBHelper.TABLE_USER + " ORDER BY id";
	String[] selectionArgs = new String[] {};
	dbHelper.getReadableDatabase().rawQuery(sql, selectionArgs);
	return null;
    }

    @Override
    public void updateUser(String phone, String pwd, int lastTime, String whereUserId) {
	// TODO Auto-generated method stub

	String sql = "UPDATE " + DBHelper.TABLE_USER + " SET phone=?,pwd=?,last_time=? WHERE user_id=?";
	dbHelper.getWritableDatabase().execSQL(sql, new String[] { phone, pwd, lastTime + "", whereUserId });

    }
}
