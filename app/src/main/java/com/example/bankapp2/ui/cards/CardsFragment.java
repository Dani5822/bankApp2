package com.example.bankapp2.ui.cards;



import static com.example.bankapp2.data.connect.RetrofitClient.getInstance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.Card;
import com.example.bankapp2.data.model.CardsAdapter;
import com.example.bankapp2.databinding.FragmentCardsBinding;
import com.example.bankapp2.global;
import com.example.bankapp2.ui.home.HomeFragment;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment to display and manage user cards.
 */
public class CardsFragment extends Fragment {

    ListView listView;
    private FragmentCardsBinding binding;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitApiService service = getInstance().create(RetrofitApiService.class);
                service.createCard(((global) getActivity().getApplication()).getAccess_token(),"HUF", HomeFragment.getUser().getFirstname()+" "+HomeFragment.getUser().getLastname(),HomeFragment.getUser().getId() ).enqueue(new Callback<Card>() {

                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(CardsFragment.super.getContext(), "Sikeres kártya hozzáadás", Toast.LENGTH_SHORT).show();
                            init();
                        }else {
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });

        return root;
    }

    /**
     * Initializes the fragment by fetching and displaying the user's cards.
     */
    public void init() {
        listView=binding.cardlistview;
        RetrofitApiService service = getInstance().create(RetrofitApiService.class);
        service.getUsersByCardID(HomeFragment.getUser().getId(),((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Card[]>() {
            @Override
            public void onResponse(Call<Card[]> call, Response<Card[]> response) {
                if(response.isSuccessful()){
                    List<Card> cards = Arrays.asList(response.body());
                    CardsAdapter adapter = new CardsAdapter(getContext(),cards,getParentFragmentManager());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Card[]> call, Throwable t) {

            }
        });

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
