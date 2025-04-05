package com.example.bankapp2.data.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.bankapp2.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Adapter class for displaying transactions in a ListView.
 */
public class TransactionAdapter extends BaseAdapter {

    private final ArrayList<Transaction> tarnsactionList;
    private final Context context;

    /**
     * Constructor for TransactionAdapter.
     *
     * @param tarnsactionList List of transactions to display.
     * @param context         Context of the application.
     */
    public TransactionAdapter(ArrayList<Transaction> tarnsactionList, Context context) {
        this.tarnsactionList = tarnsactionList;
        this.context = context;
    }

    /**
     * Returns the number of transactions.
     *
     * @return Number of transactions.
     */
    @Override
    public int getCount() {
        return tarnsactionList.size();
    }

    /**
     * Returns the transaction at the specified position.
     *
     * @param i Position of the transaction.
     * @return The transaction at the specified position.
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     * Returns the ID of the transaction at the specified position.
     *
     * @param i Position of the transaction.
     * @return The ID of the transaction.
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Returns the view for the transaction at the specified position.
     *
     * @param i         Position of the transaction.
     * @param view      The old view to reuse, if possible.
     * @param viewGroup The parent that this view will eventually be attached to.
     * @return The view for the transaction at the specified position.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.transactionlistviewitem, viewGroup, false);

        TextView transactionName = view.findViewById(R.id.name);
        TextView transactionAmount = view.findViewById(R.id.amount);
        TextView transactioncategory = view.findViewById(R.id.category);
        ImageView transactionIcon = view.findViewById(R.id.kep);

        transactionName.setText(tarnsactionList.get(i).getCategory());
        transactionAmount.setText(new BigDecimal(tarnsactionList.get(i).getTotal()).toPlainString());
        String ido = new SimpleDateFormat("yyyy MM dd").format(tarnsactionList.get(i).getCreatedAt());
        transactioncategory.setText(ido);

        if (tarnsactionList.get(i).getClass() == Income.class) {
            switch (tarnsactionList.get(i).getCategory()) {
                case "Salary":
                    transactionIcon.setImageResource(R.drawable.salary);
                    break;
                case "Other":
                    transactionIcon.setImageResource(R.drawable.income);
                    break;
                case "Transaction":
                    transactionIcon.setImageResource(R.drawable.income);
                    break;
            }
        } else {
            switch (tarnsactionList.get(i).getCategory()) {
                case "Shopping":
                    transactionIcon.setImageResource(R.drawable.shopping);
                    break;
                case "Transport":
                    transactionIcon.setImageResource(R.drawable.transport);
                    break;
                case "Rent":
                    transactionIcon.setImageResource(R.drawable.rent);
                    break;
                case "Other":
                    transactionIcon.setImageResource(R.drawable.expense);
                    break;
                case "Transaction":
                    transactionIcon.setImageResource(R.drawable.expense);
                    break;
            }
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                if (tarnsactionList.get(i).getClass() == Income.class) {
                    bundle.putString("Income", tarnsactionList.get(i).getId());
                } else {
                    bundle.putString("Expense", tarnsactionList.get(i).getId());
                }

                Navigation.findNavController(view).navigate(R.id.transaction_details, bundle);
            }
        });

        return view;
    }
}
