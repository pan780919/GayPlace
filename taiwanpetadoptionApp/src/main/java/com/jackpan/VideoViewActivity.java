package com.jackpan;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jackpan.TaiwanpetadoptionApp.R;



public class VideoViewActivity extends Activity {
    private ProgressDialog progressDialog;
    private ImageView imageView;
    private static final String TAG = "VideoViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //		 //開啟全螢幕
		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		        //設定隱藏APP標題
		        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_view);
        Bundle bundle =this.getIntent().getExtras();

        String video = bundle.getString("video");
        Log.d(TAG, "onCreate: "+video);

        VideoView videoView = (VideoView) this.findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);


        //透過 url 播放
        videoView.setVideoURI(Uri.parse(video));

        //透過 sdcard 路徑播放
        //videoView.setVideoPath("/sdcard/testmovie.mp4");

  /*其他 sdcard 路徑的範例，給大家參考
  videoView.setVideoURI(
   Uri.parse("file://" +
   Environment.getExternalStoragePublicDirectory(
   Environment.DIRECTORY_MOVIES) + "/testmovie.mp4"));
  */


        //本次用project下的目錄作為路徑來示範
//        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.testmovie));


        videoView.requestFocus();
        progressDialog = new ProgressDialog(VideoViewActivity.this);

        progressDialog.setTitle("提示訊息");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("讀取中！！");
        progressDialog.show();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
                progressDialog.dismiss();


            }
        });

        addAd();

    }

    private void addAd() {


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();



            }
        });
    }
}
