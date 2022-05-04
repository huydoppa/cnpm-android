package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Adapter.CommentAdapter;
import com.example.myapp.Model.Comment;
import com.example.myapp.Model.Service;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceActivity extends AppCompatActivity {
    private EditText edtTime;
    private TextView tvServiceName,tvServicePrice, tvServiceProPrice, tvDescription, tvSupplier;
    private TextView tvAddress;
    private ImageView imgService;
    private Button btnBookService;
    private List<Comment> commentList;
    private List<Comment> list;
    Service service;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        commentList = new ArrayList<>();

        initUI();
        list = new ArrayList<>();
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTime();
            }
        });
        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra("service");
        tvServiceName.setText(service.getNameService());
        tvServicePrice.setText(String.format("%,d",service.getPriceService())+"đ");
        tvServiceProPrice.setText(String.format("%,d",service.getProPriceService())+"đ");
        if(service.getProPriceService() == 0){
            tvServiceProPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            tvServiceProPrice.setTextColor(getResources().getColor(R.color.red));
            tvServiceProPrice.setText("");
        }else{
            tvServicePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvServicePrice.setTextColor(getResources().getColor(R.color.gray));
        }
        tvSupplier.setText(service.getSupplier());
        tvAddress.setText(service.getAddress());
        tvDescription.setText(service.getDescription());
        Picasso.with(this).load(service.getImgService()).into(imgService);
        btnBookService = findViewById(R.id.btn_book);
        TextView tvEmpty = findViewById(R.id.tvCommentService);
        EditText edtVote = findViewById(R.id.edtVoteService);
        Button btnVote = findViewById(R.id.btnVoteService);
        RecyclerView recyclerView = findViewById(R.id.list_comment_service);

        CommentAdapter commentAdapter = new CommentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getComment();
        tvEmpty.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getServiceComment(service.getIdService());
                recyclerView.setAdapter(commentAdapter);
                commentAdapter.setData(commentList);
                if (commentList.size() <=0 ){
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
        },1000);
        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtVote.getText().toString();
                if (TextUtils.isEmpty(content)){
                    Toast.makeText(ServiceActivity.this, "Hãy nhập nội dung", Toast.LENGTH_SHORT).show();
                }else{
                    addComment(service.getIdService(),content);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getComment();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getServiceComment(service.getIdService());
                                    recyclerView.setAdapter(commentAdapter);
                                    commentAdapter.setData(commentList);
                                }
                            },1000);
                        }
                    },1000);
                    edtVote.setText("");
                    tvEmpty.setVisibility(View.GONE);
                }

            }
        });

        btnBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtTime.getText().toString())){
                    Toast.makeText(ServiceActivity.this, "Hãy chọn thời gian", Toast.LENGTH_SHORT).show();
                }else{
                    time = edtTime.getText().toString();
                    addService();
                    Toast.makeText(ServiceActivity.this, "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialogTime() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 8){
                            hourOfDay = 8;
                        }
                        if (hourOfDay > 20){
                            hourOfDay = 20;
                        }
                        calendar.set(year,month,dayOfMonth,hourOfDay,minute);
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        edtTime.setText(format.format(calendar.getTime()));
                    }
                },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();

            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void initUI() {
        edtTime = findViewById(R.id.edtTime);
        tvServiceName = findViewById(R.id.tvServiceName);
        imgService = findViewById(R.id.imgService);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvServiceProPrice = findViewById(R.id.tvServiceProPrice);
        tvDescription = findViewById(R.id.tvInforService);
        tvAddress = findViewById(R.id.tvAddressService);
        tvSupplier = findViewById(R.id.tvSupplier);
    }

    private void addComment(String idProduct,String content){
        String url = "http://10.10.19.2:1337/api/comment";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idObject", idProduct);
                params.put("idCustomer", HomeActivity.user.getIdUser());
                params.put("content", content);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void updateComment(String idProduct){
        List<Comment> list = new ArrayList<>();
        String url = "http://10.10.19.2:1337/api/comment";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i =0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        list.add(new Comment(object.getString("idObject"),
                                object.getString("content"),
                                object.getString("idCustomer")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(Comment comment: list){
                            if(comment.getIdProduct().equals(idProduct) && comment.getIdUser().equals(comment.getIdUser())){
                                commentList.add(comment);
                            }
                        }
                    }
                },2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);

    }
    private void addService(){
        String url = "http://10.10.19.2:1337/api/serviceorder";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idServiceOrder", service.getIdService());
                params.put("idCustomer",HomeActivity.user.getIdUser());
                params.put("nameService", service.getNameService());
                params.put("nameCustomer", HomeActivity.user.getName());
                params.put("address",service.getAddress());
                params.put("time", time);
                params.put("price", "1");
                params.put("promoPrice","1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getComment(){
        list.clear();
        String url = "http://10.10.19.2:1337/api/comment";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i =0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        list.add(new Comment(object.getString("idObject"),
                                object.getString("content"),
                                object.getString("idCustomer")));

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
    public void getServiceComment(String id){
        commentList.clear();
        for(Comment comment : list){
            if(comment.getIdProduct().equals(id) && comment.getIdUser().equals(HomeActivity.user.getIdUser())){
                commentList.add(comment);
            }
        }
    }
}