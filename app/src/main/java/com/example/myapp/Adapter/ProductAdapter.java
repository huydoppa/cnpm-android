package com.example.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.Service;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    private List<Product> productList;
    private Context context;
    private List<Product> productListOld;

    public ProductAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Product> list){
        productList = list;
        productListOld = list;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getProductName());
        holder.tvProductPrice.setText(String.format("%,d",product.getPriceProduct())+"đ");
        holder.tvProductProPrice.setText(String.format("%,d",product.getProPrice())+"đ");
        Glide.with(context).load(product.getImg_product()).into(holder.imgProduct);
        if(product.getProPrice() == 0){
            holder.tvProductPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvProductProPrice.setText("");
        }else{
            holder.tvProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.gray));
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            }
        });
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
                    productList = productListOld;
                }else{
                    List<Product> list = new ArrayList<>();
                    for(Product product : productList){
                        if( BoDau(product.getProductName().toLowerCase()).contains(strSearch)){
                            list.add(product);
                        }
                    }
                    productList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvProductProPrice;
        LinearLayout layout;
        public ProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_productName);
            tvProductPrice = itemView.findViewById(R.id.tv_productPrice);
            tvProductProPrice = itemView.findViewById(R.id.tv_productProPrice);
            layout = itemView.findViewById(R.id.layout_item_product);
        }
    }
}
