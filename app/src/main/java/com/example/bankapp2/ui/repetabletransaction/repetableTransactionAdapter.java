package com.example.bankapp2.ui.repetabletransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.navigation.Navigation;


import com.example.bankapp2.R;
import com.example.bankapp2.data.model.RepeatableTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying repeatable transactions in a list.
 */
public class repetableTransactionAdapter extends BaseAdapter {
    private List<RepeatableTransaction> repeatableTransactions;
    private Context context;
    private String token;

    /**
     * Constructor for creating a new instance of repetableTransactionAdapter.
     *
     * @param repeatableTransactions List of repeatable transactions to be displayed.
     * @param context The context in which the adapter is running.
     * @param token The access token for authentication.
     */
    public repetableTransactionAdapter(List<RepeatableTransaction> repeatableTransactions, Context context, String token) {
        this.repeatableTransactions = new ArrayList<>(repeatableTransactions);
        this.context = context;
        this.token = token;
    }

    /**
     * Returns the number of repeatable transactions.
     *
     * @return The number of repeatable transactions.
     */
    @Override
    public int getCount() {
        return repeatableTransactions.size();
    }

    /**
     * Returns the data item at the specified position.
     *
     * @param i The position of the item within the adapter's data set.
     * @return The data item at the specified position.
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     * Returns the row ID associated with the specified position in the list.
     *
     * @param i The position of the item within the adapter's data set.
     * @return The ID of the item at the specified position.
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param i The position of the item within the adapter's data set.
     * @param view The old view to reuse, if possible.
     * @param viewGroup The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.repetabletransactionitem, viewGroup, false);

        TextView transactionName = view.findViewById(R.id.name);
        TextView transactionStartDate = view.findViewById(R.id.startdate);
        TextView transactionEndDate = view.findViewById(R.id.enddate);
        TextView total = view.findViewById(R.id.total);
        transactionName.setText(repeatableTransactions.get(i).getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        transactionStartDate.setText(simpleDateFormat.format(repeatableTransactions.get(i).getRepeatStart()));
        transactionEndDate.setText(simpleDateFormat.format(repeatableTransactions.get(i).getRepeatEnd()));
        total.setText(String.valueOf(Math.round(repeatableTransactions.get(i).getTotal())) + " HUF");


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("repeatableTransactionId", repeatableTransactions.get(i).getId());
                Navigation.findNavController(view).navigate(R.id.repetable_detail, bundle);
            }
        });


        return view;
    }
}
