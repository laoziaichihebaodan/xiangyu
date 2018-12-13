package com.example.administrator.zxingdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.zxingdemo.application.BaseActivity;
import com.example.administrator.zxingdemo.manager.AppManager;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_CODE = 111;
    public static final int REQUEST_IMAGE = 222;
    public static final int REQUEST_CODE3 = 333;
    private final int RESULT_CODE_CAMERA=1;//判断是否有拍照权限的标识码
    TextView textView,textView2,textView3,textView4,exit,history;
    Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
        initView();
        //相机动态权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //提示用户开户权限
            String[] perms = {"android.permission.CAMERA","android.permission.READ_EXTERNAL_STORAGE"};
            ActivityCompat.requestPermissions(MainActivity.this,perms, RESULT_CODE_CAMERA);

        }

    }

    private void initView() {
        textView = (TextView) findViewById(R.id.click);
        textView2 = (TextView) findViewById(R.id.click2);
        textView3 = (TextView) findViewById(R.id.click3);
        textView4 = (TextView) findViewById(R.id.click4);
        exit = (TextView) findViewById(R.id.exit);
        history = (TextView) findViewById(R.id.history);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        exit.setOnClickListener(this);
        history.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case RESULT_CODE_CAMERA:
                boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean readAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if(cameraAccepted){
                    //授权成功之后，调用系统相机进行拍照操作等
//                    openCamera();
//                    onCreate(savedInstanceState);
                    Toast.makeText(MainActivity.this,"已获得相机权限",Toast.LENGTH_SHORT).show();

                }else{
                    //用户授权拒绝之后，友情提示一下就可以了
                    Toast.makeText(MainActivity.this,"请开启应用拍照权限",Toast.LENGTH_SHORT).show();
                }
                if (readAccepted){
                    //用户
                    Toast.makeText(MainActivity.this,"已获得读写权限",Toast.LENGTH_SHORT).show();
                }else{
                    //用户授权拒绝之后，友情提示一下就可以了
                    Toast.makeText(MainActivity.this,"请开启读写权限",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    textView.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    textView.setText( "解析二维码失败");
                }
            }
        }
        if (requestCode == REQUEST_CODE3) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    textView4.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    textView4.setText( "解析二维码失败");
                }
            }
        }
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this,uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            textView2.setText(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                            textView2.setText( "解析二维码失败");
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()) {

            case R.id.click:
                intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.click2:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.click3:
                intent = new Intent(MainActivity.this,CreatZxingActivity.class);
                startActivity(intent);
                break;
            case R.id.click4:
                intent = new Intent(MainActivity.this,CustomZxingActivity.class);

                startActivityForResult(intent,REQUEST_CODE3);
                break;
            case R.id.exit:
                finish();
                break;
            case R.id.history:
                intent = new Intent(MainActivity.this,SignHistoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getInstance().appExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
