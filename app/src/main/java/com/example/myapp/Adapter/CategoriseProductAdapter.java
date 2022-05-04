package com.example.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.HomeActivity;
import com.example.myapp.ListProductActivity;
import com.example.myapp.LoginActivity;
import com.example.myapp.Model.CategoriesProduct;
import com.example.myapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoriseProductAdapter extends RecyclerView.Adapter<CategoriseProductAdapter.CateProductViewHolder>{
    private List<CategoriesProduct> list;
    private Context context;

    public CategoriseProductAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<CategoriesProduct> list1){
        this.list = list1;
        notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public CateProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cate_product,parent,false);

        return new CateProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoriseProductAdapter.CateProductViewHolder holder, int position) {
        CategoriesProduct categoriesProduct = list.get(position) ;
        holder.tv_name.setText(categoriesProduct.getNameCategory());
        holder.img.setImageResource(categoriesProduct.getImgCategory());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListProductActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CateProductViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private ImageView img;
        public CateProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_cate);
            img = itemView.findViewById(R.id.img_cate_product);
        }
    }
}
