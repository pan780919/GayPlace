package com.jackpan.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jackpan.TaiwanpetadoptionApp.MySharedPrefernces;
import com.jackpan.TaiwanpetadoptionApp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ForIdeaAndShareVideoActivity extends Activity implements View.OnClickListener {
    private EditText mNameEdt, mMailEdt, mTiitleEdt, mMessageEdt;
    private Button mSureBtn, mCancelBtn;
    private ImageView mImg;
    private DisplayMetrics mPhone;
    private final static int CAMERA = 66;
    private final static int PHOTO = 99;
    private final static int VIDEO = 33;
    private String picUri = "";
    private String videoUri = "";
    private String videoUri2 = "";
    private String videoUri3 = "";
    private String videoUri4 = "";
    private String videoUri5 = "";
    private String videoUri6 = "";
    private String videoUri7 = "";
    private String videoUri8 = "";
    private String videoUri9 = "";
    private String videoUri10 = "";
    private String videoName = "";
    private String videoName2 = "";
    private String videoName3 = "";
    private String videoName4 = "";
    private String videoName5 = "";
    private String videoName6 = "";
    private String videoName7 = "";
    private String videoName8 = "";
    private String videoName9 = "";
    private String videoName10 = "";
    private int videoInt = 0;
    CharSequence s;
    private Bitmap bitmap;
    private static final int PICKER = 100;
    private static final String TAG = "ForIdeaAndShareActivity";
    ImageView imageView;
    private static final int REQUEST_EXTERNAL_STORAGE = 200;
    private ProgressDialog progressDialog;
    private TextView mPicName, mVideoName,mVideoName2,mVideoName3,mVideoName4,
    mVideoName5,mVideoName6,mVideoName7,mVideoName8,mVideoName9,mVideoName10;
    private Button mPicBtn, mVideoBtn,mVideoBtn2,mVideoBtn3,mVideoBtn4,mVideoBtn5,
    mVideoBtn6,mVideoBtn7,mVideoBtn8,mVideoBtn9,mVideoBtn10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_idea_and_share_video);
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);
        initLayout();

        Calendar mCal = Calendar.getInstance();
        s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());
        Log.d(TAG, "onCreate: " + s);
    }

    private void initLayout() {
        mPicName = (TextView) findViewById(R.id.picnametext);
        mVideoName = (TextView) findViewById(R.id.videonametext);
        mVideoName2 = (TextView) findViewById(R.id.videonametext2);
        mVideoName3 = (TextView) findViewById(R.id.videonametext3);
        mVideoName4 = (TextView) findViewById(R.id.videonametext4);
        mVideoName5 = (TextView) findViewById(R.id.videonametext5);
        mVideoName6 = (TextView) findViewById(R.id.videonametext6);
        mVideoName7 = (TextView) findViewById(R.id.videonametext7);
        mVideoName8 = (TextView) findViewById(R.id.videonametext8);
        mVideoName9 = (TextView) findViewById(R.id.videonametext9);
        mVideoName10 = (TextView) findViewById(R.id.videonametext10);
        mMailEdt = (EditText) findViewById(R.id.mailedt);
        mTiitleEdt = (EditText) findViewById(R.id.tittle);
        mMessageEdt = (EditText) findViewById(R.id.ideaedt);
        mSureBtn = (Button) findViewById(R.id.btn_sure_send);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel_send);
        findViewById(R.id.btn_sure_send).setOnClickListener(this);
        findViewById(R.id.btn_cancel_send).setOnClickListener(this);
        findViewById(R.id.picbtn).setOnClickListener(this);
        findViewById(R.id.videobtn).setOnClickListener(this);
        findViewById(R.id.videobtn2).setOnClickListener(this);
        findViewById(R.id.videobtn3).setOnClickListener(this);
        findViewById(R.id.videobtn4).setOnClickListener(this);
        findViewById(R.id.videobtn5).setOnClickListener(this);
        findViewById(R.id.videobtn6).setOnClickListener(this);
        findViewById(R.id.videobtn7).setOnClickListener(this);
        findViewById(R.id.videobtn8).setOnClickListener(this);
        findViewById(R.id.videobtn9).setOnClickListener(this);
        findViewById(R.id.videobtn10).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure_send:
                setFireBaseDB(MySharedPrefernces.getUserId(this),
                        mTiitleEdt.getText().toString().trim(),
                        mMessageEdt.getText().toString().trim(),
                        picUri,
                        videoUri,
                        videoUri2,
                        videoUri3,
                        videoUri4,
                        videoUri5,
                        videoUri6,
                        videoUri7,
                        videoUri8,
                        videoUri9,
                        videoUri10,
                        String.valueOf(s));
                break;
            case R.id.btn_cancel_send:
                ForIdeaAndShareVideoActivity.this.finish();
                break;
            case R.id.picbtn:
                selectPic();
                break;
            case R.id.videobtn:
                videoInt = 1;
                selectVideo();
                break;
            case R.id.videobtn2:
                videoInt = 2;
                selectVideo();
                break;
            case R.id.videobtn3:
                videoInt = 3;
                selectVideo();

                break;
            case R.id.videobtn4:
                videoInt = 4;

                selectVideo();

                break;
            case R.id.videobtn5:
                videoInt = 5;

                selectVideo();

                break;
            case R.id.videobtn6:
                videoInt = 6;

                selectVideo();

                break;
            case R.id.videobtn7:
                videoInt = 7;

                selectVideo();

                break;
            case R.id.videobtn8:
                videoInt = 8;

                selectVideo();

                break;
            case R.id.videobtn9:
                videoInt = 9;

                selectVideo();

                break;
            case R.id.videobtn10:
                videoInt = 10;

                selectVideo();

                break;

        }


    }

    private void setFireBaseDB(String id, String tittle, String message, String uri,
                               String videoUri,String videoUri2,String videoUri3,String videoUri4,
                               String videoUri5,String videoUri6,String videoUri7,String videoUri8,
                               String videoUri9,String videoUri10
            , String date
                               ) {
        String url = "https://sevenpeoplebook.firebaseio.com/GayVideo";
        Firebase mFirebaseRef = new Firebase(url);
//		Firebase userRef = mFirebaseRef.child("user");
//		Map newUserData = new HashMap();
//		newUserData.put("age", 30);
//		newUserData.put("city", "Provo, UT");
        Firebase newPostRef = mFirebaseRef.child("posts").push();
//        String newPostKey = newPostRef.getKey();
//        Log.d(TAG, "setFireBaseDB: " + newPostKey);
        String key =id+date;

        Map newPost = new HashMap();
        newPost.put("id", id);
        newPost.put("tittle", tittle);
        newPost.put("message", message);
        newPost.put("pic", uri);
        newPost.put("url", videoUri);
        newPost.put("url2", videoUri2);
        newPost.put("url3", videoUri3);
        newPost.put("url4", videoUri4);
        newPost.put("url5", videoUri5);
        newPost.put("url6", videoUri6);
        newPost.put("url7", videoUri7);
        newPost.put("url8", videoUri8);
        newPost.put("url9", videoUri9);
        newPost.put("url10", videoUri10);
        newPost.put("date", date);
        newPost.put("like",1);
        newPost.put("view",1);
        Map updatedUserData = new HashMap();
//		updatedUserData.put("3/posts/" + newPostKey, true);
        updatedUserData.put(key, newPost);
        mFirebaseRef.updateChildren(updatedUserData, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "onComplete: " + "Error updating data: " + firebaseError.getMessage());
                }else{
                    Log.d(TAG, "onComplete: "+"is true");
                    new AlertDialog.Builder(ForIdeaAndShareVideoActivity.this)
                            .setTitle("訊息")
                            .setMessage("您已經發布文章成功,將跳回首頁！！")
                            .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                @Override
                                public void  onClick(DialogInterface dialog, int which) {
                                    ForIdeaAndShareVideoActivity.this.finish();
                                    dialog.dismiss();


                                }
                            }).show();
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

    private void selectVideo() {
        int permission = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限

            if (ActivityCompat.shouldShowRequestPermissionRationale(ForIdeaAndShareVideoActivity.this,
                    android.Manifest.permission.CAMERA)) {
                new android.support.v7.app.AlertDialog.Builder(ForIdeaAndShareVideoActivity.this)
                        .setMessage("我真的沒有要做壞事, 給我權限吧?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ForIdeaAndShareVideoActivity.this,
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
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, VIDEO);


        }
    }

    private void selectPic() {
        int permission = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限

            if (ActivityCompat.shouldShowRequestPermissionRationale(ForIdeaAndShareVideoActivity.this,
                    android.Manifest.permission.CAMERA)) {
                new android.support.v7.app.AlertDialog.Builder(ForIdeaAndShareVideoActivity.this)
                        .setMessage("我真的沒有要做壞事, 給我權限吧?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ForIdeaAndShareVideoActivity.this,
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
        if ((requestCode == CAMERA || requestCode == VIDEO) && data != null) {
            //取得照片路徑uri
            Uri datauri = data.getData();
            uploadFromFile(datauri);


            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadFromFile(Uri path) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sevenpeoplebook.appspot.com");
        Uri file = path;
        StorageReference imageRef = storageRef.child(file.getLastPathSegment());
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .setCustomMetadata("country", "Thailand")
                .build();
        UploadTask uploadTask = imageRef.putFile(file, metadata);

        progressDialog = new ProgressDialog(ForIdeaAndShareVideoActivity.this);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                progressDialog.dismiss();
                Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳失敗",Toast.LENGTH_SHORT).show();



            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                switch (videoInt){
                    case 1:
                        videoUri = taskSnapshot.getDownloadUrl().toString();
                        mVideoName.setText(taskSnapshot.getMetadata().getName());

                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        videoUri2 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName2.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        videoUri3 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName3.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        videoUri4= taskSnapshot.getDownloadUrl().toString();
                        mVideoName4.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        videoUri5 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName5.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        videoUri6 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName6.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        videoUri7 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName7.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        videoUri8 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName8.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 9:
                        videoUri9 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName9.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 10:
                        videoUri10 = taskSnapshot.getDownloadUrl().toString();
                        mVideoName10.setText(taskSnapshot.getMetadata().getName());
                        progressDialog.dismiss();
                        Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        break;

                }




            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                progressDialog.setTitle("提示訊息");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("上傳中！！");
                progressDialog.show();
            }
        });
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
        progressDialog = new ProgressDialog(ForIdeaAndShareVideoActivity.this);


        mUploadTask.addOnFailureListener(new OnFailureListener() {


            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: " + "onFailure: "+ exception.getMessage()
                );
                progressDialog.dismiss();
                Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳失敗",Toast.LENGTH_SHORT).show();

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
                picUri = taskSnapshot.getDownloadUrl().toString();
//                test(picUri);
                mPicName.setText(taskSnapshot.getMetadata().getName());
                progressDialog.dismiss();
                Toast.makeText(ForIdeaAndShareVideoActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();

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


    /**
     * 查詢MediaStroe Uri對應的絕對路徑。
     *
     * @param context 傳入Context
     * @param uri     傳入MediaStore Uri
     * @return 傳回絕對路徑
     */
    public static String queryAbsolutePath(final Context context, final Uri uri) {
        final String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

}
