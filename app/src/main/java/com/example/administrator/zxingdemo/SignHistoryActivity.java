package com.example.administrator.zxingdemo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.zxingdemo.application.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignHistoryActivity extends BaseActivity {
    private ListView lv;
    private Dialog dialog;
    private List<bean> data = new ArrayList<>();
    private SignHistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_history);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new SignHistoryAdapter();
        initData();
    }

    private void initData() {
        if (dialog == null){
            dialog = new Dialog(this);
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setContentView(R.layout.dialog_progress);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false); // 点击外部返回
        }
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.URL+"/app/teacher/selectCheckList.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    String msg;
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("status") == 0) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            String name = object1.getString("course_name");
                            String type = object1.getString("type");
                            String time = object1.getString("create_time");
                            data.add(new bean(time,name,type));
                        }
                        if (data != null && data.size() > 0){
                            lv.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast toast = Toast.makeText(SignHistoryActivity.this,"网络错误，请重试···",Toast.LENGTH_SHORT);
                toast.setText("网络错误，请重试···");
                toast.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("userId",Constant.studentId+"");
                return map;
            }
        };
        queue.add(request);
        queue.cancelAll(request);
    }


    class bean{
        private String create_time;//": "2019-05-05 14:05:42",
        private String course_name;//": "Linux学习",
        private String type;//": "出勤"

        public bean(String create_time, String course_name, String type) {
            this.create_time = create_time;
            this.course_name = course_name;
            this.type = type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    class SignHistoryAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(SignHistoryActivity.this,R.layout.item_signhistory,null);
            TextView name = (TextView) view.findViewById(R.id.item_name);
            TextView type = (TextView) view.findViewById(R.id.item_type);
            TextView time = (TextView) view.findViewById(R.id.item_time);

            name.setText(data.get(i).getCourse_name());
            type.setText(data.get(i).getType());
            time.setText(data.get(i).getCreate_time());
            switch (data.get(i).getType()){
                case "旷课":
                    type.setTextColor(Color.RED);
                    break;
                case "出勤":
                    type.setTextColor(Color.GREEN);
                    break;
                case "请假":
                    type.setTextColor(Color.BLUE);
                    break;
                case "迟到":
                    type.setTextColor(Color.YELLOW);
                    break;
            }
            return view;
        }
    }
}
