package com.example.administrator.zxingdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.zxingdemo.application.BaseActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class CreatZxingActivity extends BaseActivity implements View.OnClickListener{
    EditText content;
    Button button1,button2;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_zxing);
        initView();
    }

    private void initView() {
        content = (EditText) findViewById(R.id.edit_input);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        imageView = (ImageView) findViewById(R.id.img_zxing);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String textContent;
        Bitmap mBitmap;
        switch (v.getId()) {
            case R.id.button2:
                /**
                 * 生成二维码图片
                 */
                textContent = content.getText().toString();
                if (TextUtils.isEmpty(textContent)) {
                    Toast.makeText(CreatZxingActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                content.setText("");
                mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                imageView.setImageBitmap(mBitmap);
                break;
            case R.id.button1:
                /**
                 * 生成不带logo的二维码图片
                 */
                textContent = content.getText().toString();
                if (TextUtils.isEmpty(textContent)) {
                    Toast.makeText(CreatZxingActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                content.setText("");
                mBitmap = CodeUtils.createImage(textContent, 400, 400, null);
                imageView.setImageBitmap(mBitmap);
                break;
        }
    }
}
