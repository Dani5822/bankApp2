package com.example.bankapp2.ui.arfolyam;

import static com.example.bankapp2.data.connect.RetrofitClient.getEuroInstance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.bankapp2.R;
import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.currency;
import com.example.bankapp2.databinding.FragmentArfolyamokBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment to display currency exchange rates.
 */
public class arfolyamok extends Fragment {

    private FragmentArfolyamokBinding binding;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArfolyamokBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    /**
     * Initializes the fragment by fetching the currency data.
     */
    private void init() {
        List<String> currencyList = Arrays.asList(getResources().getStringArray(R.array.currency));
        binding.loading.setVisibility(View.VISIBLE);
        new FetchCurrencyTask().execute(currencyList.toArray(new String[0]));
    }

    /**
     * AsyncTask to fetch currency data in the background.
     */
    private class FetchCurrencyTask extends AsyncTask<String, Void, List<List<currency>>> {

        /**
         * Performs a computation on a background thread.
         *
         * @param currencies The currencies to fetch data for.
         * @return The fetched currency data.
         */
        @Override
        protected List<List<currency>> doInBackground(String... currencies) {
            List<List<currency>> currencys = new ArrayList<>();
            for (String currency : currencies) {
                currencys.add(fetchCurrency(currency));
            }
            return currencys;
        }

        /**
         * Runs on the UI thread after doInBackground.
         *
         * @param result The result of the background computation.
         */
        @Override
        protected void onPostExecute(List<List<currency>> result) {
            super.onPostExecute(result);
            List<List<currency>> oszlop1 = result.subList(0, result.size() / 2);
            List<List<currency>> oszlop2 = result.subList(result.size() / 2, result.size());
            arfolyam adapter = new arfolyam(getContext(), oszlop1, oszlop2);
            binding.currency.setAdapter(adapter);
            binding.loading.setVisibility(View.GONE);
        }
    }

    /**
     * Fetches the currency data for the given currency.
     *
     * @param currency The currency to fetch data for.
     * @return The fetched currency data.
     */
    private List<currency> fetchCurrency(String currency) {
        List<currency> currencys = Arrays.asList(null, null);
        CountDownLatch latch = new CountDownLatch(2);
        LocalDate x = LocalDate.now();
        RetrofitApiService currencyService = getEuroInstance(getContext()).create(RetrofitApiService.class);
        
        currencyService.GetCurrencyByDate(x.toString(), currency).enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currencys.set(0, response.body());
                } else {
                    System.out.println("Error fetching current currency: " + response.message());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                System.out.println("Error fetching current currency: " + t.getMessage());
                latch.countDown();
            }
        });

        currencyService.GetCurrencyByDate(x.minusDays(1).toString(), currency).enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currencys.set(1, response.body());
                } else {
                    System.out.println("Error fetching past currency: " + response.message());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                System.out.println("Error fetching past currency: " + t.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(); // Wait for both API calls to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return currencys;
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
