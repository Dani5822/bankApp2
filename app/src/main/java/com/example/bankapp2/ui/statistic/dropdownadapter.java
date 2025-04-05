package com.example.bankapp2.ui.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bankapp2.R;
import com.example.bankapp2.data.model.Transaction;
import com.example.bankapp2.data.model.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for displaying transactions in a dropdown list.
 */
public class dropdownadapter extends ArrayAdapter<Transaction> {

    /**
     * Constructor for dropdownadapter.
     *
     * @param context      the context
     * @param transactions the list of transactions
     */
    public dropdownadapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    /**
     * Provides a view for an AdapterView.
     *
     * @param position    the position of the item within the adapter's data set
     * @param convertView the old view to reuse, if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return a View corresponding to the data at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.statisticlistitemview, parent, false);
        }

        ArrayList<Transaction> x = new ArrayList<>();
        x.add(getItem(position));
        ListView transactionDetail = convertView.findViewById(R.id.category);
        transactionDetail.setAdapter(new TransactionAdapter(x, getContext()));
        return convertView;
    }
}
