package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Model.Cart;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.User;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edt_email, edt_pass;
    private Button btn_login;
    public static List<Cart> cartList;
    private TextView tv_dangKy, tv_quenMK;
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
//        GetListUser();
        cartList = new ArrayList<>();
        getCart();

        tv_dangKy.setOnClickListener(this::onClick);
        tv_quenMK.setOnClickListener(this::onClick);
        btn_login.setOnClickListener(this::onClick);
    }

    private void initUI() {
        edt_email = findViewById(R.id.edt_email_login);
        edt_pass = findViewById(R.id.edt_pass_login);
        tv_dangKy = findViewById(R.id.tv_dangKy);
        tv_quenMK = findViewById(R.id.tv_quenMK);
        btn_login = findViewById(R.id.btn_login);
    }

    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_quenMK:
                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.email_confirm_dialog);
                dialog.show();
                EditText edtEmail = dialog.findViewById(R.id.edt_email_quenmk);
                Button btn_send = dialog.findViewById(R.id.btn_send);
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialogConfirm = new Dialog(LoginActivity.this);
                        dialogConfirm.setContentView(R.layout.confirm_dialog_layout);
                        dialogConfirm.show();
                        dialog.dismiss();
                        EditText edt_code = dialogConfirm.findViewById(R.id.edt_code);
                        Button btn_confirm = dialogConfirm.findViewById(R.id.btn_Confirm);
                        btn_confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog dialog1 = new Dialog(LoginActivity.this);
                                dialog1.setContentView(R.layout.change_pass_dialog_layout);
                                dialog1.show();
                                dialogConfirm.dismiss();
                                EditText edt_pass_new = dialog1.findViewById(R.id.edt_pass_new);
                                EditText edt_repass = dialog1.findViewById(R.id.edt_repass);
                                Button btn_change = dialog1.findViewById(R.id.btn_Confirm_Change);
                                btn_change.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String pass1 = edt_pass_new.getText().toString();
                                        String pass2 = edt_repass.getText().toString();
                                        if (pass1.equals(pass2)) {
                                            dialog1.dismiss();
                                            Toast.makeText(LoginActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                break;

            case R.id.btn_login:
                String email = edt_email.getText().toString().trim();
                String pass = edt_pass.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Login(email, pass);
                }
                break;

            case R.id.tv_dangKy:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

    //    private void GetListUser(){
//        userList = new ArrayList<>();
//        userList.clear();
//        String url = "http://10.10.19.2:1337/api/customer";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++){
//                    try {
//                        JSONObject object = response.getJSONObject(i);
//                        userList.add(new User(object.getString("idCustomer") ,
//                                object.getString("phone"),
//                                object.getString("password"),
//                                object.getString("name"),
//                                object.getString("email"),
//                                object.getString("address"),
//                                object.getString("avatar"),
//                                object.getString("birthday")));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(jsonArrayRequest);
//    }
    private void Login(String phone, String pass) {
        String url = "http://10.10.19.2:1337/customer/login";
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("e").equals("1")) {
                        Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject object = new JSONObject(object1.getString("data"));
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        User user = new User(object.getString("idCustomer"),
                                object.getString("phone"),
                                object.getString("password"),
                                object.getString("name"),
                                object.getString("email"),
                                object.getString("address"),
                                object.getString("avatar"),
                                object.getString("birthday"));
                        intent.putExtra("user", user);
                        startActivity(intent);
                        dialog.dismiss();

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", pass);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getCart() {
        String url = "http://10.10.19.2:1337/api/cart";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Cart cart = new Cart(object.getString("idCustomer"),
                                object.getString("idProduct"),
                                object.getString("amount"),
                                object.getString("id"));
                        cartList.add(cart);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}
