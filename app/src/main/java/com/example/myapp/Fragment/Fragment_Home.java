package com.example.myapp.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Adapter.CategoriseProductAdapter;
import com.example.myapp.AllServiceActivity;
import com.example.myapp.Model.CategoriesProduct;
import com.example.myapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private RecyclerView CateProductList;
    private CategoriseProductAdapter categoriseProductAdapter;
    private TextView tvViewAll;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CateProductList = view.findViewById(R.id.list_products);
        categoriseProductAdapter = new CategoriseProductAdapter(container.getContext());
        CateProductList.setLayoutManager(new GridLayoutManager(container.getContext(),4));
        CateProductList.setAdapter(categoriseProductAdapter);
        categoriseProductAdapter.setData(ListCate());
        tvViewAll = view.findViewById(R.id.ViewAllService);
        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), AllServiceActivity.class));

            }
        });
        return view;
    }



    private List<CategoriesProduct> ListCate() {
        List<CategoriesProduct> list = new ArrayList<>();
        list.add(new CategoriesProduct(1,"Tất cả",R.drawable.all));
        list.add(new CategoriesProduct(1,"Thức ăn",R.drawable.thucan));
        list.add(new CategoriesProduct(2,"Quần áo",R.drawable.quanao));
        list.add(new CategoriesProduct(3,"Chuồng",R.drawable.chuong));
        list.add(new CategoriesProduct(4,"Y tế",R.drawable.yte));
        list.add(new CategoriesProduct(5,"Đồ chơi",R.drawable.toys));
        list.add(new CategoriesProduct(6,"Mỹ phẩm",R.drawable.mypham));
        list.add(new CategoriesProduct(7,"Vệ sinh",R.drawable.vesinh));
        list.add(new CategoriesProduct(8,"Phụ kiện",R.drawable.phukien));
        return list;

    }
}
