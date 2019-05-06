package com.example.administrator.zxingdemo.login;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.zxingdemo.Constant;
import com.example.administrator.zxingdemo.MainActivity;
import com.example.administrator.zxingdemo.R;
import com.example.administrator.zxingdemo.application.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Dialog dialog;
    private TextView changePassWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        changePassWord = (TextView) findViewById(R.id.email_change_password);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        changePassWord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ChangePasswordActivity.class));
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            login();

        }
    }

    private void login() {
        if (dialog == null){
            dialog = new Dialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false); // 点击外部返回
        }
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.URL+"/app/teacher/login.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("status") == 0) {
                        JSONObject object1 = object.getJSONObject("data");
                        Constant.studentId = object1.getInt("id");
                        Toast toast = Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT);
                        toast.setText("登陆成功");
                        toast.show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }else{
                        Toast toast = Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT);
                        toast.setText("登陆失败");
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast toast = Toast.makeText(LoginActivity.this,"网络错误，请重试···",Toast.LENGTH_SHORT);
                toast.setText("网络错误，请重试···");
                toast.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("type","2");
                map.put("userName",mEmailView.getText().toString().trim());
                map.put("passWord",mPasswordView.getText().toString().trim());
                return map;
            }
        };
        queue.add(request);
        queue.cancelAll(request);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

}

