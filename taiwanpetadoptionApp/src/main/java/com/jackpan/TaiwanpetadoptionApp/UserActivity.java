package com.jackpan.TaiwanpetadoptionApp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adbert.AdbertListener;
import com.adbert.AdbertLoopADView;
import com.adbert.AdbertOrientation;
import com.adbert.AdbertVideoBox;
import com.adbert.AdbertVideoBoxListener;
import com.adbert.ExpandVideoPosition;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class UserActivity extends Activity implements View.OnClickListener{
    private ImageView mUsetImg;
    private TextView mUserName,mUserId,mUserEmail,mUserLv;
    Activity mActivty;
    private  String mUserPicStr,mUserNameStr,mUserIdStr,mUserMailStr;
    boolean mUserLvBoolean;
    private static final String TAG = "UserActivity";


    private DisplayMetrics mPhone;
    private final static int CAMERA = 66;
    private final static int PHOTO = 99;

    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    private static final int PICKER = 100;

    private ProgressDialog progressDialog;
    AdbertLoopADView adbertView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWindow().setFormat(PixelFormat.TRANSPARENT);
        mActivty = this;
//        showAD();
        getUser();
        initLayout();
        setUser();

    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(adbertView == null){
//            adbertView.pause();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(adbertView == null){
//            adbertView.resume();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(adbertView == null){
//            adbertView.destroy();
//        }
//    }

    private void getUser() {
        mUserPicStr = MySharedPrefernces.getUserPic(mActivty);
        mUserIdStr = MySharedPrefernces.getUserId(mActivty);
        mUserNameStr = MySharedPrefernces.getUserName(mActivty);
        mUserMailStr = MySharedPrefernces.getUserMail(mActivty);
        mUserLvBoolean = MySharedPrefernces.getIsBuyed(mActivty);
    }


    private void initLayout(){

        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);
        mUsetImg = (ImageView) findViewById(R.id.userimg);
        findViewById(R.id.userimg).setOnClickListener(this);
        mUserId = (TextView) findViewById(R.id.userid);
        mUserName = (TextView) findViewById(R.id.username);
        findViewById(R.id.username).setOnClickListener(this);

        mUserEmail = (TextView) findViewById(R.id.usermail);
        mUserLv = (TextView) findViewById(R.id.userlv);

    }
    private void setUser() {
        Glide.with(mActivty)

                .load(mUserPicStr)

                .error(R.drawable.nophoto)//load失敗的Drawable

                .centerCrop()//中心切圖, 會填滿

                .fitCenter()//中心fit, 以原本圖片的長寬為主

                .into(mUsetImg);
        mUserId.setText(mUserIdStr);
        mUserName.setText(mUserNameStr);
        mUserEmail.setText(mUserMailStr);
        if (!mUserLvBoolean) mUserLv.setText("普通會員");
        else  mUserLv.setText("尊榮會員");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userimg:
                selectPic();
                break;
            case R.id.username:
                final EditText edtext = new EditText(mActivty);
                 new AlertDialog.Builder(UserActivity.this)
                         .setView(edtext)
                         .setTitle("修改姓名")
                         .setMessage("輸入要更改的名字")
                         .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 updateUserName(edtext.getText().toString().trim());
                                 dialog.dismiss();
                             }
                         })
                         .show();
                break;

        }

    }

    private void updateUserPic(String picUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                .setPhotoUri(Uri.parse(picUri))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mActivty,"更新照片成功,將於下次登入後更新!!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(mActivty,"更新照片失敗",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });
    }

    private void selectPic() {
        int permission = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限

            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivty,
                    android.Manifest.permission.CAMERA)) {
                new android.support.v7.app.AlertDialog.Builder(mActivty)
                        .setMessage("我真的沒有要做壞事, 給我權限吧?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(mActivty,
                                        new String[]{android.Manifest.permission.CAMERA},
                                        REQUEST_EXTERNAL_STORAGE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE

                );
            }

        } else {
            //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因
            //為點選相片後返回程式呼叫onActivityResult
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PHOTO);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKER) {
            if (resultCode == Activity.RESULT_OK) {


            }
        }
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == CAMERA || requestCode == PHOTO) && data != null) {
            //取得照片路徑uri
            Uri datauri = data.getData();

            uploadFromPic(datauri);


            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void uploadFromPic(Uri datauri) {
        final boolean after44 = Build.VERSION.SDK_INT >= 19;
        String filePath = "";

        if(after44){
            String wholeID = DocumentsContract.getDocumentId(datauri);

// Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

// where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
                Log.d(TAG, "onActivityResult: " + filePath);

            }

            cursor.close();
        }else {

            try {
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(datauri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                Log.d(TAG, "uploadFromPic:"+filePath);
                cursor.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }

//			sharePicWithUri(uri);
//			if (true) return;
//			String uri = data.getData()+"";
//			ContentResolver cr = this.getContentResolver();
//			String uri = "sdcard/req_images/temp.jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sevenpeoplebook.appspot.com");

        StorageReference mountainsRef = storageRef.child(filePath);


        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        UploadTask mUploadTask = mountainsRef.putStream(stream);
        progressDialog = new ProgressDialog(mActivty);


        mUploadTask.addOnFailureListener(new OnFailureListener() {


            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: " + "onFailure: "+ exception.getMessage()
                );
                progressDialog.dismiss();
                Toast.makeText(mActivty,"上傳失敗",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: " + "onSuccess: " + taskSnapshot.getMetadata().getName());
                Log.d(TAG, "onSuccess: " + taskSnapshot.getUploadSessionUri());
                Log.d(TAG, "onSuccess: " + taskSnapshot.getDownloadUrl());

//
//                setFireBaseDB(mNameEdt.getText().toString().trim(),
//                        mTiitleEdt.getText().toString().trim(),
//                        mMessageEdt.getText().toString().trim(),
//                        taskSnapshot.getDownloadUrl().toString());
//                test(picUri);
                updateUserPic(taskSnapshot.getDownloadUrl().toString());



            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //calculating progress percentage
                Log.d(TAG, "onProgress: " + taskSnapshot.getBytesTransferred());
                Log.d(TAG, "onProgress: " + taskSnapshot.getTotalByteCount());

                int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                Log.d(TAG, "onProgress: " + progress);
                Log.d(TAG, progress + "% done");


                progressDialog.setTitle("提示訊息");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("上傳中！！");
                progressDialog.show();
            }

        });
////			try {
////				//讀取照片，型態為Bitmap
////				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
////				//判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
////				if (bitmap.getWidth() > bitmap.getHeight()) ScalePic(bitmap,
////						mPhone.heightPixels);
////				else ScalePic(bitmap, mPhone.widthPixels);
////			} catch (FileNotFoundException e) {
////			}

    }
    private void updateUserName(String Name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mActivty,"更新成功,將於下次登入後更新!!",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(mActivty,"更新失敗",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
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
}
