package com.jackpan.TaiwanpetadoptionApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends Activity implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private ImageView fbImg;

    AccessTokenTracker accessTokenTracker ;
    ProfileTracker profileTracker;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        findViewById(R.id.button).setOnClickListener(this);
        fbImg  =(ImageView )findViewById(R.id.fdimg);

        fbLogin();
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "登入:" +
                            user.getUid());
                    userUID = user.getUid();
                    Log.d(TAG, "onAuthStateChanged: "+ firebaseAuth.getCurrentUser().getPhotoUrl());
                    Log.d(TAG, "onAuthStateChanged: "+ firebaseAuth.getCurrentUser().getDisplayName());
                    Log.d(TAG, "onAuthStateChanged: "+ firebaseAuth.getCurrentUser().getEmail());
                    MySharedPrefernces.saveUserId(LoginActivity.this,userUID);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    Log.d(TAG,"已登出");
                    MySharedPrefernces.saveUserId(LoginActivity.this,"");
                    LoginManager.getInstance().logOut();
                    fbImg.setImageBitmap(null);

                }
            }
        };

    }

    private void register(final String email, final String password) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                .setPositiveButton("註冊",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createUser(email, password);
                            }
                        })
                .setNeutralButton("取消", null)
                .show();
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message =
                                        task.isSuccessful() ? "註冊成功" : "註冊失敗";
                                // task.isComplete() ? "註冊成功" : "註冊失敗"; (感謝jiaping網友提醒)
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage(message)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                LoginActivity.this.finish();
                                                dialog.dismiss();

                                            }
                                        })
                                        .show();
                            }
                        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                final String email = ((EditText) findViewById(R.id.email))
                        .getText().toString();
                final String password = ((EditText) findViewById(R.id.password))
                        .getText().toString();
                Log.d("AUTH", email + "/" + password);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("onComplete", "登入失敗");
                            register(email, password);
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "onSuccess: "+authResult.getUser());
                    }
                });
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private  void fbLogin(){
        List<String> PERMISSIONS_PUBLISH = Arrays.asList("public_profile", "email","user_friends");
//        fbName  =(TextView ) findViewById(R.id.fbname);
        loginButton = (LoginButton) findViewById(R.id.fb_btn);
        loginButton.setReadPermissions(PERMISSIONS_PUBLISH);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: "+loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
                setUsetProfile();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");

            }

        });

//		accessTokenTracker = new AccessTokenTracker() {
//			@Override
//			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//			}
//		};




    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
 private void setUsetProfile(){
     profileTracker = new ProfileTracker() {
         @Override
         protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
             if(oldProfile!=null){
                 //登出後
//                    fbName.setText("");
                 fbImg.setImageBitmap(null);

             }

             if(currentProfile!=null){
                 //登入
//                    fbName.setText(currentProfile.getName());
                 MyApi.loadImage(String.valueOf(currentProfile.getProfilePictureUri(150,150)),fbImg,LoginActivity.this);


             }

         }
     };
     profileTracker.startTracking();
     if(profileTracker.isTracking()){
         Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "yes");
         if(Profile.getCurrentProfile()==null)return;

//            if(Profile.getCurrentProfile().getName()!=null)	fbName.setText(Profile.getCurrentProfile().getName());
         if(Profile.getCurrentProfile().getProfilePictureUri(150, 150)!=null)	MyApi.loadImage(String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(150, 150)),fbImg, LoginActivity.this);
     }


     else
         Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "no");

 }

}
