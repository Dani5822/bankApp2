package com.example.bankapp2.ui.login;



import static com.example.bankapp2.data.connect.RetrofitClient.getInstance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.bankapp2.MainActivity;
import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.LoggedInUser;
import com.example.bankapp2.databinding.ActivityLoginBinding;
import com.example.bankapp2.global;
import com.example.bankapp2.regist;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LoginActivity handles the user login process, including input validation and authentication.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private boolean nightModeEnabled;
    SharedPreferences sharedPreferences;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username.getEditText();
        final EditText passwordEditText = binding.password.getEditText();
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        nightModeEnabled = sharedPreferences.getBoolean("NIGHT_MODE", false);


        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
                apiService.loginUser(usernameEditText.getText().toString(), passwordEditText.getText().toString()).enqueue(new Callback<LoggedInUser>() {
                    @Override
                    public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                        if (response.isSuccessful()) {
                            ((global) getApplication()).setId(response.body().getId());
                            ((global) getApplication()).setAccess_token("Bearer "+response.body().getAccess_token());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userid", response.body().getId());
                            startActivity(intent);
                            finish();

                        } else {
                            loadingProgressBar.setVisibility(View.GONE);
                            binding.errormessage.setText("Invalid Username or Password");
                            System.out.println(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoggedInUser> call, Throwable t) {
                        loadingProgressBar.setVisibility(View.GONE);
                        binding.errormessage.setText("Some Error Occured");
                        System.out.println(t.getMessage());

                    }
                });

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, regist.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
