package com.example.bankapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.connect.RetrofitClient;
import com.example.bankapp2.data.model.LoggedInUser;
import com.example.bankapp2.ui.login.LoginActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Registration activity for the BankApp2 application.
 */
public class regist extends AppCompatActivity {
    EditText password, lastName, firstName, email;
    Button register, back;


    /**
     * Called when the activity is starting. This is where most initialization should go.
     * 
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String last = lastName.getText().toString();
                String first = firstName.getText().toString();
                String mail = email.getText().toString();
                if (pass.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                if (last.isEmpty()) {
                    lastName.setError("Lastname is required");
                    lastName.requestFocus();
                    return;
                }
                if (first.isEmpty()) {
                    firstName.setError("Firstname is required");
                    firstName.requestFocus();
                    return;
                }
                if (mail.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                if(!mail.matches("[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+")){
                    email.setError("Email is not valid");
                    email.requestFocus();
                    return;
                }
                RetrofitApiService apiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
                Call<LoggedInUser> call = apiService.registerUser(first, last, mail, pass);
                call.enqueue(new Callback<LoggedInUser>() {
                    @Override
                    public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(regist.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            try {
                                if (response.code() == 409) {
                                    email.setError("Email already exists");
                                    email.requestFocus();
                                    return;
                                }
                                String errorBody = response.errorBody().string();
                                JSONObject errorJson = new JSONObject(errorBody);
                                String errorMessage = errorJson.getJSONArray("message").getString(0);

                                if (errorMessage.equals("password is not strong enough")) {
                                    password.setError("Password is not strong enough");
                                    password.requestFocus();
                                } else {
                                    System.out.println(errorMessage);
                                }
                            } catch (IOException | JSONException e) {
                                    System.out.println("Error: "+response.message());
                                System.out.println("Error: "+e.getMessage());

                            }

                        }
                        }

                    @Override
                    public void onFailure(Call<LoggedInUser> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(regist.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Initializes the views and buttons in the registration activity.
     */
    public void init() {
        password = ((TextInputLayout)findViewById(R.id.regpassword)).getEditText();
        lastName = ((TextInputLayout)findViewById(R.id.reglastname)).getEditText();
        firstName = ((TextInputLayout)findViewById(R.id.regfirstname)).getEditText();
        email = ((TextInputLayout)findViewById(R.id.regemail)).getEditText();
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);
    }
}
