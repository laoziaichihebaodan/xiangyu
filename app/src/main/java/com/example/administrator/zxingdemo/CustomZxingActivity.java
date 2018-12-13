package com.example.administrator.zxingdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.administrator.zxingdemo.application.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class CustomZxingActivity extends BaseActivity {
    Button open,close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_zxing);
        initView();

        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        /**
         * 二维码解析回调函数
         */
        CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, result);
                resultIntent.putExtras(bundle);
                CustomZxingActivity.this.setResult(RESULT_OK, resultIntent);
                CustomZxingActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                resultIntent.putExtras(bundle);
                CustomZxingActivity.this.setResult(RESULT_OK, resultIntent);
                CustomZxingActivity.this.finish();
            }
        };

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */ getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();

    }

    private void initView() {
        open = (Button) findViewById(R.id.open_falsh);
        close = (Button) findViewById(R.id.close_falsh);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_falsh:
                /**
                 * 打开闪光灯
                 */
                CodeUtils.isLightEnable(true);
                break;
            case R.id.close_falsh:
                /**
                 * 关闭闪光灯
                 */
                CodeUtils.isLightEnable(false);
                break;
        }
    }
}
