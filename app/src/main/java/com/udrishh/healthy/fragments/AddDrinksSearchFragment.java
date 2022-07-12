package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.DrinksAdapter;
import com.udrishh.healthy.classes.Drink;

import java.util.ArrayList;
import java.util.List;

public class AddDrinksSearchFragment extends Fragment {
    private View view;
    private MaterialButton addManuallyBtn;
    private MaterialButton continueBtn;
    private MaterialAutoCompleteTextView searchTv;
    private ArrayList<Drink> drinks = new ArrayList<>();
    private Drink selectedDrink = null;

    public AddDrinksSearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_drinks_search, container, false);
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        addManuallyBtn = view.findViewById(R.id.add_drink_manual);
        continueBtn = view.findViewById(R.id.add_drink_continue);
        searchTv = view.findViewById(R.id.add_drink_search);
        drinks = ((MainActivity) this.requireActivity()).getDrinks();

        if(selectedDrink == null){
            continueBtn.setVisibility(View.INVISIBLE);
        }

        searchTv.setAdapter(new DrinksAdapter(requireContext(), R.layout.food_drink_item, (List<Drink>) drinks.clone()));
        searchTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                continueBtn.setVisibility(View.VISIBLE);
                for(Drink drink : drinks){
                    if(drink.getName().equals(searchTv.getText().toString())){
                        selectedDrink = drink;
                    }
                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new AddDrinkDbDetailsFragment(selectedDrink))
                        .addToBackStack(null)
                        .commit();
            }
        });

        addManuallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame_layout, new AddDrinkManuallyDetailsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}