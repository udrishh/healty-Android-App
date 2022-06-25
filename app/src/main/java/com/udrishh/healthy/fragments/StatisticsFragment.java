package com.udrishh.healthy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.adapters.RecordAdapter;
import com.udrishh.healthy.classes.Record;
import com.udrishh.healthy.utilities.RecordDateComparator;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticsFragment extends Fragment {
    private View view;
    private TextView recordsTitle;
    private ListView recordsList;
    private ArrayList<Record> records;
    private MaterialButton viewAllRecordsBtn;
    private MaterialButton viewAllStatisticsBtn;

    public StatisticsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        getRecords();
        initialiseComponents();
        return view;
    }

    private void getRecords() {
        records = new ArrayList<>();
        records = (((MainActivity) this.requireActivity()).getRecords());
    }

    private void initialiseComponents() {
        recordsTitle = view.findViewById(R.id.statistics_record_card_title);
        recordsList = view.findViewById(R.id.statistics_record_list);
        viewAllRecordsBtn = view.findViewById(R.id.statistics_view_all_records);
        viewAllStatisticsBtn = view.findViewById(R.id.statistics_view_all_stats);

        if (records.isEmpty()) {
            recordsTitle.setText(getString(R.string.statistics_no_records_text));
        } else {
            if(records!=null){
                Collections.sort(records, new RecordDateComparator());
            }
            ArrayList<Record> lastRecords = new ArrayList<>();
            int nb = Math.min(records.size(), 5);
            if(nb == 1){
                recordsTitle.setText(getString(R.string.statistics_one_stats_text));
            } else {
                recordsTitle.setText(getString(R.string.statistics_stats_text, nb));
            }
            for (int i = 0; i < nb; i++) {
                lastRecords.add(records.get(i));
            }
            recordsList.setAdapter(new RecordAdapter(getContext(), lastRecords));
        }

        viewAllRecordsBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new ViewHistoryFragment(records))
                    .commit();
        });

        viewAllStatisticsBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new PieChartFragment())
                    .commit();
        });

        recordsList.setOnItemClickListener((parent, view, position, id) -> {
            Record selectedRecord = (Record) parent.getItemAtPosition(position);
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, new RecordDetailsFragment(selectedRecord))
                    .commit();
        });
    }
}