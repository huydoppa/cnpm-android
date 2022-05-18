package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import com.example.myapp.Fragment.Fragment_Home;
import com.example.myapp.Fragment.Fragment_Profile;
import com.example.myapp.Model.Cart;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.Service;
import com.example.myapp.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private ImageView img_cart;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout home_layout;
    private TextView title;
    public static User user;
    public static List<Product> productList;
    public static List<Service> listService;
    public static List<Cart> list;
    public static List<User> userList;
    private static void onClick(View v) {
        switch (v.getId()){
            case R.id.img_cart:
                Intent intent = new Intent(v.getContext(), CartActivity.class );
                v.getContext().startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        list = new ArrayList<>();
        userList = new ArrayList<>();
        GetListProduct();
        getDataService();
        getUserData();
        img_cart.setOnClickListener(HomeActivity::onClick);
        getCart();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_layout, new Fragment_Home());
        fragmentTransaction.commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.item_home:
                        title.setText("Trang chủ");
                        fragment = new Fragment_Home();
                        break;
                    case R.id.item_profile:
                        title.setText("Cá nhân");
                        fragment = new Fragment_Profile();
                        break;
                    default:
                        fragment = new Fragment_Home();
                }
                transaction.replace(R.id.home_layout, fragment);
                transaction.commit();
                return true;
            }
        });
    }




    private void initUI() {
        title = findViewById(R.id.tv_title_home);
        img_cart = findViewById(R.id.img_cart);
        bottomNavigationView = findViewById(R.id.nav_home);
        home_layout = findViewById(R.id.home_layout);
    }
    private void GetListProduct(){
        productList = new ArrayList<>();
        String url = "http://10.10.19.2:1337/api/product";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data =  object.getJSONArray("data");
                    for (int i =0; i < data.length(); i++){
                        JSONObject obj = data.getJSONObject(i);
                        productList.add(new Product(obj.getString("idProduct"),
                                obj.getString("name"),
                                obj.getString("type"),
                                obj.getInt("price"),
                                obj.getInt("promoPrice"),
                                obj.getInt("amount"),
                                obj.getString("image"),
                                obj.getString("description")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }


    public void getDataService(){
        listService = new ArrayList<>();
        String url = "http://10.10.19.2:1337/api/service";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data =  object.getJSONArray("data");
                    for (int i =0; i < data.length(); i++){
                        JSONObject obj = data.getJSONObject(i);
                        listService.add(new Service(obj.getString("idService"),
                                obj.getString("name"),
                                obj.getInt("price"),
                                obj.getInt("promoPrice"),
                                obj.getString("address"),
                                obj.getString("description"),
                                obj.getString("image"),
                                obj.getString("supplier")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }
    //xem gio san pham
    private void getCart(){
        LoginActivity.cartList.clear();
        String url = "http://10.10.19.2:1337/api/cart" ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++){
                        JSONObject data = array.getJSONObject(i);
                        Cart cart = new Cart(data.getString("idCustomer"),
                                data.getString("idProduct"),
                                data.getString("amount"));
                        LoginActivity.cartList.add(cart);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
    public void getUserData(){
        userList.clear();
        String url = "http://10.10.19.2:1337/api/customer";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        userList.add(new User(object.getString("idCustomer"),
                                object.getString("phone"),
                                "",object.getString("name"),
                                object.getString("email"),
                                object.getString("address"),
                                "",""
                                ));

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