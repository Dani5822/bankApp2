package com.example.bankapp2.ui.cardsinfo;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.connect.RetrofitClient;
import com.example.bankapp2.data.model.Card;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for managing card information.
 */
public class CardInfoViewModel extends AndroidViewModel {
    private final MutableLiveData<Card> cardLiveData = new MutableLiveData<>();
    private final RetrofitApiService retrofitApiService;

    /**
     * Constructor for CardInfoViewModel.
     * 
     * @param application the application context
     */
    public CardInfoViewModel(Application application) {
        super(application);
        retrofitApiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
    }

    /**
     * Gets the live data for the card.
     * 
     * @return the live data for the card
     */
    public LiveData<Card> getCardLiveData() {
        return cardLiveData;
    }

    /**
     * Fetches the card information from the server.
     * 
     * @param cardId the card ID
     * @param accessToken the access token
     */
    public void fetchCardInfo(String cardId, String accessToken) {
        retrofitApiService.getCardUsers(cardId, accessToken).enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful()) {
                    cardLiveData.setValue(response.body());
                } else {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                cardLiveData.setValue(null);
            }
        });
    }




}
