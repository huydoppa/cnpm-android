package com.example.myapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapp.Fragment.Fragment_Accessories;
import com.example.myapp.Fragment.Fragment_All;
import com.example.myapp.Fragment.Fragment_ChemicalProduct;
import com.example.myapp.Fragment.Fragment_Cleaning;
import com.example.myapp.Fragment.Fragment_Clothes;
import com.example.myapp.Fragment.Fragment_Food;
import com.example.myapp.Fragment.Fragment_House;
import com.example.myapp.Fragment.Fragment_Medicine;
import com.example.myapp.Fragment.Fragment_Toys;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_All();
            case 1:
                return new Fragment_Food();
            case 2:
                return new Fragment_Clothes();
            case 3:
                return new Fragment_House();
            case 4:
                return new Fragment_Medicine();
            case 5:
                return new Fragment_Toys();
            case 6:
                return new Fragment_ChemicalProduct();
            case 7:
                return new Fragment_Cleaning();
            case 8:
                return new Fragment_Accessories();
            default:
                return new Fragment_All();

        }
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Tất cả";
            case 1:
                return "Thức ăn";
            case 2:
                return "Quần áo";
            case 3:
                return "Chuồng";
            case 4:
                return "Y tế";
            case 5:
                return "Đồ chơi";
            case 6:
                return "Mỹ phẩm";
            case 7:
                return "Vệ sinh";
            case 8:
                return "Phụ kiện";
            default:
                return "Tất cả";
        }

    }
}
