package com.udrishh.healthy.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.FoodsAdapter;
import com.udrishh.healthy.classes.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddFoodsSearchFragment extends Fragment {
    private View view;
    private MaterialButton addManuallyBtn;
    private MaterialButton continueBtn;
    private MaterialAutoCompleteTextView searchTv;
    private ArrayList<Food> foods = new ArrayList<>();
    private Food selectedFood = new Food();

    public AddFoodsSearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_foods_search, container, false);
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addManuallyBtn = view.findViewById(R.id.add_food_manual);
        continueBtn = view.findViewById(R.id.add_food_continue);
        searchTv = view.findViewById(R.id.add_food_search);
        foods = ((MainActivity) this.requireActivity()).getFoods();

        searchTv.setAdapter(new FoodsAdapter(requireContext(), R.layout.food_drink_item, (List<Food>) foods.clone()));
        searchTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                continueBtn.setVisibility(View.VISIBLE);
                for(Food food : foods){
                    if(food.getName().equals(searchTv.getText().toString())){
                        selectedFood = food;
                    }
                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new AddFoodDetailsFragment(selectedFood))
                        .commit();
            }
        });
    }
}