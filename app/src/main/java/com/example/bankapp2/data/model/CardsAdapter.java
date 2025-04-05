package com.example.bankapp2.data.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;


import com.example.bankapp2.R;

import java.math.BigInteger;
import java.util.List;

public class CardsAdapter extends BaseAdapter {
    private List<Card> cardList;
    private Context context;
    private FragmentManager fragmentManager;

    public CardsAdapter(Context context, List<Card> cards, FragmentManager fragmentManager) {
        this.context = context;
        cardList = cards;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.cardlayout,viewGroup,false);

        TextView cardName = view.findViewById(R.id.cardname);
        TextView balance = view.findViewById(R.id.balance);
        ImageButton image=view.findViewById(R.id.card);
        cardName.setText(cardList.get(i).getOwnerName());
        if (cardList.get(i).getCurrency().equals("HUF")) {
            BigInteger x = BigInteger.valueOf(cardList.get(i).getTotal());
            balance.setText(x + " HUF");
        } else {
            balance.setText(String.format("%.2f ",  BigInteger.valueOf(cardList.get(i).getTotal()))+" "+cardList.get(i).getCurrency());
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("cardId",cardList.get(i).getId());
                Navigation.findNavController(view).navigate(R.id.cardinfo,bundle);
            }
        });
        return view;
    }
}
