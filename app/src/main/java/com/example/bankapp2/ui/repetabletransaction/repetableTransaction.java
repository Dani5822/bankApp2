package com.example.bankapp2.ui.repetabletransaction;



import static com.example.bankapp2.data.connect.RetrofitClient.getInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.example.bankapp2.data.connect.RetrofitApiService;

import com.example.bankapp2.data.model.RepeatableTransaction;
import com.example.bankapp2.databinding.FragmentRepetableTransactionBinding;
import com.example.bankapp2.global;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Fragment for displaying a list of repeatable transactions.
 */
public class repetableTransaction extends Fragment {
    private FragmentRepetableTransactionBinding binding;
    private RetrofitApiService apiService;
    private String token;
    private String cardId;
    private RepeatableTransaction[] repeatableTransactions;

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
        binding = FragmentRepetableTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        return root;
    }

    /**
     * Initializes the fragment by setting up necessary data and UI components.
     */
    private void init() {
        apiService = getInstance().create(RetrofitApiService.class);
        token = ((global) getActivity().getApplication()).getAccess_token();
        cardId = getArguments().getString("activeCardId");

        apiService.getAllRepeatableTransactions(cardId, token).enqueue(new Callback<RepeatableTransaction[]>() {
            @Override
            public void onResponse(Call<RepeatableTransaction[]> call, Response<RepeatableTransaction[]> response) {
                repeatableTransactions = response.body();
                updateUI();
            }

            @Override
            public void onFailure(Call<RepeatableTransaction[]> call, Throwable t) {

            }
        });


    }

    /**
     * Updates the UI with the list of repeatable transactions.
     */
    private void updateUI() {
        if (repeatableTransactions != null) {
            repetableTransactionAdapter adapter = new repetableTransactionAdapter(Arrays.asList(repeatableTransactions), getContext(), token);
            binding.repetableList.setAdapter(adapter);
        }
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
