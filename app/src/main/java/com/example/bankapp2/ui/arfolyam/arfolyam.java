package com.example.bankapp2.ui.arfolyam;

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
import com.example.bankapp2.data.model.currency;

import java.util.List;

/**
 * Adapter class for displaying currency exchange rates.
 */
public class arfolyam extends BaseAdapter {

    private final List<List<currency>> oszlop1;
    private final List<List<currency>> oszlop2;
    private final Context context;

    /**
     * Constructor for the arfolyam adapter.
     *
     * @param context the context in which the adapter is used
     * @param oszlop1 the first list of currency data
     * @param oszlop2 the second list of currency data
     */
    public arfolyam(Context context, List<List<currency>> oszlop1, List<List<currency>> oszlop2) {
        this.context = context;
        this.oszlop1 = oszlop1;
        this.oszlop2 = oszlop2;
    }

    /**
     * Returns the number of items in the adapter.
     *
     * @return the number of items
     */
    @Override
    public int getCount() {
        return oszlop1.size();
    }

    /**
     * Returns the item at the specified position.
     *
     * @param i the position of the item
     * @return the item at the specified position
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     * Returns the ID of the item at the specified position.
     *
     * @param i the position of the item
     * @return the ID of the item
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Returns the view for the item at the specified position.
     *
     * @param i the position of the item
     * @param view the old view to reuse, if possible
     * @param viewGroup the parent that this view will eventually be attached to
     * @return the view for the item at the specified position
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_arfolyam, viewGroup, false);

        TextView currency = view.findViewById(R.id.oszlop1currency);
        TextView currentrate = view.findViewById(R.id.oszlop1currentrate);
        TextView change = view.findViewById(R.id.oszlop1change);
        ImageView icon = view.findViewById(R.id.oszlop1iconbal);
        double currentCurrency = oszlop1.get(i).get(0).getEur().getOrDefault("huf", 0.0);
        double pastcurrency = oszlop1.get(i).get(1).getEur().getOrDefault("huf", 0.0);
        currency.setText(oszlop1.get(i).get(0).getName());
        double szazalek = pastcurrency != 0 ? ((currentCurrency - pastcurrency) / pastcurrency) * 100 : 0;
        currentrate.setText(String.format("%.2f HUF", currentCurrency));
        change.setText(String.format("%.2f", pastcurrency) + " (" + String.format("%.2f", szazalek) + "%)");
        icon.setImageResource(szazalek >= 0 ? R.drawable.up : R.drawable.down);

        View x = view.findViewById(R.id.oszlop1);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("currency", oszlop1.get(i).get(0).getName());
                Navigation.findNavController(view).navigate(R.id.arfolyamreszlet, bundle);
            }
        });

        TextView currency2 = view.findViewById(R.id.oszlop2currency);
        TextView currentrate2 = view.findViewById(R.id.oszlop2currentrate);
        TextView change2 = view.findViewById(R.id.oszlop2change);
        ImageView icon2 = view.findViewById(R.id.oszlop2iconbal);
        double currentCurrency2 = oszlop2.get(i).get(0).getEur().getOrDefault("huf", 0.0);
        double pastcurrency2 = oszlop2.get(i).get(1).getEur().getOrDefault("huf", 0.0);
        currency2.setText(oszlop2.get(i).get(0).getName());
        double szazalek2 = pastcurrency2 != 0 ? ((currentCurrency2 - pastcurrency2) / pastcurrency2) * 100 : 0;
        currentrate2.setText(String.format("%.2f HUF", currentCurrency2));
        change2.setText(String.format("%.2f", pastcurrency2) + " (" + String.format("%.2f", szazalek2) + "%)");
        icon2.setImageResource(szazalek2 >= 0 ? R.drawable.up : R.drawable.down);

        View y = view.findViewById(R.id.oszlop2);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("currency", oszlop2.get(i).get(0).getName());
                Navigation.findNavController(view).navigate(R.id.arfolyamreszlet, bundle);
            }
        });

        return view;
    }
}
