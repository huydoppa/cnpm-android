package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Model.User;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    TextInputEditText edtName, edtPhone, edtAddress, edtEmail;
    Button btnSave;
    User user1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user1 = getUser();
        initUI();
        edtName.setText(user1.getName());
        edtEmail.setText(user1.getEmail());
        edtPhone.setText(user1.getPhone());
        edtAddress.setText(user1.getAddress());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                Update(name, email, phone, address);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        user1 = getUser();
                        finish();
                    }
                },1000);
            }
        });
    }

    private void initUI() {
        edtName = findViewById(R.id.edt_hoTen_tt);
        edtAddress = findViewById(R.id.edt_diaChi_tt);
        edtPhone = findViewById(R.id.edt_sdt_tt);
        edtEmail = findViewById(R.id.edt_email_tt);
        btnSave = findViewById(R.id.btn_LuuThongTin);
    }
    private void Update(String name, String email, String phone, String address){
        String url = "http://10.10.19.2:1337/customer/changeprofile";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object1 = new JSONObject(response);
                    if(object1.getString("e").equals("0")){
                        Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idCustomer",HomeActivity.user.getIdUser());
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("address", address);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public User getUser(){
        String url = "http://10.10.19.2:1337/api/customer?idCustomer="+HomeActivity.user.getIdUser();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = response.getJSONObject(0);
                    User user = new User(object.getString("idCustomer"),
                            object.getString("phone"),
                            object.getString("password"),
                            object.getString("name"),
                            object.getString("email"),
                            object.getString("address"),
                            object.getString("avatar"),
                            object.getString("birthday"));
                    HomeActivity.user = user;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
        return HomeActivity.user;
    }
}