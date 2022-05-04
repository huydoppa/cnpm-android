package com.example.myapp.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Adapter.ProductAdapter;
import com.example.myapp.HomeActivity;
import com.example.myapp.Model.Product;
import com.example.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Food extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_food);
        productAdapter = new ProductAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        productAdapter.setData(ListData());

        SearchManager searchManager = (SearchManager)  getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = view.findViewById(R.id.SearchFood);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    private List<Product> ListData() {
        List<Product> list = new ArrayList<>();
        for(Product product: HomeActivity.productList){
            if (product.getType().equals("Thuc an")){
                list.add(product);
            }
        }
        return list;
    }
}
