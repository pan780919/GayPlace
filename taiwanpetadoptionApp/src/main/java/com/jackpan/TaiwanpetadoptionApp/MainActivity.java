package com.jackpan.TaiwanpetadoptionApp;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.MyAPI.VersionChecker;
import com.adlocus.PushAd;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Appkey.MyAdKey;
import bolts.AppLinks;

import com.facebook.ads.*;

import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;
import com.jackpan.MyPushService;
import com.jackpan.VideoViewActivity;
import com.jackpan.video.VideoMainActivity;

public class MainActivity extends Activity {
    private ListView petlist;
    //	private ArrayAdapter<String>petadp;
    //	private List<String>listpet =new ArrayList<String>();
    private ArrayList<ResultData> mAllData = new ArrayList<ResultData>();
    private TextView numtext;
    MyAdapter mydapter = null;
    private boolean isCencel = false;
    private ProgressDialog progressDialog;

    private com.facebook.ads.AdView adView, googleads;
    private InterstitialAd interstitial;
    private Spinner mSpinner, mSpinner2;
    HashMap<String, ArrayList<ResultData>> mKind;
    HashMap<String, ArrayList<String>> mCity;
    private MyAdapter mAdapter;
    private ArrayAdapter<String> mAdapter2 = null;
    private DatabaseReference mDatabase;
    private Button mInviteBtn;
    ImageView imageView;
    private static final String TAG = "MainActivity";
    private ArrayList<GayPlace> list = new ArrayList<>();

    private AdView admobAd, admobAd2;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		 //開啟全螢幕
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	
//                             WindowManager.LayoutParams.FLAG_FULLSCREEN);	
//        //設定隱藏APP標題	
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("onAuthStateChanged", "登入:" +
                            user.getUid());
                    userUID = user.getUid();
                    MySharedPrefernces.saveUserId(MainActivity.this,userUID);
                   if(firebaseAuth.getCurrentUser().getDisplayName()!=null) MySharedPrefernces.saveUserName(MainActivity.this,firebaseAuth.getCurrentUser().getDisplayName());
                    else MySharedPrefernces.saveUserName(MainActivity.this,"沒有暱稱");
                    if(firebaseAuth.getCurrentUser().getEmail()!=null) MySharedPrefernces.saveUserMail(MainActivity.this,firebaseAuth.getCurrentUser().getEmail());
                    else MySharedPrefernces.saveUserMail(MainActivity.this,"");
                    if(firebaseAuth.getCurrentUser().getPhotoUrl()!=null)MySharedPrefernces.saveUserPic(MainActivity.this, String.valueOf(firebaseAuth.getCurrentUser().getPhotoUrl()));
                    else MySharedPrefernces.saveUserPic(MainActivity.this,"");
                } else {
                    Log.d("onAuthStateChanged", "已登出");
                    MySharedPrefernces.saveUserId(MainActivity.this,"");
                }
            }
        };
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		MyGAManager.sendScreenName(this,"搜尋頁面");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mInviteBtn = (Button) findViewById(R.id.inviteBtn);
        mInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ForIdeaAndShareActivity.class);
                startActivity(i);
//				uploadFromStream();
//				upLoad();
//				setFireBaseDB();
//				String appLinkUrl, previewImageUrl;
//				appLinkUrl = "https://play.google.com/store/apps/details?id=com.jackpan.TaipeiZoo";
//				previewImageUrl = "https://lh3.googleusercontent.com/2TPsyspPyf6WOYUEjduISOrg0HZH_xqtwa0G5LJsclL-knggHE0-KdbisjutLpr7lo8=w300-rw";
//
//				if (AppInviteDialog.canShow()) {
//					AppInviteContent content = new AppInviteContent.Builder()
//							.setApplinkUrl(appLinkUrl)
//							.setPreviewImageUrl(previewImageUrl)
//							.build();
//					AppInviteDialog.show(MainActivity.this, content);
//				}
            }
        });

        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        } else {
            AppLinkData.fetchDeferredAppLinkData(
                    getApplication(),
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            //process applink data
                        }
                    });
        }
        progressDialog = ProgressDialog.show(MainActivity.this, "讀取中", "目前資料量比較龐大，請耐心等候！！", false, false, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                //
                isCencel = true;
                finish();
            }
        });
        configVersionCheck();

        boolean isbuy = MySharedPrefernces.getIsBuyed(this);
        if (isbuy) {
            Intent promotionIntent = new Intent(this, MainActivity.class);
            PushAd.enablePush(this, MyAdKey.AdLoucskey, promotionIntent);
        } else {
            PushAd.disablePush(MainActivity.this);
        }
//		FirebaseMessaging.getInstance().subscribeToTopic("news");
//
//
//		mDatabase = FirebaseDatabase.getInstance().getReference();
//		final String userId = "123456";
//		mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
//				new ValueEventListener() {
//					@Override
//					public void onDataChange(DataSnapshot dataSnapshot) {
//						// Get user value
//						User user = dataSnapshot.getValue(User.class);
//
//						// [START_EXCLUDE]
//						if (user == null) {
//							// User is null, error out
//							Log.e("Jack", "User " + userId + " is unexpectedly null");
//							Toast.makeText(MainActivity.this,
//									"Error: could not fetch user.",
//									Toast.LENGTH_SHORT).show();
//						} else {
//							// Write new post
//							writeNewPost(userId, user.username, "name", "message");
//						}
//
//						// Finish this Activity, back to the stream
//						finish();
//						// [END_EXCLUDE]
//					}
//
//					@Override
//					public void onCancelled(DatabaseError databaseError) {
//						Log.d("Jack", "getUser:onCancelled", databaseError.toException());
//					}
//				});

//		PushAd.test(this);

        //		numtext= (TextView) findViewById(R.id.textView1);
        //		numtext.setVisibility(View.GONE);
        petlist = (ListView) findViewById(R.id.listView1);
        //		petadp= new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,android.R.id.text1);

        petlist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent petint = new Intent(MainActivity.this, TwoActivity.class);
                petint.putExtra("json", new Gson().toJson(mAdapter.mDatas.get(position)));

                startActivity(petint);


            }
        });


        mAdapter = new MyAdapter(list);

        petlist.setAdapter(mAdapter);
//
//		LoadNetAsyncTask loadNetAsyncTask = new LoadNetAsyncTask();
//		loadNetAsyncTask.execute(MyAdKey.jsondata);
//		MobileAds.initialize(this, "ca-app-pub-7019441527375550~9403733429");
//		adapterWrapper = new AdmobAdapterWrapper(this);
//		String admobUnitId =getResources().getString(R.string.admob_unit_id);
//
//		adapterWrapper.setAdmobReleaseUnitId(admobUnitId);
//		adapterWrapper.setAdapter(mAdapter);
//		adapterWrapper.setLimitOfAds(3);
//		adapterWrapper.setNoOfDataBetweenAds(3);
//
//		petlist.setAdapter(adapterWrapper);
//		mAdapter.notifyDataSetChanged();
//		initUpdateAdsTimer();

        setFireBase();
		setAdMobAd();
        setFbAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushManager.getInstance().initialize(MainActivity.this, MyPushService.class);
        PushManager.getInstance().registerPushIntentService(MainActivity.this, MyIntentService.class);
    }


    private void setFbAd() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MyAdKey.AdmobinterstitialBannerId);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
//        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
//
//        adView = new com.facebook.ads.AdView(this, "583698071813390_587400221443175", AdSize.BANNER_320_50);
//        adViewContainer.addView(adView);
//        adView.loadAd();

        RelativeLayout adViewContainer2 = (RelativeLayout) findViewById(R.id.adViewContainer2);
        googleads = new com.facebook.ads.AdView(this, "583698071813390_587400221443175", AdSize.BANNER_320_50);
        adViewContainer2.addView(googleads);
        googleads.loadAd();

    }

    private void setAdMobAd() {
        admobAd = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        admobAd.loadAd(adRequest);

//        admobAd2 = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest2 = new AdRequest.Builder().build();
//        admobAd2.loadAd(adRequest2);


    }

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private class LoadNetAsyncTask extends AsyncTask<String, Void, ArrayList<ResultData>> {

        @Override
        protected void onPostExecute(final ArrayList<ResultData> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result == null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("出錯囉!!")
                        .setMessage("很抱歉，系統暫時無法提供服務。請您稍後再試～")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
//						interstitial.show();
                            }
                        }).show();
                return;

            }

            final ArrayList<String> kindStrings = new ArrayList<String>(mCity.keySet());


            String id = kindStrings.toString().substring(0, kindStrings.toString().length());

            kindStrings.add(0, "全部");

            ArrayAdapter<String> animalKindSpinner = new
                    ArrayAdapter<String>(MainActivity.this, R.layout.myspinnerlayout, kindStrings);
            mAdapter2 = new ArrayAdapter<String>(MainActivity.this, R.layout.myspinnerlayout, new ArrayList<String>());


            animalKindSpinner.setDropDownViewResource(R.layout.myspinnerlayout);
            mAdapter2.setDropDownViewResource(R.layout.myspinnerlayout);
            mSpinner.setAdapter(animalKindSpinner);
            mSpinner2.setAdapter(mAdapter2);


            mSpinner.setOnItemSelectedListener(new
                                                       AdapterView.OnItemSelectedListener() {
                                                           @Override
                                                           public void onItemSelected(AdapterView<?> parent, View view, int
                                                                   position, long id) {
//					if (position == 0) {
//						mAdapter.updateData(mAllData);
//						mSpinner2.setVisibility(View.GONE);
//					} else {
//						selectSpinner(kindStrings.get(position));
//						mSpinner2.setVisibility(View.VISIBLE);
//					}
                                                           }

                                                           @Override
                                                           public void onNothingSelected(AdapterView<?> parent) {
//					mAdapter.updateData(mAllData);
                                                           }
                                                       });
            mSpinner2.setOnItemSelectedListener(new
                                                        AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int
                                                                    position, long id) {

                                                                String city = (String) mSpinner.getSelectedItem();
                                                                String township = (String) mSpinner2.getSelectedItem();

                                                                selectSpinner2(city + "," + township);
                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {
//					mAdapter2.updateData(mAllData);
                                                            }
                                                        });

            mAllData = result;
//			mAdapter.updateData(mAllData);

        }

        @Override
        protected ArrayList<ResultData> doInBackground(String... params) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpUrlCon =
                        (HttpURLConnection) url.openConnection();
                httpUrlCon.setConnectTimeout(20000);//連線
                httpUrlCon.setReadTimeout(20000);//讀取

                InputStream inputStream = httpUrlCon.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                br = new BufferedReader(inputStreamReader);
                String value = null;

                while ((value = br.readLine()) != null) {
                    sb.append(value);
                }
                String result = sb.toString();

                ArrayList<ResultData> allData = new ArrayList<ResultData>();
                mKind = new HashMap<String, ArrayList<ResultData>>();//city
                mCity = new HashMap<String, ArrayList<String>>();
                try {

//					JSONArray jsonarry = new JSONArray(result);
                    JSONObject o = new JSONObject(result);
                    JSONObject resultObj = o.getJSONObject("result");
                    JSONArray jsonarry = resultObj.getJSONArray("results");
                    for (int i = 0; i < jsonarry.length(); i++) {
                        JSONObject jsonObject = jsonarry.getJSONObject(i);
                        Gson gson = new Gson();
                        ResultData data = gson.fromJson(jsonObject.toString(), ResultData.class);
                        String key = data.A_Location + "," + data.A_Name_Ch;
                        ArrayList<ResultData> animalKind = mKind.get(key);
                        if (animalKind == null) {
                            animalKind = new ArrayList<ResultData>();

                        }
                        mKind.put(key, animalKind);

                        animalKind.add(data);


                        ArrayList<String> towmShip = mCity.get(data.A_Location);
                        if (towmShip == null) {
                            towmShip = new ArrayList<String>();

                        }
                        mCity.put(data.A_Location, towmShip);
                        if (!towmShip.contains(data.A_Name_Ch)) towmShip.add(data.A_Name_Ch);

//						data.startTime = MyApi.getTime(data.animal_opendate);

                        allData.add(data);

                    }
//					Collections.sort(allData);


                } catch (JSONException e) {
                }

                return allData;
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } finally {

                try {
                    if (br != null) br.close();
                } catch (IOException e) {
                }
            }

            return null;

        }


    }

    public void selectSpinner(String kinds) {
        ArrayList<String> kindList = mCity.get(kinds);
        mAdapter2.clear();

        mAdapter2.addAll(kindList);

    }

    public void selectSpinner2(String kinds) {

        ArrayList<ResultData> kindList = mKind.get(kinds);

//		mAdapter.updateData(kindList);

    }

    public class MyAdapter extends BaseAdapter {
        //		private ArrayList<ResultData> mDatas;
        private ArrayList<GayPlace> mDatas;

        public MyAdapter(ArrayList<GayPlace> datas) {
            mDatas = datas;
        }

        public void updateData(ArrayList<GayPlace> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.mylayout, null);
//			ResultData data = mDatas.get(position);
            GayPlace taipeiZoo = mDatas.get(position);
            TextView textname = (TextView) convertView.findViewById(R.id.name);
            TextView list = (TextView) convertView.findViewById(R.id.txtengname);
            TextView bigtext = (TextView) convertView.findViewById(R.id.bigtext);
            bigtext.setVisibility(View.GONE);
            TextView place = (TextView) convertView.findViewById(R.id.palace);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView  userview = (TextView) convertView.findViewById(R.id.view);
            TextView  userlike = (TextView) convertView.findViewById(R.id.like);

            textname.setText(taipeiZoo.getTittle());
            list.setText("作者:" +taipeiZoo.getName());
            time.setText("發文時間:"+taipeiZoo.getDate());
//            bigtext.setVisibility(View.GONE);
            place.setVisibility(View.VISIBLE);
            place.setText("ID:"+taipeiZoo.getId());
            if(taipeiZoo.getView()==-1){
                userview.setText("觀看人數:"+0);
            }else {
                userview.setText("觀看人數:"+taipeiZoo.view);
            }
            if (taipeiZoo.getLike()==-1){
                userlike.setText("喜歡人數:"+0);
            }else {
                userlike.setText("喜歡人數:"+taipeiZoo.like);
            }




//            time.setVisibility(View.GONE);
//			place.setText("英文名:"+taipeiZoo.getAge());
//			time.setText("地理分布:"+data.A_Distribution );
            imageView = (ImageView) convertView.findViewById(R.id.photoimg);
            //			loadImage(data.album_file, img);
            //			Glide.with(MainActivity.this).load(data.album_file).into(imageView);

            Glide.with(MainActivity.this)
                    .load(taipeiZoo.getPic())
                    .centerCrop()
                    .placeholder(R.drawable.nophoto)
                    .crossFade()
                    .into(imageView);
            return convertView;
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

            ConfirmExit(); //呼叫ConfirmExit()函數

            return true;

        }

        return super.onKeyDown(keyCode, event);

    }


    public void ConfirmExit() {

        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this); //創建訊息方塊

        ad.setTitle("離開");

        ad.setMessage("確定要登出帳號?");

        ad.setPositiveButton("登出", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {


                MainActivity.this.finish();//關閉activity
                auth.signOut();
                MySharedPrefernces.saveUserId(MainActivity.this,"");
                interstitial.show();

            }

        });

        ad.setNegativeButton("不用", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

                MainActivity.this.finish();//關閉activity
                interstitial.show();
            }

        });

        ad.show();//顯示訊息視窗

    }


    private void loadImage(final String path,
                           final ImageView imageView) {

        new Thread() {

            @Override
            public void run() {

                try {
                    URL imageUrl = new URL(path);
                    HttpURLConnection httpCon =
                            (HttpURLConnection) imageUrl.openConnection();
                    InputStream imageStr = httpCon.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStr);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            imageView.setImageBitmap(bitmap);
                        }
                    });


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    Log.e("Howard", "MalformedURLException:" + e);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.e("Howard", "IOException:" + e);
                }


            }


        }.start();

    }

    public class User {

        public String username;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }

    @Override
    protected void onDestroy() {
//		adView.destroy();

        super.onDestroy();
    }

    private void configVersionCheck() {

//        if (!GtApi.checkNetwork(IndexActivity.this)) return;

        VersionChecker.checkOnce(MainActivity.this, new VersionChecker.DoneAdapter() {

            @Override
            public void onHasNewVersion() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("已有最新版本!")
                        .setMessage("本次更新內容：\n\n" +
                                "修正 觀看人數 按讚 留言板問題！！")
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(VersionChecker.openMartketIntent());
                                dialog.dismiss();
                            }
                        })
                        .show();
            }


        });

    }

    private void setFireBase() {
        Firebase.setAndroidContext(this);
        String url = "https://sevenpeoplebook.firebaseio.com/GayPlace";

        Firebase mFirebaseRef = new Firebase(url);


//		if(Firebase.getDefaultConfig().isPersistenceEnabled()==false)mFirebaseRef.getDefaultConfig().setPersistenceEnabled(true);
        mFirebaseRef.orderByChild("date").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue().toString());
//				Log.d(TAG, "onChildAdded: "+ (String) dataSnapshot.child("tittle").getValue());
//				Log.d(TAG, "onChildAdded: "+ (Long) dataSnapshot.child("message").getValue());
//				TaipeiZoo taipeiZoo = new TaipeiZoo();
//				taipeiZoo.setName((String)dataSnapshot.child("name").getValue());
//                for (DataSnapshot alert: dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onChildAdded: "+alert.getValue().toString());
//                    list.add(0,gayPlace);
////                    for (DataSnapshot recipient: alert.child("formsg").getChildren()) {
////                        Log.d(TAG, "onChildAdded: "+recipient.getValue().toString());
////                    }
//
                GayPlace gayPlace = dataSnapshot.getValue(GayPlace.class);
                list.add(0,gayPlace);


                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();







            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: "+"onChildChanged");

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


    }

    private void setFireBaseDB(String uri) {
        String url = "https://sevenpeoplebook.firebaseio.com/GayPlace";
        Firebase mFirebaseRef = new Firebase(url);
//		Firebase userRef = mFirebaseRef.child("user");
//		Map newUserData = new HashMap();
//		newUserData.put("age", 30);
//		newUserData.put("city", "Provo, UT");
        Firebase newPostRef = mFirebaseRef.child("posts").push();
        String newPostKey = newPostRef.getKey();
        Log.d(TAG, "setFireBaseDB: " + newPostKey);
        Map newPost = new HashMap();
        newPost.put("tittle", "hello");
        newPost.put("message", 21);
        newPost.put("pic", uri);
        Map updatedUserData = new HashMap();
//		updatedUserData.put("3/posts/" + newPostKey, true);
        updatedUserData.put(newPostKey, newPost);
        mFirebaseRef.updateChildren(updatedUserData, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "onComplete: " + "Error updating data: " + firebaseError.getMessage());
                }
            }
        });
    }

    private void upLoad() {

//				sharePicWithUri(uri);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sevenpeoplebook.appspot.com");
        StorageReference mountainsRef = storageRef.child("file.jpg");
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }


    //縮放照片
    private void ScalePic(Bitmap bitmap, int phone) {
        //縮放比例預設為1
        float mScale = 1;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if (bitmap.getWidth() > phone) {
            //判斷縮放比例
            mScale = (float) phone / (float) bitmap.getWidth();

            Matrix mMat = new Matrix();
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
//			mImg.setImageBitmap(mScaleBitmap);
        }
//		else mImg.setImageBitmap(bitmap);
    }

    //儲存圖片
    public Uri savePicture(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();
        String fname = "temp.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_user){
            startActivity(new Intent(MainActivity.this,UserActivity.class));
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,InAppBillingActivity.class));

            return true;
        }
        if (id== R.id.action_video){
            startActivity(new Intent(MainActivity.this, VideoMainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}