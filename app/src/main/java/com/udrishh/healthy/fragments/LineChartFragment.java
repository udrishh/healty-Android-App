package com.udrishh.healthy.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udrishh.healthy.R;
import com.udrishh.healthy.activities.MainActivity;
import com.udrishh.healthy.classes.MeasurementRecord;
import com.udrishh.healthy.enums.RecordType;
import com.udrishh.healthy.utilities.DateConverter;
import com.udrishh.healthy.utilities.RecordDateComparator;
import com.udrishh.healthy.utilities.ValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import javax.xml.validation.Validator;


public class LineChartFragment extends Fragment {

    private View view;
    private LineChart lineChart;
    private TextView noDataText;
    private String joinDate;
    private ArrayList<MeasurementRecord> records;

    public LineChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_line_chart, container, false);
        joinDate = (((MainActivity) this.requireActivity()).getJoinDate());
        records = (((MainActivity) this.requireActivity()).getMeasurementRecords());
        initialiseComponents();
        return view;
    }

    private void initialiseComponents() {
        lineChart = view.findViewById(R.id.line_chart);
        noDataText = view.findViewById(R.id.statistics_no_data);

        setupLineChart();
        loadLineChartData();
    }

    private void loadLineChartData() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();

        Collections.sort(records, new RecordDateComparator());

        for (int i = records.size() - 1; i >= 0; i--) {
            MeasurementRecord record = records.get(i);
            if (record.getMeasurementCategory() == RecordType.WEIGHT) {
                Log.d("mytagg", record.getDate());
                int days = 0;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date1 = LocalDate.parse(joinDate, dtf);
                LocalDate date2 = LocalDate.parse(record.getDate().split(" ")[0], dtf);
                days = (int) ChronoUnit.DAYS.between(date1, date2);

                entries.add(new Entry(days, record.getValue()));
                Log.d("mytagg", days + " " + record.getValue());
            }
        }

        if (entries.size() > 1) {
            //We can show statistics
            lineChart.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);

            LineDataSet dataSet = new LineDataSet(entries, "greutate înregistrată (kg)");
            dataSet.setDrawCircles(true);
            dataSet.setCircleRadius(4);
            dataSet.setDrawValues(true);
            dataSet.setValueTextSize(16);
            dataSet.setValueFormatter(new DefaultValueFormatter(0));
            dataSet.setLineWidth(1.5f);
            dataSet.setColor(Color.argb(255,92,189,145));
            dataSet.setCircleColor(Color.argb(255,27,76,54));

            dataSets.add(dataSet);

            LineData lineData = new LineData(dataSets);
            lineChart.setData(lineData);
            lineChart.invalidate();
        } else {
            lineChart.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
        }
    }

    private void setupLineChart() {
        Description description = new Description();
        description.setText("");
        description.setTextSize(20);
        lineChart.setDescription(description);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setHighlightPerTapEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter(joinDate));
    }
}