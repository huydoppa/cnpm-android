package com.example.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.AllServiceActivity;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.Service;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.example.myapp.ServiceActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> implements Filterable {
    private List<Service> serviceList;
    private Context context;
    private List<Service> serviceListOld;

    public ServiceAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Service> list){
        serviceList = list;
        serviceListOld = list;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceAdapter.ViewHolder holder, int position) {
        Service service = serviceList.get(position);

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
        holder.tvSupplier.setText(service.getSupplier());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceActivity.class);
                intent.putExtra("service", service);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public String BoDau(String s){
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String s = constraint.toString();
                String strSearch =  BoDau(s);
                if(strSearch.isEmpty()){
                    serviceList = serviceListOld;
                }else{
                    List<Service> list = new ArrayList<>();
                    for(Service service : serviceList){
                        if( BoDau(service.getNameService().toLowerCase()).contains(strSearch)){
                            list.add(service);
                        }
                    }
                    serviceList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = serviceList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                serviceList = (List<Service>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvProductProPrice, tvSupplier;
        LinearLayout layout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_service);
            tvProductName = itemView.findViewById(R.id.tv_serviceName);
            tvProductPrice = itemView.findViewById(R.id.tv_servicePrice);
            tvProductProPrice = itemView.findViewById(R.id.tv_serviceProPrice);
            tvSupplier = itemView.findViewById(R.id.serviceSupplier);
            layout = itemView.findViewById(R.id.layout_item_product);
        }
    }

}
