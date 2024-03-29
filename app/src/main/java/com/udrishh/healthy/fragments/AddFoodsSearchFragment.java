package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.FoodsAdapter;
import com.udrishh.healthy.classes.Food;

import java.util.ArrayList;
import java.util.List;

public class AddFoodsSearchFragment extends Fragment {
    private View view;
    private MaterialButton addManuallyBtn;
    private MaterialButton continueBtn;
    private MaterialAutoCompleteTextView searchTv;
    private ArrayList<Food> foods = new ArrayList<>();
    private Food selectedFood = null;
    private FragmentManager fragmentManager;

    public AddFoodsSearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_foods_search, container, false);
        initialiseComponents();
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        searchTv.setOnItemClickListener((parent, view, position, id) -> {
            continueBtn.setVisibility(View.VISIBLE);
            for(Food food : foods){
                if(food.getName().equals(searchTv.getText().toString())){
                    selectedFood = food;
                }
            }
        });
        continueBtn.setOnClickListener(v -> moveToAddFoodDbFragment());
        addManuallyBtn.setOnClickListener(v -> moveToAddFoodManuallyFragment());
    }

    private void moveToAddFoodManuallyFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddFoodManuallyDetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void moveToAddFoodDbFragment() {
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.main_frame_layout, new AddFoodDbDetailsFragment(selectedFood))
                .addToBackStack(null)
                .commit();
    }

    private void initialiseComponents() {
        addManuallyBtn = view.findViewById(R.id.add_food_manual);
        continueBtn = view.findViewById(R.id.add_food_continue);
        searchTv = view.findViewById(R.id.add_food_search);
        foods = ((MainActivity) this.requireActivity()).getFoods();
        if(selectedFood ==null){
            continueBtn.setVisibility(View.INVISIBLE);
        }
        searchTv.setAdapter(new FoodsAdapter(requireContext(), R.layout.food_drink_item, (List<Food>) foods.clone()));
    }
}