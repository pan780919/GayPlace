/**
 * 
 */
package com.jackpan.TaiwanpetadoptionApp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author Hyxen-Arthur 2015/5/5
 *
 */
public class ParamsObj {

	private String method;
	private int id = 222;
	public Params params = new Params();
	
	public static class Params {
		private String DeviceID;
		private String OSVer;
		private String DeviceType;
		private String AppVer;
	}
	
	public ParamsObj(Context context, String method, Params params) {
		this.params = params;
		setMethod(method);
		initDeviceParams(context);
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public void initDeviceParams(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
		this.params.DeviceID = telephonyManager.getDeviceId();
		//New Version
		this.params.OSVer = "Android " + Build.VERSION.RELEASE;
		this.params.DeviceType = Build.MODEL;
		//Old Version
//		this.params.OSVer = android.os.Build.VERSION.RELEASE;
//		this.params.DeviceType = "Android";

		try {
			this.params.AppVer = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
