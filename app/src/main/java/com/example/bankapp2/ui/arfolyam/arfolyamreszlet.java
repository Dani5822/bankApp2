package com.example.bankapp2.ui.arfolyam;


import static com.example.bankapp2.data.connect.RetrofitClient.getEuroInstance;
import static java.lang.Float.parseFloat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.currency;
import com.example.bankapp2.databinding.FragmentArfolyamreszletBinding;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment to display currency exchange rate details.
 */
public class arfolyamreszlet extends Fragment {

    private FragmentArfolyamreszletBinding binding;
    private ArrayList<currency> currencyList = new ArrayList<>();
    private int dataNumber = 15;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentArfolyamreszletBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    /**
     * Initializes the fragment by setting up the API service and fetching currency data.
     */
    private void init() {
        RetrofitApiService apiService = getEuroInstance(getContext()).create(RetrofitApiService.class);
        String currency = getArguments().getString("currency");
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter Formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .toFormatter();
        for (int i = 0; i < dataNumber; i++) {
            apiService.GetCurrencyByDate(currentDateTime.format(Formatter), currency).enqueue(new Callback<currency>() {
                @Override
                public void onResponse(Call<currency> call, Response<currency> response) {
                    if (response.isSuccessful()) {
                        currencyList.add(response.body());
                        updatechart();
                    } else {
                        System.out.println("Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<currency> call, Throwable t) {
                    System.out.println("Error: " + t.getMessage());
                }
            });
            currentDateTime = currentDateTime.minusDays(1);
        }

        binding.chart.setTouchEnabled(false);

        binding.currency.setText(currency.toUpperCase(Locale.ROOT));

    }

    /**
     * Updates the chart with the fetched currency data.
     */
    private void updatechart() {

        if (currencyList.size() < dataNumber) {
            return; // Not enough data to update the chart
        }
        currencyList.sort(new Comparator<currency>() {
            @Override
            public int compare(currency currency, currency t1) {
                return currency.getDate().after(t1.getDate()) ? 1 : -1;
            }
        });

        binding.curretrate.setText(currencyList.get(dataNumber-1).getEur().get("huf").toString() + " HUF");
        System.out.println(currencyList.get(dataNumber-1).getDate());

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < currencyList.size(); i++) {
            Float value = currencyList.get(i).getEur().get("huf").floatValue();
            if (value != null) {
                var x = i + 1 + "f";
                entries.add(new Entry(parseFloat(x), value));
            } else {
                System.out.println("Error: currency value is null for date " + currencyList.get(i).getDate());
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setColors(Color.WHITE);

        List<String> labels= new ArrayList<>();
        for (int i = 0; i < currencyList.size(); i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            String formattedDate=formatter.format(currencyList.get(i).getDate());
            labels.add(formattedDate);
        }



        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setDrawGridLines(true); // Enable or disable grid lines
        xAxis.setGridColor(Color.GRAY); // Set the color of the grid lines
        xAxis.setGridLineWidth(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(dataNumber);
        xAxis.setLabelRotationAngle(-60f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setTextSize(13f);
        binding.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if(value == -1 || value >= labels.size()) return "";
                return labels.get((int) value);
            }
        }
        );

        YAxis leftAxis = binding.chart.getAxisLeft();
        leftAxis.setDrawGridLines(false); // Enable or disable grid lines
        leftAxis.setGridColor(Color.GRAY); // Set the color of the grid lines
        leftAxis.setGridLineWidth(1f); // Set the width of the grid lines
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = binding.chart.getAxisRight();
        rightAxis.setDrawGridLines(true); // Enable or disable grid lines
        rightAxis.setGridColor(Color.GRAY); // Set the color of the grid lines
        rightAxis.setGridLineWidth(1f); // Set the width of the grid lines
        rightAxis.setTextColor(Color.WHITE);


        binding.chart.getDescription().setEnabled(false);
        binding.chart.getLegend().setEnabled(false);


        LineData data = new LineData(dataSet);
        binding.chart.setData(data);
        binding.chart.invalidate();

    }
}
