package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Adapter.ProductDetailAdapter;
import com.example.myapp.Adapter.ProductPayAdapter;
import com.example.myapp.Model.ProductCart;
import com.example.myapp.Model.ProductDetail;
import com.example.myapp.Model.ProductOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ProductDetail> list;
    ProductDetailAdapter productDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order_details);

        list = new ArrayList<>();
        String id = getIntent().getStringExtra("dataID");
        getData(id);
        recyclerView = findViewById(R.id.listDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productDetailAdapter = new ProductDetailAdapter(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(productDetailAdapter);
                productDetailAdapter.setData(list);
            }
        },1000);
    }
    private void getData(String id){
        list.clear();
        String url = "http://10.10.19.2:1337/api/Productorderdetail";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String idx = object.getString("idProductOrder");
                        if(id.equals(idx)){
                            list.add(new ProductDetail(object.getString("idProduct"),
                                    object.getString("name"),
                                    object.getString("price"),
                                    object.getString("promoPrice"),
                                    object.getString("amount")));
                        }
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