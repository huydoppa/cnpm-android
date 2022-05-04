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
import com.example.myapp.Adapter.ServiceOderAdapter;
import com.example.myapp.Model.ServiceOder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceBooked extends AppCompatActivity {
    List<ServiceOder> list;
    RecyclerView recyclerView;
    TextView tvEmptyService;
    ServiceOderAdapter serviceOderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_booked);
        list = new ArrayList<>();
        getData();
        tvEmptyService = findViewById(R.id.tvEmptyService);
        recyclerView = findViewById(R.id.listServiceBooked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceOderAdapter = new ServiceOderAdapter(this);
        tvEmptyService.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUp();
                if(list.size() > 0){
                    tvEmptyService.setVisibility(View.GONE);
                }else{
                    tvEmptyService.setVisibility(View.VISIBLE);
                }
            }
        },2000);

    }
    private void setUp(){
        recyclerView.setAdapter(serviceOderAdapter);
        serviceOderAdapter.setData(list);
    }
    private void getData(){
        list.clear();
        String url = "http://10.10.19.2:1337/api/serviceorder";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String id = object.getString("idCustomer");
                        if(id.equals(HomeActivity.user.getIdUser())){
                        list.add(new ServiceOder(object.getString("idServiceOrder"),
                                object.getString("id"),
                                object.getString("idCustomer"),
                                object.getString("nameService"),
                                object.getString("nameCustomer"),
                                object.getString("address"),
                                object.getString("time")));
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