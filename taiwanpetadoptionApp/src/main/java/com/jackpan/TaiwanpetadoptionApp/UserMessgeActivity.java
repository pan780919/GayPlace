package com.jackpan.TaiwanpetadoptionApp;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adbert.AdbertListener;
import com.adbert.AdbertLoopADView;
import com.adbert.AdbertOrientation;
import com.adbert.ExpandVideoPosition;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import java.util.Calendar;


public class UserMessgeActivity extends Activity implements View.OnClickListener {
    AdbertLoopADView adbertView;
    private TextView userMsgText;
    private EditText userMsgEdt;
    private Button  userMsgBtn;
    String id ="";
    String name = "";
    String  tomsg= "";
    CharSequence s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messge);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        getUserid();
        showAD();
        initLayout();
    }

    private static final String TAG = "UserMessgeActivity";
    private void getUserid() {
        Bundle bundle =this.getIntent().getExtras();

        id = bundle.getString("id");
        name = bundle.getString("name");
        tomsg =bundle.getString("msg");
        Log.d(TAG, "getUserid: "+tomsg);
        Calendar mCal = Calendar.getInstance();
        s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
    }

    private void initLayout() {
        userMsgText = (TextView) findViewById(R.id.usermsgtext);
        userMsgText.setText(tomsg);
        userMsgEdt = (EditText) findViewById(R.id.usermsgedt);
        findViewById(R.id.usermsgbtn).setOnClickListener(this);
        setTitle(name+"的留言板");
    }

    private void showAD(){

        adbertView = (AdbertLoopADView)findViewById(R.id.adbertADView);
        adbertView.setMode(AdbertOrientation.NORMAL);
        adbertView.setExpandVideo(ExpandVideoPosition.BOTTOM);
        adbertView.setFullScreen(false);

        adbertView.setAPPID("20161111000002", "5a73897de2c53f95333b6ddaf23639c7");
        adbertView.setListener(new AdbertListener() {
            @Override
            public void onReceive(String msg) {

            }
            @Override
            public void onFailedReceive(String msg) {

            }
        });
        adbertView.start();
    }
    private void toMsg(final String msg) {

        String url = "https://sevenpeoplebook.firebaseio.com/GayPlace";
        Firebase mFirebaseRef = new Firebase(url);

        Firebase countRef = mFirebaseRef.child(id).child("tomsg");
        countRef.runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData currentData) {

//                if (currentData.getValue() == null) {
//                    currentData.setValue("");
////                    userMsgText.setText(currentData.getValue().toString());
//                } else {
                    String value = currentData.getValue().toString();
                    String Usrmsg = msg+"\t"+s;
                    String tomsg = Usrmsg+"\n"+value;
                    currentData.setValue(tomsg);
//                    userMsgText.setText(currentData.getValue().toString());
//
//                }
                return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                //This method will be called once with the results of the transaction.
            }
        });

    }
        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.usermsgbtn:
                    if(userMsgEdt.getText().toString().trim().equals("")&&userMsgEdt.getText().toString().trim()==null){
                        Toast.makeText(UserMessgeActivity.this,"不能輸入空白喔！",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        toMsg(userMsgEdt.getText().toString().trim());
                        userMsgEdt.setText("");
                        Toast.makeText(UserMessgeActivity.this,"已經留言給他囉",Toast.LENGTH_SHORT).show();

                    }

                    break;

            }

        }


    }
