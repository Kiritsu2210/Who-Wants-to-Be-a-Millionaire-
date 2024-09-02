package com.example.btlmobileapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatisticActivity extends AppCompatActivity {
    private PieChart pieChart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        ImageView imgBack = (ImageView) findViewById(R.id.imgHome);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pieChart = findViewById(R.id.pieChart);
        Map<Integer, Integer> counts = getCountsFromFile();
        // Ghi log kết quả để kiểm tra
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            Log.d("FileRead", entry.getKey() + ": " + entry.getValue());
        }
        displayPieChart();
    }

    private void displayPieChart() {
        Map<Integer, Integer> counts = getCountsFromFile();
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), "Question " + entry.getKey()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Questions");
        List<Integer> colors = getCustomColors(entries.size());
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(16f);
        pieChart.setEntryLabelColor(Color.parseColor("#875d5d"));

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();
    }


    private Map<Integer, Integer> getCountsFromFile() {
        File path = new File(getFilesDir(), "ResultLog");
        File file = new File(path, "ketqua.txt");
        Map<Integer, Integer> counts = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int questionPos = Integer.parseInt(line.trim());
                    counts.put(questionPos, counts.getOrDefault(questionPos, 0) + 1);
                } catch (NumberFormatException e) {
                    Log.e("FileReadError", "Invalid number format: " + line, e);
                }
            }

        } catch (IOException e) {
            Log.e("FileReadError", "Error reading from file", e);
        }

        return counts;
    }


    private List<Integer> getCustomColors(int size) {
        List<Integer> colors = new ArrayList<>();

        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.GRAY);
        colors.add(Color.DKGRAY);
        colors.add(Color.LTGRAY);
        colors.add(Color.BLACK);
        colors.add(Color.WHITE);
        colors.add(Color.rgb(255, 105, 180)); // Hot Pink
        colors.add(Color.rgb(255, 165, 0));   // Orange
        colors.add(Color.rgb(0, 255, 255));   // Aqua
        colors.add(Color.rgb(0, 128, 0));     // Green
    return colors;
    }

}
