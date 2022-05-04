package com.example.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.HomeActivity;
import com.example.myapp.Model.Product;
import com.example.myapp.Model.ProductCart;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductPayAdapter extends RecyclerView.Adapter<ProductPayAdapter.ProductViewHolder> {
    private List<ProductCart> productList;
    private Context context;

    public ProductPayAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<ProductCart> list){
        productList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_pay, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductPayAdapter.ProductViewHolder holder, int position) {
        ProductCart productCart = productList.get(position);
        Product product = getProduct(productCart.getCart().getIdProduct());
        Picasso.with(context).load(product.getImg_product()).into(holder.imgProduct);
        holder.tvProductPrice.setText(String.format("%,d",product.getPriceProduct())+"đ");
        holder.tvProductProPrice.setText(String.format("%,d",product.getProPrice())+"đ");
        Picasso.with(context).load(product.getImg_product()).into(holder.imgProduct);
        if(product.getProPrice() == 0){
            holder.tvProductPrice.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvProductProPrice.setText("");
        }else{
            holder.tvProductPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProductPrice.setTextColor(context.getResources().getColor(R.color.gray));
        }
        holder.tvCount.setText(productCart.getCount()+"");

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductPrice, tvProductProPrice,tvCount;
        public ProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product_Pay );
            tvProductName = itemView.findViewById(R.id.tv_productName_Pay);
            tvProductPrice = itemView.findViewById(R.id.tv_productPrice_Pay);
            tvProductProPrice = itemView.findViewById(R.id.tv_productProPrice_Pay);
            tvCount = itemView.findViewById(R.id.tvSoLuong);
        }
    }
    private Product getProduct(String id){

        for(Product product: HomeActivity.productList){
            if(product.getIdProduct().equals(id)){
                return product;
            }
        }

        return new Product();
    }
}
