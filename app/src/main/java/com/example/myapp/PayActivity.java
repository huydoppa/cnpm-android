package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.Adapter.ProductPayAdapter;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.ProductCart;
import com.example.myapp.Model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PayActivity extends AppCompatActivity {
    TextInputEditText edtName, edtPhone, edtAddress;
    RecyclerView recyclerView;
    Button buttonPay;
    TextView tvTotal;
    List<ProductCart> list;
    ProductPayAdapter productPayAdapter;
    String id = HomeActivity.user.getIdUser()+String.valueOf(new Random().nextInt());
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initUI();
        Intent intent = getIntent();
        total = 0;
        list = (List<ProductCart>) intent.getSerializableExtra("ProductCarts");
        for (ProductCart productCart: list){
          Product product = getProduct(productCart.getCart().getIdProduct());
          int gia = product.getProPrice() > 0?product.getProPrice():product.getPriceProduct();
          total+= gia* productCart.getCount();
        }
        tvTotal.setText(String.format("%,d",total)+"đ");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productPayAdapter = new ProductPayAdapter(this);
        recyclerView.setAdapter(productPayAdapter);
        productPayAdapter.setData(list);
        User user = HomeActivity.user;
        edtName.setText(user.getName());
        edtPhone.setText(user.getPhone());
        edtAddress.setText(user.getAddress());
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addProductOrder();
               addProductOrderDetails();
               Toast.makeText(PayActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(PayActivity.this, CartActivity.class));
               finish();
            }
        });
    }

    private void initUI() {
        recyclerView = findViewById(R.id.listProductPay);
        buttonPay = findViewById(R.id.btn_ConfirmPay);
        tvTotal = findViewById(R.id.tvTotalPay);
        edtAddress = findViewById(R.id.edt_Address_pay);
        edtName = findViewById(R.id.edt_hoTen_pay);
        edtPhone = findViewById(R.id.edt_Phone_pay);
    }
    private Product getProduct(String id){

        for (Product product: HomeActivity.productList){
            if (product.getIdProduct().equals(id))
                return product;
        }
        return new Product();
    }
    private void addProductOrder(){
        String url = "http://10.10.19.2:1337/api/Productorder";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PayActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idProductOrder",id);
                params.put("idCustomer",HomeActivity.user.getIdUser());
                params.put("name", edtName.getText().toString());
                params.put("address", edtAddress.getText().toString());
                params.put("totalPrice", total+"");
                params.put("phone", edtPhone.getText().toString());
                params.put("status","pending");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void addProductOrderDetails(){
        String url = "http://10.10.19.2:1337/api/Productorderdetail";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        for (int i = 0; i < list.size(); i++){
            Product product = getProduct(list.get(i).getCart().getIdProduct());
            String count = list.get(i).getCount()+"";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PayActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idProductOrder",id);
                    params.put("idProduct",product.getIdProduct());
                    params.put("name", product.getProductName());
                    params.put("price", product.getPriceProduct()+"");
                    params.put("promoPrice", product.getProPrice()+"");
                    params.put("amount",count);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }
}