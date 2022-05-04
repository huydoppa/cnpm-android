package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Adapter.ProductBookedAdapter;
import com.example.myapp.Model.Cart;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.ProductOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductBooked extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductBookedAdapter adapter;
    List<ProductOrder> list;
    TextView tvEmptyProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_booked);
        list = new ArrayList<>();
        getData();
        tvEmptyProduct = findViewById(R.id.tvEmptyProduct);
        recyclerView = findViewById(R.id.listProductBooked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductBookedAdapter();
        tvEmptyProduct.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                adapter.setData(list);
                if(list.size()<=0){
                    tvEmptyProduct.setVisibility(View.VISIBLE);
                }
            }
        },1000);
    }

    private void getData(){
        list.clear();
        String url = "http://10.10.19.2:1337/api/Productorder";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String id = object.getString("idCustomer");
                        if(id.equals(HomeActivity.user.getIdUser())){
                            list.add(new ProductOrder(object.getString("idProductOrder"),
                                    object.getString("idCustomer"),
                                    object.getString("name"),
                                    object.getString("address"),
                                    object.getString("totalPrice"),
                                    object.getString("status"),
                                    object.getString("id"),
                                    object.getString("phone")));
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