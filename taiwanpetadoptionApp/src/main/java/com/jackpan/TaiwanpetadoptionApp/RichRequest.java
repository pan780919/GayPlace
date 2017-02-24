///**
// *
// */
//package com.jackpan.TaiwanpetadoptionApp;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import com.google.gson.Gson;
//
//import org.apache.http.params.CoreConnectionPNames;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import static java.net.Proxy.Type.HTTP;
//
///**
// * @author Hyxen-Arthur 2015/5/4
// *
// */
//public class RichRequest {
//
////	public static String postWithoutKey(Context context, String path, String value) {
////
////		Log.d("path", path);
////		Log.d("post", value);
////
//////		String result = "";
//////		PrintWriter printWriter = null;
//////		BufferedReader bufferedReader = null;
//////
//////		if(!RichApi.checkNetwork(context)) return null;
//////
//////		URL url;
//////		try {
//////			url = new URL(path);
//////		} catch (MalformedURLException e1) {
//////			return null;
//////		}
//////
//////		try {
//////			URLConnection urlConnection = url.openConnection();
//////			urlConnection.setConnectTimeout(2000);
//////			urlConnection.setReadTimeout(5000);
//////			urlConnection.setDoOutput(true);
//////			urlConnection.setDoInput(true);
//////
//////			printWriter = new PrintWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF_8"), true);
//////			printWriter.print(value);
//////			printWriter.flush();
//////
//////			bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF_8"));
//////
//////			String line;
//////            for (;(line = bufferedReader.readLine()) != null;){
//////                result += "\n" + line;
//////            }
//////
//////            Log.e("result", result);
//////
//////            return result;
//////
//////		} catch (IOException e1) {
//////			return null;
//////		}
////
////		String networkErr = "{\"result\": {\"StateObject\": {},\"StateMessage\": \"未連接至網路\",\"StateCode\": -1}}";
////		String dataZero =  "{\"result\": {\"StateObject\": {},\"StateMessage\": \"你目前沒有資料\",\"StateCode\": -1}}";
////
////		if(RichApi.checkNetwork(context) == false) {
////			return networkErr;
////		}
////
////		HttpClient httpclient = new DefaultHttpClient();
////		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
////		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
////		HttpPost httppost = new HttpPost(path);
////
////		try {
////			httppost.setEntity(new StringEntity(value, HTTP.UTF_8));
////			HttpResponse resp = null;
////
////			try {
////				resp = httpclient.execute(httppost);
////			} catch (ClientProtocolException e) {
////				e.printStackTrace();
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////
////			if(resp == null) {
////				Log.d("return", "null");
////				return null;
////			}
////
////			HttpEntity ent = resp.getEntity();
////
////			try {
////				String respone = EntityUtils.toString(ent, HTTP.UTF_8);
////				Log.d("return", respone);
////
////				if(respone.contains("StateObject\":[]")) {
////					return dataZero;
////				}
////
////				Log.d("return-null", respone);
////
////				return respone;
////			} catch (ParseException e) {
////				e.printStackTrace();
////			} catch (IOException e) {
////				e.printStackTrace();
////			}
////
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
////
////		return null;
////
////
////	}null
//
//	public static boolean checkToken(Context context) {
//
//		String url = RichKey.KEY_URL_TOKEN;
//		ParamsTokenObj paramsToken = new ParamsTokenObj(context, TokenMethod.Check);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(paramsToken));
//		if(isJsonDataNull(content)) return false;
//
//		ResponObj respon = new Gson().fromJson(content, ResponObj.class);
//		if(!respon.isSuccess()) return false;
//
//		return true;
//	}
//
//	public static boolean generateToken(Context context) {
//		String url = RichKey.KEY_URL_TOKEN;
//		ParamsTokenObj paramsToken = new ParamsTokenObj(context, TokenMethod.Generate);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(paramsToken));
//		if(isJsonDataNull(content)) return false;
//
//		ResponObj respon = new Gson().fromJson(content, ResponObj.class);
//		if(!respon.isSuccess()) return false;
//
//		return true;
//	}
//
//	public static ResponObj driverLogin(Context context, String driverId, String password) {
//
//		String url = RichKey.KEY_URL_DRIVER;
//
////		if(RichKey.KEY_IS_TEST_MODE) {
////			driverId = "A123456789";
////			password = "123456";
////		}
//
//		ParamsLoginObj params = new ParamsLoginObj(context, driverId, password);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//		if(isJsonDataNull(content)) return null;
//
//		ResponObj respon = new Gson().fromJson(content, ResponObj.class);
//
//		if(respon.isSuccess()) {
//			RichSharedPreferences.saveDriverID(context, driverId);
//			RichSharedPreferences.saveDriverPW(context, password);
//		}
//
//		return respon;
//	}
//
//	public static boolean driverRegistPush(Context context) {
//
//		String url = RichKey.KEY_URL_DRIVER;
//		String driverId = RichSharedPreferences.getDriverID(context);
//		String registId = RichSharedPreferences.getRegistrationId(context);
//
//		if(registId.equals("")) return false;
//
//		ParamsRegistPushObj params = new ParamsRegistPushObj(context, driverId, registId);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return false;
//
//		ResponObj respon = new Gson().fromJson(content, ResponObj.class);
//		if(!respon.isSuccess()) return false;
//
////		RichSharedPreferences.saveDriverID(context, driverId);
//
//		return true;
//	}
//
//	public static String driverMethod(Context context, DriverMethod method) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsDriverObj params = new ParamsDriverObj(context, method, id);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//		if(isJsonDataNull(content)) return null;
//
//		if (method == DriverMethod.GetDriverProfile) {
//			ResponProfileObj repResponObj = new Gson().fromJson(content, ResponProfileObj.class);
//			Log.e("driverProfile", new Gson().toJson(repResponObj));
//		} else if (method == DriverMethod.GetDriverTaskRecord) {
//			ResponRecordObj record = new Gson().fromJson(content, ResponRecordObj.class);
//			Log.e("record", new Gson().toJson(record));
//		} else if (method == DriverMethod.GetDriverTaskExperience) {
//			ResponExperiencesObj experiences = new Gson().fromJson(content, ResponExperiencesObj.class);
//			Log.e("experiences", new Gson().toJson(experiences));
//		} else if (method == DriverMethod.GetDriverScore) {
//			ResponScoresObj score = new Gson().fromJson(content, ResponScoresObj.class);
//			Log.e("score", new Gson().toJson(score));
//		} else if (method == DriverMethod.GetDriverPay) {
//			ResponPayObj pay = new Gson().fromJson(content, ResponPayObj.class);
//			Log.e("pay", new Gson().toJson(pay));
//		} else if (method == DriverMethod.GetDriverOther) {
//			ResponOtherObj other = new Gson().fromJson(content, ResponOtherObj.class);
//			Log.e("other", new Gson().toJson(other));
//		} else if (method == DriverMethod.GetDriverNews) {
//			ResponNewsObj news = new Gson().fromJson(content, ResponNewsObj.class);
//			Log.e("news", new Gson().toJson(news));
//		} else if (method == DriverMethod.GetDriverPrepaid) {
//			ResponPrepaidObj prepaid = new Gson().fromJson(content, ResponPrepaidObj.class);
//			Log.e("prepaid", new Gson().toJson(prepaid));
//		}
//
//		return content;
//	}
//
//	//For getDriverTaskRecord
//	public static String driverMethodTaskRecord(Context context, DriverMethod method, String inquiryDate, int page) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsDriverObj params = new ParamsDriverObj(context, method, id, inquiryDate, page);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//		if(isJsonDataNull(content)) return null;
//
//		if (method == DriverMethod.GetDriverTaskRecord) {
//			ResponRecordObj record = new Gson().fromJson(content, ResponRecordObj.class);
//			Log.e("taskRecord", new Gson().toJson(record));
//		}
//
//		return content;
//	}
//
//	//For getDriverNearbyDriver
//	public static String driverMethodGetNearby(Context context, DriverMethod method, double lat, double lon) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsDriverObj params = new ParamsDriverObj(context, method, id, lat, lon);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//		if(isJsonDataNull(content)) return null;
//
//		return content;
//	}
//
//	public static ResponObj driverForgetPassword(Context context, String driverId) {
//
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = driverId;
//
//		ParamsDriverObj params = new ParamsDriverObj(context, DriverMethod.DriverForgetPassword, id);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//		if(isJsonDataNull(content)) return null;
//
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponProfileObj getDriverProfile(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverProfile);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponProfileObj.class);
//	}
//
//	public static ResponRecordObj getDriverTaskRecord(Context context, String inquiryDate,int page) {
//		String content = driverMethodTaskRecord(context, DriverMethod.GetDriverTaskRecord, inquiryDate, page);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponRecordObj.class);
//	}
//
//	public static ResponExperiencesObj getDriverTaskExperience(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverTaskExperience);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponExperiencesObj.class);
//	}
//
//	public static ResponScoresObj getDriverScore(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverScore);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponScoresObj.class);
//	}
//
//	public static ResponPayObj getDriverPay(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverPay);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponPayObj.class);
//	}
//
//	public static ResponOtherObj getDriverOther(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverOther);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponOtherObj.class);
//	}
//
//	public static ResponNewsObj getDriverNews(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverNews);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponNewsObj.class);
//	}
//
//	public static ResponPrepaidObj getDriverPrepaid(Context context) {
//		String content = driverMethod(context, DriverMethod.GetDriverPrepaid);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponPrepaidObj.class);
//	}
//
//	public static ResponNearbyDriverObj getDriverNearbyDriver(Context context, double driverLat, double driverLon) {
//
//		String content = driverMethodGetNearby(context, DriverMethod.GetDriverNearbyDriver, driverLat, driverLon);
//		if(content == null) return null;
//		return new Gson().fromJson(content, ResponNearbyDriverObj.class);
//	}
//
//	public static  ResponObj sendDriverState(Context context, DriverState state) {
//
//		if(state == DriverState.Online) RichSharedPreferences.updateLocation(context);
//
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//		ParamsDriverStateObj params = new ParamsDriverStateObj(context, id, state);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//		Log.d("sendDriverState","sendDriverState:"+content);
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj saveDriverProfile(Context context, DDDriverProfileObj driver) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		driver.setID(id);
//
//		ParamsDriverEditObj params = new ParamsDriverEditObj(context, driver);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	/**
//	 * Task Mach
//	 */
//
//	public static ResponObj terminateTask(Context context, String taskId) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskObj params = new ParamsTaskObj(context, TaskMethod.TerminateTask, id, taskId);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj driverReceivedTask(Context context, String taskId) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskObj params = new ParamsTaskObj(context, TaskMethod.DriverReceivedTask, id, taskId);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponRecordObj driverDecideTask(Context context, String taskId, boolean isAccept) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskIsAcceptObj params = new ParamsTaskIsAcceptObj(context, id, taskId, isAccept);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponRecordObj.class);
//	}
//
//	public static ResponObj driverTaskArrived(Context context, String taskId) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskObj params = new ParamsTaskObj(context, TaskMethod.DriverTaskArrived, id, taskId);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponRecordObj driverTaskStart(Context context, String taskId, double lat, double lon, int wait) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		//150821 Arthur Edit wait(second to minute)
////		wait = wait / 60;
//
//		ParamsTaskStartObj params = new ParamsTaskStartObj(context, id, taskId, lat, lon, wait);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponRecordObj.class);
//	}
//
//	public static ResponTaskUpdateObj updateDriverLocation(Context context, String taskId, double lat, double lon, long ts, int distance, int wait) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		//150821 Arthur Edit wait(second to minute)
////		wait = wait / 60;
//
//		ParamsTaskUpdateObj params = new ParamsTaskUpdateObj(context, id, taskId, lat, lon, ts, distance, wait);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponTaskUpdateObj.class);
//	}
//
//	public static ResponObj driverTaskPause(Context context, String taskId, double lat, double lon, int wait) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		//150821 Arthur Edit wait(second to minute)
////		wait = wait / 60;
//
//		ParamsTaskPauseObj params = new ParamsTaskPauseObj(context, TaskPauseMethod.DriverTaskPause, id, taskId, lat, lon, wait);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj driverTaskContinue(Context context, String taskId, double lat, double lon, int wait) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		//150821 Arthur Edit wait(second to minute)
////		wait = wait / 60;
//
//		ParamsTaskPauseObj params = new ParamsTaskPauseObj(context, TaskPauseMethod.DriverTaskContinue, id, taskId, lat, lon, wait);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponRecordObj driverTaskEnd(Context context, String taskId, double lat, double lon, int distance, int wait) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		//150821 Arthur Edit wait(second to minute)
////		wait = wait / 60;
//
//		ParamsTaskEndObj params = new ParamsTaskEndObj(context, id, taskId, lat, lon, distance, wait);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponRecordObj.class);
//	}
//
//	public static ResponRecordObj driverTaskUseCoupon(Context context, String taskId, String code) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskCouponObj params = new ParamsTaskCouponObj(context, id, taskId, code);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponRecordObj.class);
//	}
//
//	public static ResponObj driverTaskDone(Context context, String taskId) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskObj params = new ParamsTaskObj(context, TaskMethod.DriverTaskDone, id, taskId);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj driverTaskCancel(Context context, String taskId, String cancelReason) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskObj params = new ParamsTaskObj(context, TaskMethod.DriverTaskCancel, id, taskId, cancelReason);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj driverTaskQuery(Context context) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskObj params = new ParamsTaskObj(context, TaskMethod.DriverTaskQuery, id, null);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	//2016/03/30 Arthur
//	public static ResponObj getDriverTaskCross(Context context) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsCrossGetObj params = new ParamsCrossGetObj(context, id);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj sendDriverTaskCross(Context context, String taskId, int crossId) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsCrossSendObj params = new ParamsCrossSendObj(context, id, taskId, crossId);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponObj sendDriverCarpool(Context context, String goal, String note) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsCarpoolObj params = new ParamsCarpoolObj(context, id, goal, note, true);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	public static ResponCarpoolObj getDriverCarpool(Context context) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsCarpoolObj params = new ParamsCarpoolObj(context, id);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponCarpoolObj.class);
//	}
//
//	public static ResponObj cancelDriverCarpool(Context context) {
//		String url = RichKey.KEY_URL_DRIVER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsCarpoolObj params = new ParamsCarpoolObj(context, id, "", "", false);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponObj.class);
//	}
//
//	//Test Function
//	//For driver requestTaskMatch
//	public static ResponRecordObj requestTaskMatch(Context context) {
//		String url = RichKey.KEY_URL_USER;
//		String id = RichSharedPreferences.getDriverID(context);
//
//		ParamsTaskRequestObj params = new ParamsTaskRequestObj(context, id);
//
//		String content = postWithoutKey(context, url, new Gson().toJson(params));
//
//		if(isJsonDataNull(content)) return null;
//		return new Gson().fromJson(content, ResponRecordObj.class);
//	}
//
//	public static boolean isJsonDataNull(String content) {
////		if(content == null || content.equals("") || (content.startsWith("{") != true) || (content.contains("StateObject\":[]"))) return true;
//		if(content == null) return true;
//		if(content.equals("")) return true;
//		if(content.startsWith("{") != true) return true;
//		if(content.contains("StateObject\":[]")) return true;
//		return false;
//	}
//
//	public static String httpGetData(String url) {
//
//		String strResult = null;
//
//		try {
//			HttpGet get = new HttpGet(url);
//			HttpParams params = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(params, 5000);
//			HttpResponse httpResponse = new DefaultHttpClient(params).execute(get);
//			strResult = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return strResult;
//	}
//
//	public static boolean checkNewVersion(Context context) {
//
//		if(RichApi.checkNetwork(context) == false) return true;
//
//		String url = RichKey.KEY_URL_GOOGLEPLAY;
//		String result = httpGetData(url);
//
//		if(result == null) return true;
//
//		Pattern pattern = Pattern.compile("\"softwareVersion\"\\W*([\\d\\.]+)");
//		Matcher matcher = pattern.matcher(result);
//
//		if(matcher.find()) {
//
//			final String[] storeVer = matcher.group(1).split("\\.");
//
//			try {
//				String[] appVer = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.split("\\.");
//
//				for(int i = 0; i < appVer.length; i++) {
//					int storeCode = Integer.valueOf(storeVer[i]);
//					int appCode = Integer.valueOf(appVer[i]);
//
//					Log.e("checkVersion-"+i, "storeCode-" + storeCode + "_appCode-" + appCode);
//					if(appCode < storeCode) return false;
//				}
//
//			} catch (PackageManager.NameNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return true;
//	}
//}
