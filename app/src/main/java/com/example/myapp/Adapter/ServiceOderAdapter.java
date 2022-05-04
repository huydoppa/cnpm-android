package com.example.myapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapp.HomeActivity;
import com.example.myapp.Model.Service;
import com.example.myapp.Model.ServiceOder;
import com.example.myapp.R;
import com.example.myapp.ServiceActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ServiceOderAdapter extends RecyclerView.Adapter<ServiceOderAdapter.ViewHolder> {
    private List<ServiceOder> serviceList;
    private Context context;

    public ServiceOderAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<ServiceOder> list){
        serviceList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicebooked, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceOderAdapter.ViewHolder holder, int position) {
        ServiceOder serviceOder = serviceList.get(position);
        Service service = getService(serviceOder.getIdService());

        holder.tvProductName.setText(service.getNameService());
        holder.tvProductPrice.setText(String.format("%,d",service.getPriceService())+"đ");
        holder.tvProductProPrice.setText(String.format("%,d",service.getProPriceService())+"đ");
        Picasso.with(context).load(service.getImgService()).into(holder.imgProduct);
        if(service.getProPriceService() == 0){
            holder.tvProductPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvProductProPrice.setText("");
        }else{
            holder.tvProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.gray));
        }
        holder.tvTime.setText(serviceOder.getTime());
        holder.tvAddress.setText(serviceOder.getAddress());
        holder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Bạn có muốn hủy dịch vụ \" "+service.getNameService()+" \" ?")
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteService(serviceOder.getId());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getData();
                                    }
                                },1000);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                                    }
                                },2000);
                            }
                        }).show();
            }
        });
    }

    private void DeleteService(String id) {
        String url = "http://10.10.19.2:1337/api/serviceorder?id="+id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
    private void getData(){
        serviceList.clear();
        String url = "http://10.10.19.2:1337/api/serviceorder";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String id = object.getString("idCustomer");
                        if(HomeActivity.user.getIdUser().equals(id)){
                            serviceList.add(new ServiceOder(object.getString("idServiceOrder"),
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

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvProductProPrice, tvTime, tvAddress;
        Button btnCancle;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_servicebookede);
            tvProductName = itemView.findViewById(R.id.tv_servicebookedName);
            tvProductPrice = itemView.findViewById(R.id.tv_servicebookedPrice);
            tvProductProPrice = itemView.findViewById(R.id.tv_servicebookedProPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnCancle = itemView.findViewById(R.id.btnCancleService);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }

    }
    public Service getService(String id){

        for(Service service: HomeActivity.listService){
            if(service.getIdService().equals(id))
                return service;
        }
        return new Service();
    }
}
