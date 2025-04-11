package com.example.bankapp2.ui.statistic;

import static com.example.bankapp2.data.connect.RetrofitClient.getInstance;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.Expense;
import com.example.bankapp2.data.model.Income;
import com.example.bankapp2.data.model.Transaction;
import com.example.bankapp2.databinding.FragmentStatisticBinding;
import com.example.bankapp2.global;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for displaying statistics using a PieChart.
 */
public class statisticFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentStatisticBinding binding;
    private Expense[] expenses;
    private Income[] incomes;
    private final RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
    private String token;
    private PieChart chart;
    private ArrayList<Transaction> sortedTransaction;
    private Map<String, ArrayList<Transaction>> categorys = new Hashtable<>();
    private dropdownadapter adapter;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return Return the View for the fragment's UI, or null
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    /**
     * Initializes the fragment by setting up the PieChart and fetching data.
     */
    public void init() {
        token = ((global) getActivity().getApplication()).getAccess_token();
        chart = binding.chart1;
        setPieChart();
        adapter = new dropdownadapter(getContext(), new ArrayList<Transaction>());
        binding.repetableList.setAdapter(adapter);
        String activeCardId = getArguments().getString("activeCardId");
        apiService.getallexbycardid(activeCardId, token).enqueue(new Callback<Expense[]>() {
            @Override
            public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                if (response.isSuccessful()) {
                    expenses = response.body();
                    updatedata();
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Expense[]> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        apiService.getallinbycardid(activeCardId, token).enqueue(new Callback<Income[]>() {
            @Override
            public void onResponse(Call<Income[]> call, Response<Income[]> response) {
                if (response.isSuccessful()) {
                    incomes = response.body();
                    updatedata();
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Income[]> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    /**
     * Generates the data for the PieChart.
     *
     * @return the PieData for the chart
     */
    private PieData generatePieData() {
        ArrayList<Transaction> x = new ArrayList<>();
        x.clear();
        if (incomes != null) {
            x.addAll(Arrays.asList(incomes));
        }
        if (expenses != null) {
            x.addAll(Arrays.asList(expenses));
        }
        if (x.size() > 0 && x != null) {
            x.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            sortedTransaction = x;
            adapter.clear();
            adapter.addAll(x);
        }

        for (Transaction transaction : x) {
            if (!categorys.containsKey(transaction.getCategory())) {
                categorys.put(transaction.getCategory(), new ArrayList<>());
            }
            categorys.get(transaction.getCategory()).add(transaction);
        }

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (Map.Entry<String, ArrayList<Transaction>> entry : categorys.entrySet()) {
            double sum = sum(entry.getValue());
            PieEntry entry1 = new PieEntry((float) sum, entry.getKey());
            entries1.add(entry1);
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(new int[]{
                Color.parseColor("#4285F4"), // Blue
                Color.parseColor("#EA4335"), // Red
                Color.parseColor("#FBBC05"), // Yellow
                Color.parseColor("#34A853"), // Green
                Color.parseColor("#9B51E0"), // Purple
                Color.parseColor("#00ACC1")  // Cyan
        });
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(12f);

        return new PieData(ds1);
    }

    /**
     * Sums the total amount of transactions in a list.
     *
     * @param list the list of transactions
     * @return the total sum
     */
    private double sum(ArrayList<Transaction> list) {
        double ossz = 0;

        for (int i = 0; i < list.size(); i++) {
            ossz += list.get(i).getTotal();
        }

        return ossz;
    }

    /**
     * Generates the center text for the PieChart.
     *
     * @return the SpannableString for the center text
     */
    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Tranzakciók");
        s.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 8, s.length(), 0);
        return s;
    }

    /**
     * Updates the PieChart with new data.
     */
    private void updatedata() {
        chart.setData(generatePieData());
        chart.getData().setDrawValues(false);
        chart.invalidate();
    }

    /**
     * Sets up the PieChart with initial settings.
     */
    public void setPieChart() {
        chart.getDescription().setEnabled(false);
        chart.setCenterText(generateCenterText());
        chart.setCenterTextSize(9f);
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(true);
        chart.setHoleRadius(75f);

        Legend l = chart.getLegend();
        l.setEnabled(true);
        chart.setRotation(0);
        chart.setRotationEnabled(false);

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setMaxSizePercent(0.95f);
        l.setTextSize(15f);
        l.setTextColor(Color.WHITE);
        l.setDrawInside(false);

        chart.setOnChartValueSelectedListener(this);
    }

    /**
     * Called when the fragment's view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Called when a value is selected in the PieChart.
     *
     * @param e the selected entry
     * @param h the highlight object
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e instanceof PieEntry) {
            PieEntry entry = (PieEntry) e;
            String category = entry.getLabel();
            double sum = sum(categorys.get(category));

            List<Transaction> transactions = categorys.get(category);
            List<Transaction> transactionDetails = new ArrayList<>();
            for (Transaction transaction : transactions) {
                transactionDetails.add(transaction); // Customize this to display relevant details
            }
            adapter.clear();
            adapter.addAll(transactionDetails);
            adapter.notifyDataSetChanged();
            chart.setCenterText(String.valueOf(sum));
            chart.setCenterTextSize(15f);
        }
    }

    /**
     * Called when nothing is selected in the PieChart.
     */
    @Override
    public void onNothingSelected() {
        adapter.clear();
        adapter.addAll(sortedTransaction);
        chart.setCenterText("Tranzakciók");
        chart.setCenterTextSize(15f);
    }
}
