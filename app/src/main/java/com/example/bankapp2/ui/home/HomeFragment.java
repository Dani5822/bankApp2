package com.example.bankapp2.ui.home;
import static com.example.bankapp2.data.connect.RetrofitClient.getInstance;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.bankapp2.R;
import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.Card;
import com.example.bankapp2.data.model.Expense;
import com.example.bankapp2.data.model.Income;
import com.example.bankapp2.data.model.LoggedInUser;
import com.example.bankapp2.data.model.OnSwipeTouchListener;
import com.example.bankapp2.data.model.RepeatableTransaction;
import com.example.bankapp2.data.model.Transaction;
import com.example.bankapp2.data.model.TransactionAdapter;
import com.example.bankapp2.databinding.FragmentHomeBinding;
import com.example.bankapp2.global;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * HomeFragment is responsible for displaying the home screen of the bank application.
 * It handles user interactions, updates the UI, and fetches data from the server.
 */
public class HomeFragment extends Fragment {

    private static LoggedInUser user;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private final ArrayList<Income> incomes = new ArrayList<>();
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private FragmentHomeBinding binding;
    private int db;
    private Card activeCard;

    /**
     * Returns the currently logged-in user.
     * @return the logged-in user
     */
    public static LoggedInUser getUser() {
        return user;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return the View for the fragment's UI
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();

        return root;
    }

    /**
     * Called when the view previously created by onCreateView has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Initializes the fragment, sets up event listeners, and fetches initial data.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        String id = ((global) getActivity().getApplication()).getId();
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        apiService.getUserById(id, ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    transactions.clear();
                    if (user.getCards().length > 0) {
                        activeCard = user.getCards()[0];
                        for (Card cards : user.getCards()) {
                            updateRepeatTransactions(cards.getId());
                        }
                    }
                    updateUI();
                } else {
                    System.out.println("Error fetching user: " + response.message());
                }
            }


            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                System.out.println("Error fetching user: " + t.getMessage());
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });


        db = 0;
        binding.cardlayout.card.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeLeft() throws InterruptedException {
                super.onSwipeLeft();
                db++;
                if (db == user.getCards().length) {
                    db = 0;
                }

                slideCard(binding.cardlayout.frame, -binding.cardlayout.card.getWidth());
                activeCard = user.getCards()[db];
                updateUI();
            }

            @Override
            public void onSwipeRight() throws InterruptedException {
                super.onSwipeRight();
                db--;
                if (db < 0) {
                    db = user.getCards().length - 1;
                }
                slideCard(binding.cardlayout.frame, binding.cardlayout.card.getWidth());
                activeCard = user.getCards()[db];
                updateUI();
            }
        });

        binding.felvetel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Transactions.class);
                intent.putExtra("cardid", activeCard.getId());
                intent.putExtra("userId", user.getId());
                someActivityResultLauncher.launch(intent);
            }
        });

        binding.statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activeCardId",activeCard.getId());
                Navigation.findNavController(view).navigate(R.id.statisticFragment,bundle);
            }
        });

        binding.arfolyam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.arfolyamok);
            }
        });

        binding.repetableinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activeCardId",activeCard.getId());
                Navigation.findNavController(view).navigate(R.id.repetableTransaction,bundle);
            }
        });
    }

    /**
     * Refreshes the UI by updating the data and stopping the swipe refresh layout's refreshing animation.
     */
    public void Refresh() {
        if (binding != null && binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(true);
            updateUI();
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Updates repeatable transactions for a given card ID.
     * @param cardId the ID of the card
     */
    private void updateRepeatTransactions(String cardId) {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);

                apiService.updateRepeatableTransactions( ((global) getActivity().getApplication()).getAccess_token(), cardId, user.getId()).enqueue(new Callback<RepeatableTransaction>() {
                    @Override
                    public void onResponse(Call<RepeatableTransaction> call, Response<RepeatableTransaction> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateUI();
                        } else {
                            System.out.println("Error fetching repeatable transactions: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<RepeatableTransaction> call, Throwable t) {
                        System.out.println("Error fetching repeatable transactions: " + t.getMessage());
                    }
                });
            }



    /**
     * Updates the UI with the latest data for the active card.
     */
    private void updateUI() {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        Card card = activeCard;
        if (binding!=null&&card != null && binding.cardlayout!=null) {
            updateTotal();
            binding.cardlayout.cardname.setText(card.getOwnerName());

            apiService.getallexbycardid(card.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Expense[]>() {
                @Override
                public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                    expenses.clear();
                    if (response.isSuccessful() && response.body() != null) {
                        Expense[] expenseslocal = response.body();
                        expenses.addAll(Arrays.asList(expenseslocal));
                    } else {
                        System.out.println("Error fetching expenses: " + response.message());
                    }
                    updatetransaction();
                }

                @Override
                public void onFailure(Call<Expense[]> call, Throwable t) {
                    expenses.clear();
                    System.out.println("Error fetching expenses: " + t.getMessage());
                    updatetransaction();
                }
            });
            apiService.getallinbycardid(card.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Income[]>() {
                @Override
                public void onResponse(Call<Income[]> call, Response<Income[]> response) {
                    incomes.clear();
                    if (response.isSuccessful() && response.body() != null) {
                        Income[] incomeslocal = response.body();
                        incomes.addAll(Arrays.stream(incomeslocal).collect(Collectors.toList()));
                    } else {
                        System.out.println("Error fetching incomes: " + response.message());
                    }
                    updatetransaction();
                }

                @Override
                public void onFailure(Call<Income[]> call, Throwable t) {
                    incomes.clear();
                    System.out.println("Error fetching incomes: " + t.getMessage());
                    updatetransaction();
                }
            });
        }
    }

    /**
     * Updates the list of transactions and notifies the adapter of data changes.
     */
    public void updatetransaction() {
        transactions.clear();
        transactions.addAll(incomes);
        transactions.addAll(expenses);
        notifyDataSetChanged();
        if (binding!=null&&transactions.size() > 0 && transactions != null) {
            transactions.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            binding.transactionsListView.setAdapter(new TransactionAdapter(transactions, HomeFragment.this.getContext()));
        }
        updateTotal();
    }

    /**
     * Updates the total balance for the active card.
     */
    public void updateTotal() {
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        apiService.getCardById(activeCard.getId(), ((global) getActivity().getApplication()).getAccess_token()).enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful() && response.body() != null &&binding!=null&& binding.cardlayout != null) {
                    activeCard = response.body();
                    if (activeCard.getCurrency().equals("HUF")) {
                        BigInteger x = BigInteger.valueOf(activeCard.getTotal());
                        binding.cardlayout.balance.setText(x + " HUF");
                    } else {
                        binding.cardlayout.balance.setText(String.format("%.2f", activeCard.getTotal()) + " " + activeCard.getCurrency());
                    }
                } else {
                    System.out.println("Error fetching card: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                System.out.println("Error fetching card: " + t.getMessage());
            }
        });

    }

    /**
     * Notifies the adapter that the data set has changed.
     */
    private void notifyDataSetChanged() {
        if (binding!=null&&binding.transactionsListView!=null&&binding.transactionsListView.getAdapter() != null) {
            ((TransactionAdapter) binding.transactionsListView.getAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * Animates the sliding of the card view.
     * @param card the card view to animate
     * @param toX the target X position for the animation
     * @throws InterruptedException if the thread is interrupted
     */
    private void slideCard(View card, float toX) throws InterruptedException {
        ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationX", toX);
        animator.setAutoCancel(true);
        animator.setDuration(100);
        animator.start();
        animator.addListener(new ObjectAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                binding.cardlayout.card.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateUI();
                slideCardvissza(card, 0, -toX);
                slideCardvissza(card, 100, 0);
                binding.cardlayout.card.setClickable(true);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }


        });
    }

    /**
     * Animates the sliding back of the card view.
     * @param card the card view to animate
     * @param time the duration of the animation
     * @param toX the target X position for the animation
     */
    private void slideCardvissza(View card, int time, float toX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(card, "translationX", toX);
        animator.setAutoCancel(true);
        animator.setDuration(time);
        animator.start();
    }

    /**
     * Handles the result from the launched activity.
     */
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    init();
                    updatetransaction();
                }
            }
    );
}
