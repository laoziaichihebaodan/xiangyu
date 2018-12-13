package com.example.administrator.zxingdemo.login;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.zxingdemo.R;
import com.example.administrator.zxingdemo.application.BaseActivity;

public class ChangePasswordActivity extends BaseActivity {
    private EditText oldPassword,newPassword,surePassword;
    private Button confirm;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        surePassword = (EditText) findViewById(R.id.sure_password);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }
    private void confirm() {
        // Reset errors.
        oldPassword.setError(null);
        newPassword.setError(null);
        surePassword.setError(null);

        // Store values at the time of the login attempt.
        String oldPasswordString = oldPassword.getText().toString();
        String newPasswordString = newPassword.getText().toString();
        String surePasswordString = surePassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //确认密码
        if (TextUtils.isEmpty(surePasswordString) || !isPasswordValid(surePasswordString)) {
            surePassword.setError(getString(R.string.error_invalid_sure_password));
            focusView = surePassword;
            cancel = true;
        }
        //新密码
        if (TextUtils.isEmpty(newPasswordString) || !isPasswordValid(newPasswordString)) {
            newPassword.setError(getString(R.string.error_invalid_new_password));
            focusView = newPassword;
            cancel = true;
        }
        // 旧密码
        if (TextUtils.isEmpty(oldPasswordString) || !isPasswordValid(oldPasswordString)) {
            oldPassword.setError(getString(R.string.error_invalid_old_password));
            focusView = oldPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //新旧密码比较
            if (newPasswordString.equals(oldPasswordString)) {
                Toast toast = Toast.makeText(this,"新密码与旧密码相同",Toast.LENGTH_SHORT);
                toast.setText("新密码与旧密码相同");
                toast.show();
                return;
            }
            //两次密码比较
            if (!newPasswordString.equals(surePasswordString)) {
                Toast toast = Toast.makeText(this,getString(R.string.error_invalid_unlike),Toast.LENGTH_SHORT);
                toast.setText(getString(R.string.error_invalid_unlike));
                toast.show();
                return;
            }
            change();

        }
    }

    private void change() {
        if (dialog == null){
            dialog = new Dialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false); // 点击外部返回
        }
        finish();
//        dialog.show();
//        try {
//            Thread.sleep(2000);
//        }catch (Exception e){}
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.0.109:8080/xlbt/selectActivity", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dialog.dismiss();
//                Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                Toast.makeText(LoginActivity.this,"网络错误，请重试···",Toast.LENGTH_SHORT).show();
//            }
//        });
//        queue.add(request);
//        queue.cancelAll(request);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }
}
