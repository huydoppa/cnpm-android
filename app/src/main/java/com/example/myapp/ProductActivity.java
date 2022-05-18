package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.myapp.Adapter.CommentAdapter;
import com.example.myapp.Model.Cart;
import com.example.myapp.Model.Comment;
import com.example.myapp.Model.Product;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> commentList;
    List<Comment> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent intent = getIntent();


        commentList = new ArrayList<>();
        list = new ArrayList<>();
        Product product = (Product) intent.getSerializableExtra("product");
        TextView tvInfo = findViewById(R.id.tvInforProduct);
        ImageView imgProduct = findViewById(R.id.imgProduct);
        TextView tvEmpty = findViewById(R.id.tvComment);
        TextView tvProductName = findViewById(R.id.tvProductName);
        TextView tvProductPrice = findViewById(R.id.tvProductPrice);
        TextView tvProductProPrice = findViewById(R.id.tvProductProPrice);
        Button btnAdd = findViewById(R.id.btn_addToCart);
        EditText edtVote = findViewById(R.id.edtVote);
        Button btnVote = findViewById(R.id.btnVote);

        recyclerView = findViewById(R.id.list_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        tvEmpty.setVisibility(View.GONE);
        getComment();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProductComment(product.getIdProduct());
                recyclerView.setAdapter(commentAdapter);
                commentAdapter.setData(commentList);
                if(commentList.size() <= 0){
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
        },1000);


        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtVote.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(ProductActivity.this, "Hãy nhập nội dung", Toast.LENGTH_SHORT).show();
                }else{
                addComment(product.getIdProduct(),content);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getComment();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getProductComment(product.getIdProduct());
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean chuaAdd = true;
                String id = HomeActivity.user.getIdUser();
                for(Cart cart:LoginActivity.cartList){
                    if(cart.getIdCustomer().equals(id)){
                        if(cart.getIdProduct().equals(product.getIdProduct())){
                            chuaAdd = false;
                        }
                    }
                }

                if(chuaAdd){
                    btnAdd.setClickable(false);
                    addToCart(product.getIdProduct());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCart();
                            Toast.makeText(ProductActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            btnAdd.setClickable(true);
                        }
                    },1500);
                }else{
                    Toast.makeText(ProductActivity.this, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvInfo.setText(product.getDescription());
        Picasso.with(this).load(product.getImg_product()).into(imgProduct);
        tvProductName.setText(product.getProductName());
        tvProductPrice.setText(String.format("%,d",product.getPriceProduct())+"đ");
        tvProductProPrice.setText(String.format("%,d",product.getProPrice())+"đ");
        if(product.getProPrice() == 0){
            tvProductPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            tvProductPrice.setTextColor(getResources().getColor(R.color.red));
            tvProductProPrice.setText("");
        }else{
            tvProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvProductPrice.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    private List<Comment> dataList() {
        List<Comment> list = new ArrayList<>();

        return list;
    }
    //them vao gio
    private void addToCart(String idProduct){
        String url = "http://10.10.19.2:1337/api/cart";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idCustomer", HomeActivity.user.getIdUser());
                params.put("idProduct", idProduct);
                return params;
            }
        };
        requestQueue.add(stringRequest);
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
                Toast.makeText(ProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void getCart(){
        LoginActivity.cartList.clear();
        String url = "http://10.10.19.2:1337/api/cart" ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Cart cart = new Cart(object.getString("idCustomer"),
                                object.getString("idProduct"),
                                object.getString("amount"),
                                object.getString("id"));
                        LoginActivity.cartList.add(cart);
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
    public void getProductComment(String id){
        commentList.clear();
        for(Comment comment : list){
            if(comment.getIdProduct().equals(id) && comment.getIdUser().equals(HomeActivity.user.getIdUser())){
                commentList.add(comment);
            }
        }
    }

}