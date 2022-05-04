package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Adapter.ProductCartAdapter;
import com.example.myapp.Adapter.ProductPayAdapter;
import com.example.myapp.Model.Cart;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.ProductCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductCartAdapter productCartAdapter;
    private TextView tvEmpty, tvTotal;
    private int total;
    private Button btnPay;
    private List<ProductCart> productCarts = new ArrayList<>();
    private static List<Cart> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initUI();
        productCarts.clear();
        total = 0;
        list = new ArrayList<>();
        getMyCart();
        tvTotal.setText(String.format("%,d",total)+"đ");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productCartAdapter = new ProductCartAdapter(this, new ProductCartAdapter.CartOnClick() {
            @Override
            public void OnPlus(ImageView imageView, TextView textView, Cart cart, CheckBox checkBox) {
                int count = Integer.parseInt(textView.getText().toString());
                count++;
                if (count > 1){
                    imageView.setVisibility(View.VISIBLE);
                }
                textView.setText(count+"");
                for(int i = 0; i < productCarts.size(); i++){
                    if(productCarts.get(i).getCart().getIdProduct().equals(cart.getIdProduct())){
                        productCarts.get(i).setCount(Integer.parseInt(textView.getText().toString()));
                        break;
                    }
                }
                UpdateTotal();
            }

            @Override
            public void OnDecrease(ImageView imageView, TextView textView, Cart cart, CheckBox checkBox) {
                int count = Integer.parseInt(textView.getText().toString());
                count--;
                if (count <= 1){
                    imageView.setVisibility(View.GONE);
                }
                textView.setText(count+"");
                for(int i = 0; i < productCarts.size(); i++){
                    if(productCarts.get(i).getCart().getIdProduct().equals(cart.getIdProduct())){
                        productCarts.get(i).setCount(Integer.parseInt(textView.getText().toString()));
                        break;
                    }
                }
                UpdateTotal();
                }

                @Override
                public void OnCheck(boolean isChecked, Cart cart, TextView textView) {
                    if(isChecked){
                        productCarts.add(new ProductCart(cart, Integer.parseInt(textView.getText().toString())));
                    }else{
                        for(int i = 0; i < productCarts.size(); i++){
                            if(productCarts.get(i).getCart().getIdProduct().equals(cart.getIdProduct())){
                                productCarts.remove(i);
                                break;
                            }
                        }
                    }
                    UpdateTotal();
                }


                @Override
                public void OnCDelete(Cart cart,int position, CheckBox checkBox, TextView textView) {
                    AlertDialog dialog = new AlertDialog.Builder(CartActivity.this)
                            .setMessage("Bạn có muốn xóa sản phẩm ra khỏi giỏ?")
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkBox.setChecked(false);

                                    DeleteCart(cart.getIdProduct());
                                    list.remove(position);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getCart();
                                            Toast.makeText(CartActivity.this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                                        }
                                    },1000);
                                    productCartAdapter.setData(list);
                                    UpdateTotal();
                                }
                            }).show();

                }

            });
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, PayActivity.class);
                    intent.putExtra("ProductCarts", (Serializable) productCarts);
                    startActivity(intent);
                    for(ProductCart productCart: productCarts){
                        DeleteCart(productCart.getCart().getIdProduct());
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCart();

                        }
                    },1000);
                    finish();
                }
            });
            recyclerView.setAdapter(productCartAdapter);
            productCartAdapter.setData(list);
            if(list.size() > 0){
                tvEmpty.setVisibility(View.GONE);
            }else{
                tvEmpty.setVisibility(View.VISIBLE);
            }
    }
    private void initUI() {
        recyclerView = findViewById(R.id.listCart);
        tvEmpty = findViewById(R.id.tvCartEmpty);
        tvTotal = findViewById(R.id.tvTotalCash);
        productCarts = new ArrayList<>();
        btnPay = findViewById(R.id.btn_pay);
    }
    private void UpdateTotal(){
        int Tong = 0;

        for(ProductCart productCart:productCarts){
            Product product = getProduct(productCart.getCart().getIdProduct());
            int gia= product.getProPrice()>0?product.getProPrice():product.getPriceProduct();
            Tong+= gia*productCart.getCount();
        }
        tvTotal.setText(String.format("%,d",Tong)+"đ");
    }
    private void getMyCart() {
        list.clear();
        String id = HomeActivity.user.getIdUser();
        for(Cart cart:LoginActivity.cartList){
            if(cart.getIdCustomer().equals(id)){
                list.add(cart);
            }
        }
    }
    private Product getProduct(String id){

        for (Product product: HomeActivity.productList){
            if (product.getIdProduct().equals(id))
                return product;
        }

        return new Product();
    }
    private void DeleteCart(String id){
        String url = "http://10.10.19.2:1337/api/cart?id="+id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
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
}