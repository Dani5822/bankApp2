package com.example.bankapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.connect.RetrofitClient;
import com.example.bankapp2.data.model.LoggedInUser;
import com.example.bankapp2.ui.login.LoginActivity;

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
                System.out.println(pass);
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
                RetrofitApiService apiService = RetrofitClient.getInstance().create(RetrofitApiService.class);
                System.out.println(first+" "+last+" "+mail+" "+pass);
                Call<LoggedInUser> call = apiService.registerUser(first, last, mail, pass);
                call.enqueue(new Callback<LoggedInUser>() {
                    @Override
                    public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                        System.out.println(response.body());
                        Intent intent = new Intent(regist.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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
        password = findViewById(R.id.regpassword);
        lastName = findViewById(R.id.reglastname);
        firstName = findViewById(R.id.regfirstname);
        email = findViewById(R.id.regemail);
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);
    }
}
