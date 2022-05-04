package com.example.myapp.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.HomeActivity;
import com.example.myapp.LoginActivity;
import com.example.myapp.Model.User;
import com.example.myapp.ProductBooked;
import com.example.myapp.ProfileActivity;
import com.example.myapp.R;
import com.example.myapp.ServiceBooked;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Profile extends Fragment {
    Context context;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = container.getContext();

        TextView tv_info, tv_doiPass, tvDsDon, tvDsDV, tv_dangXuat, tvTen, tvEmail;
        tvTen = view.findViewById(R.id.tvTen);
        tvEmail = view.findViewById(R.id.tvEmail);
        tv_info = view.findViewById(R.id.tv_thongTin);
        tv_doiPass = view.findViewById(R.id.tv_doimk);
        tv_dangXuat = view.findViewById(R.id.tv_DangXuat);
        tvDsDon = view.findViewById(R.id.tv_dsDon);
        tvDsDV = view.findViewById(R.id.tv_dsDV);
        tvTen.setText(HomeActivity.user.getName());
        tvEmail.setText(HomeActivity.user.getEmail());

        tv_dangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),LoginActivity.class));
                getActivity().finish();
            }
        });
        tv_doiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.confirm_pass_dialog);
                dialog.show();
                EditText edtPass = dialog.findViewById(R.id.presentPass);
                EditText edtPassNew = dialog.findViewById(R.id.edt_pass_change);
                EditText edtRePass = dialog.findViewById(R.id.edt_repass_change);
                Button btnChange = dialog.findViewById(R.id.btnConfirmPass);
                btnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pass = edtPass.getText().toString();
                        String newPass = edtPassNew.getText().toString();
                        String RePass = edtRePass.getText().toString();
                        if(RePass.equals(newPass)){
                            ChangePass(pass, newPass);
                            dialog.dismiss();
                        }else {
                            Toast.makeText(v.getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        tv_info.setOnClickListener(Fragment_Profile::onClick);
        tvDsDon.setOnClickListener(Fragment_Profile::onClick);
        tvDsDV.setOnClickListener(Fragment_Profile::onClick);
        return view;
    }

    private static void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_thongTin:
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                view.getContext().startActivity(intent);
                break;
            case R.id.tv_dsDon:
                view.getContext().startActivity(new Intent(view.getContext(), ProductBooked.class));
                break;
            case R.id.tv_dsDV:
                view.getContext().startActivity(new Intent(view.getContext(), ServiceBooked.class));
                break;
        }
    }

    private void ChangePass(String mk, String mknew) {
        String url = "http://10.10.19.2:1337/customer/changePassword";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object1 = new JSONObject(response);
                    if(object1.getString("e").equals("0")){
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("phone",HomeActivity.user.getPhone());
                params.put("password",mk);
                params.put("newPassword", mknew);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
