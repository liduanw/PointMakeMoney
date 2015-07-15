package com.pwyql.pointmakemoney.util;

import java.security.MessageDigest;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

public class ApkSign {
    private static final String APP_PACKAGE_NAME = "com.pwyql.pointmakemoney";

    public static String getAPPSecretString(Activity activity) {
	String backString = "";
	try {
	    PackageInfo mPackageInfo = activity.getPackageManager().getPackageInfo(APP_PACKAGE_NAME, 64);
	    Signature xx[] = mPackageInfo.signatures;
	    byte[] arrayOfByte = mPackageInfo.signatures[0].toByteArray();
	    backString = digest(arrayOfByte);
	} catch (NameNotFoundException e1) {
	    e1.printStackTrace();
	}
	return backString;
    }

    private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static final String digest(String message) {
	try {
	    byte[] strTemp = message.getBytes();
	    MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	    mdTemp.update(strTemp);
	    byte[] md = mdTemp.digest();
	    int j = md.length;
	    char str[] = new char[j * 2];
	    int k = 0;
	    for (int i = 0; i < j; i++) {
		byte byte0 = md[i];
		str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		str[k++] = hexDigits[byte0 & 0xf];
	    }
	    return new String(str);
	} catch (Exception e) {
	    return null;
	}
    }

    public static final String digest(byte[] strTemp) {
	try {
	    // byte[] strTemp = message.getBytes();
	    MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	    mdTemp.update(strTemp);
	    byte[] md = mdTemp.digest();
	    int j = md.length;
	    char str[] = new char[j * 2];
	    int k = 0;
	    for (int i = 0; i < j; i++) {
		byte byte0 = md[i];
		str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		str[k++] = hexDigits[byte0 & 0xf];
	    }
	    return new String(str);
	} catch (Exception e) {
	    return null;
	}
    }
}
