package com.example.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.android.volley.toolbox.Volley;
import com.example.myapp.HomeActivity;
import com.example.myapp.Model.Cart;
import com.example.myapp.Model.Comment;
import com.example.myapp.Model.Product;
import com.example.myapp.ProductActivity;
import com.example.myapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductViewHolder> {
    private List<Cart> productList;
    private Context context;
    private CartOnClick cartOnClick;
    public ProductCartAdapter(Context context, CartOnClick cartOnClick) {
        this.context = context;
        this.cartOnClick = cartOnClick;
    }
    public void setData(List<Cart> list){
        productList = list;
        notifyDataSetChanged();
    }
    public interface CartOnClick{
        void OnPlus(ImageView imageView, TextView textView, Cart cart, CheckBox checkBox);
        void OnDecrease(ImageView imageView, TextView textView, Cart cart, CheckBox checkBox);
        void OnCheck( boolean isChecked, Cart cart, TextView textView );
        void OnCDelete(Cart cart,int position, CheckBox checkBox,TextView textView);
    };
    @NonNull
    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductCartAdapter.ProductViewHolder holder, int position) {
        Cart cart = productList.get(position);
        Product product = getProduct(cart.getIdProduct());
        holder.tvProductName.setText(product.getProductName());
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
        int count = Integer.parseInt(holder.tvCount.getText().toString());
        if(count == 1){
            holder.img_remove.setVisibility(View.GONE);
        }
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartOnClick.OnCDelete(cart, position, holder.checkBox, holder.tvCount);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartOnClick.OnCheck(isChecked,cart, holder.tvCount);
            }
        });
        holder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartOnClick.OnPlus(holder.img_remove, holder.tvCount, cart, holder.checkBox);
            }
        });
        holder.img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cartOnClick.OnDecrease(holder.img_remove, holder.tvCount, cart, holder.checkBox);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, img_add, img_remove, img_delete;
        TextView tvProductName, tvProductPrice, tvProductProPrice,tvCount;
        CheckBox checkBox;
        public ProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_product);
            imgProduct = itemView.findViewById(R.id.img_product_cart);
            tvProductName = itemView.findViewById(R.id.tv_productName_cart);
            tvProductPrice = itemView.findViewById(R.id.tv_productPrice_cart);
            tvProductProPrice = itemView.findViewById(R.id.tv_productProPrice_cart);
            img_add = itemView.findViewById(R.id.img_plus);
            img_remove = itemView.findViewById(R.id.img_remove);
            img_delete  = itemView.findViewById(R.id.img_delete);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }
    private Product getProduct(String id){
        for(Product product: HomeActivity.productList){
            if (product.getIdProduct().equals(id))
                return product;
        }
        return new Product();
    }
}
