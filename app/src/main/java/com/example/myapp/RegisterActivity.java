package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText edt_name,edt_phone, edt_email, edt_pass, edt_address;
    private Button btn_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
                Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.confirm_dialog_layout);
                dialog.show();
                Dialog dialogConfirm = new Dialog(RegisterActivity.this);
                dialogConfirm.setContentView(R.layout.confirm_dialog_layout);
                dialogConfirm.show();
                dialog.dismiss();
                EditText edt_code = dialogConfirm.findViewById(R.id.edt_code);
                Button btn_confirm = dialogConfirm.findViewById(R.id.btn_Confirm);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    private void initUI() {
        edt_name = (TextInputEditText) findViewById(R.id.edt_hoTen);
        edt_phone = (TextInputEditText) findViewById(R.id.edt_sdt);
        edt_email = (TextInputEditText) findViewById(R.id.edt_email);
        edt_pass = (TextInputEditText) findViewById(R.id.edt_pass);
        edt_address = (TextInputEditText) findViewById(R.id.edt_diaChi);
        btn_show= (Button) findViewById(R.id.btn_dangKy);
    }
    private void addUser(){
        String url = "http://10.10.19.2:1337/api/customer";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("password",edt_pass.getText().toString().trim());
                params.put("phone",edt_phone.getText().toString().trim());
                params.put("name",edt_name.getText().toString().trim());
                params.put("email",edt_email.getText().toString().trim());
                params.put("birthday","0");
                params.put("address",edt_address.getText().toString().trim());
                params.put("avatar","0");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}