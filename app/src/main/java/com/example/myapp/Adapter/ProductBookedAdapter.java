package com.example.myapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.HomeActivity;
import com.example.myapp.Model.ProductOrder;
import com.example.myapp.ProductOrderDetailsActivity;
import com.example.myapp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductBookedAdapter extends RecyclerView.Adapter<ProductBookedAdapter.ViewHolder> {

    List<ProductOrder> productOrderList;
    Context context;
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productbooked,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }
    public void setData(List<ProductOrder> list){
        productOrderList = list;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductBookedAdapter.ViewHolder holder, int position) {
        ProductOrder productOrder = productOrderList.get(position);
        holder.tvPhone.setText(productOrder.getPhone());
        Integer total = Integer.parseInt(productOrder.getTotalPrice());
        holder.tvName.setText(productOrder.getName());
        holder.tvAddress.setText(productOrder.getAddress());
        holder.tvTotal.setText(String.format("%,d",total)+"đ");
        holder.tvStatus.setText(productOrder.getStatus());
        if(!productOrder.getStatus().equals("pending")){
            holder.btnHuy.setVisibility(View.GONE);
            if (productOrder.getStatus().equals("cancel")){
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            }
            if (productOrder.getStatus().equals("delivered")){
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
            }
        }
        holder.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context).setMessage("Bạn muốn xóa đơn hàng?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productOrder.setStatus("cancer");
                                Update(productOrder.getId());
                                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Đã hủy", Toast.LENGTH_SHORT).show();
                                    }
                                },1000);

                            }
                        }).setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductOrderDetailsActivity.class);
                intent.putExtra("dataID",productOrder.getIdProductOrder());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvAddress, tvTotal,tvStatus,tvPhone;
        Button btnHuy;
        LinearLayout layout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPhone = itemView.findViewById(R.id.PhoneRe);
            btnHuy = itemView.findViewById(R.id.btnHuy);
            tvAddress = itemView.findViewById(R.id.AddressRe);
            tvName = itemView.findViewById(R.id.nameRe);
            tvTotal = itemView.findViewById(R.id.totalRe);
            tvStatus = itemView.findViewById(R.id.statusRe);
            layout = itemView.findViewById(R.id.layout_item_product_booked);
        }
    }
    private void Update(String id) {
        String url = "http://10.10.19.2:1337/api/Productorder/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", "cancel");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
