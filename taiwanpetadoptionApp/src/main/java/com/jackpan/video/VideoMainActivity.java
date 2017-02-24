package com.jackpan.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.MyAPI.VersionChecker;
import com.adlocus.PushAd;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.jackpan.MyPushService;
import com.jackpan.TaiwanpetadoptionApp.ForIdeaAndShareActivity;
import com.jackpan.TaiwanpetadoptionApp.GayPlace;
import com.jackpan.TaiwanpetadoptionApp.InAppBillingActivity;
import com.jackpan.TaiwanpetadoptionApp.MainActivity;
import com.jackpan.TaiwanpetadoptionApp.MyIntentService;
import com.jackpan.TaiwanpetadoptionApp.MySharedPrefernces;
import com.jackpan.TaiwanpetadoptionApp.Post;
import com.jackpan.TaiwanpetadoptionApp.R;
import com.jackpan.TaiwanpetadoptionApp.ResultData;
import com.jackpan.TaiwanpetadoptionApp.TwoActivity;
import com.jackpan.TaiwanpetadoptionApp.UserActivity;
import com.jackpan.VideoViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Appkey.MyAdKey;
import bolts.AppLinks;

public class VideoMainActivity extends Activity {
    private ListView petlist;

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
    private ArrayList<GayVideo> list = new ArrayList<>();

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
        setContentView(R.layout.activity_main_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		MyGAManager.sendScreenName(this,"搜尋頁面");

        progressDialog = ProgressDialog.show(VideoMainActivity.this, "讀取中", "目前資料量比較龐大，請耐心等候！！", false, false, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                //
                isCencel = true;
                finish();
            }
        });


        mInviteBtn = (Button) findViewById(R.id.inviteBtn);
        mInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(VideoMainActivity.this, ForIdeaAndShareVideoActivity.class);
                startActivity(i);

            }
        });
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
                Intent petint = new Intent(VideoMainActivity.this, VideoTwoActivity.class);
                petint.putExtra("json", new Gson().toJson(mAdapter.mDatas.get(position)));

                startActivity(petint);


            }
        });


        mAdapter = new MyAdapter(list);

        petlist.setAdapter(mAdapter);


        setFireBase();
		setAdMobAd();
        setFbAd();
    }


    private void setFbAd() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MyAdKey.AdmobinterstitialBannerId);
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);

        RelativeLayout adViewContainer2 = (RelativeLayout) findViewById(R.id.adViewContainer2);
        googleads = new com.facebook.ads.AdView(this, "583698071813390_587400221443175", AdSize.BANNER_320_50);
        adViewContainer2.addView(googleads);
        googleads.loadAd();

    }

    private void setAdMobAd() {
        admobAd = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        admobAd.loadAd(adRequest);


    }

    public class MyAdapter extends BaseAdapter {
        //		private ArrayList<ResultData> mDatas;
        private ArrayList<GayVideo> mDatas;

        public MyAdapter(ArrayList<GayVideo> datas) {
            mDatas = datas;
        }

        public void updateData(ArrayList<GayVideo> datas) {
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
                convertView = LayoutInflater.from(VideoMainActivity.this).inflate(
                        R.layout.mylayout_video, null);
//			ResultData data = mDatas.get(position);
            GayVideo taipeiZoo = mDatas.get(position);
            TextView textname = (TextView) convertView.findViewById(R.id.name);
            TextView bigtext = (TextView) convertView.findViewById(R.id.bigtext);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView  userview = (TextView) convertView.findViewById(R.id.view);
            TextView  userlike = (TextView) convertView.findViewById(R.id.like);

            textname.setText(taipeiZoo.getTittle());
            time.setText("發文時間:"+taipeiZoo.getDate());
            if(taipeiZoo.getView()==0){
                userview.setText("觀看人數:"+0);
            }else {
                userview.setText("觀看人數:"+taipeiZoo.view);
            }
            if (taipeiZoo.getLike()== 0){
                userlike.setText("喜歡人數:"+0);
            }else {
                userlike.setText("喜歡人數:"+taipeiZoo.like);
            }

            imageView = (ImageView) convertView.findViewById(R.id.photoimg);

            Glide.with(VideoMainActivity.this)
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

//            ConfirmExit(); //呼叫ConfirmExit()函數
            VideoMainActivity.this.finish();//關閉activity
            interstitial.show();

            return true;

        }

        return super.onKeyDown(keyCode, event);

    }


    public void ConfirmExit() {

        AlertDialog.Builder ad = new AlertDialog.Builder(VideoMainActivity.this); //創建訊息方塊

        ad.setTitle("離開");

        ad.setMessage("確定要登出帳號?");

        ad.setPositiveButton("登出", new DialogInterface.OnClickListener() { //按"是",則退出應用程式

            public void onClick(DialogInterface dialog, int i) {


                VideoMainActivity.this.finish();//關閉activity
                auth.signOut();
                MySharedPrefernces.saveUserId(VideoMainActivity.this,"");
                interstitial.show();

            }

        });

        ad.setNegativeButton("不用", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作

            public void onClick(DialogInterface dialog, int i) {

                VideoMainActivity.this.finish();//關閉activity
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


    @Override
    protected void onDestroy() {
//		adView.destroy();

        super.onDestroy();
    }


    private void setFireBase() {
        Firebase.setAndroidContext(this);
        String url = "https://sevenpeoplebook.firebaseio.com/GayVideo";

        Firebase mFirebaseRef = new Firebase(url);


//		if(Firebase.getDefaultConfig().isPersistenceEnabled()==false)mFirebaseRef.getDefaultConfig().setPersistenceEnabled(true);
        mFirebaseRef.orderByChild("date").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue().toString());
				Log.d(TAG, "onChildAdded: "+ (String) dataSnapshot.child("tittle").getValue());
//				Log.d(TAG, "onChildAdded: "+ (Long) dataSnapshot.child("message").getValue());
//				TaipeiZoo taipeiZoo = new TaipeiZoo();
//				taipeiZoo.setName((String)dataSnapshot.child("name").getValue());
//                for (DataSnapshot alert: dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onChildAdded: "+alert.getValue().toString());
//                    list.add(0,gayPlace);
////                    for (DataSnapshot recipient: alert.child("formsg").getChildren()) {
////                        Log.d(TAG, "onChildAdded: "+recipient.getValue().toString());
////                    }
////
                GayVideo gayVideo = dataSnapshot.getValue(GayVideo.class);
                list.add(0,gayVideo);

                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();







            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: "+"onChildChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


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
            startActivity(new Intent(VideoMainActivity.this,UserActivity.class));
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(VideoMainActivity.this,InAppBillingActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}