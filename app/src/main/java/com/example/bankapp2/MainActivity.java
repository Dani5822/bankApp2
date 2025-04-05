package com.example.bankapp2;



import static com.example.bankapp2.data.connect.RetrofitClient.getInstance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bankapp2.data.connect.RetrofitApiService;
import com.example.bankapp2.data.model.LoggedInUser;
import com.example.bankapp2.databinding.ActivityMainBinding;
import com.example.bankapp2.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Main activity of the BankApp2 application.
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private boolean nightModeEnabled;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public LoggedInUser user;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * 
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE);
        nightModeEnabled = sharedPreferences.getBoolean("NIGHT_MODE", false);

        if (nightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    /**
     * Initializes the user data by making a network call to fetch user details.
     */
    private void init() {
        String id = getIntent().getExtras().get("userid").toString();
        RetrofitApiService apiService = getInstance().create(RetrofitApiService.class);
        apiService.getUserById(id,((global) getApplication()).getAccess_token()).enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                if (response.isSuccessful()) {
                    response.body().toString();
                    user = response.body();

                } else {
                    if(response.message().equals("Unauthorized")){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                System.out.println(t.getMessage());
            }


        });
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * 
     * @param menu The options menu in which you place your items.
     * @return boolean You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's activity hierarchy from the action bar.
     * 
     * @return boolean Return true to navigate up, false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * 
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.themechange) {
            nightModeEnabled = !nightModeEnabled;
            if (nightModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            editor = sharedPreferences.edit();
            editor.putBoolean("NIGHT_MODE", nightModeEnabled);
            editor.apply();
        } else if (id==R.id.logout){
            ((global) getApplication()).setAccess_token(null);
            ((global) getApplication()).setId(null);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the logged-in user.
     * 
     * @return LoggedInUser The logged-in user.
     */
    public LoggedInUser getUser() {
        return user;
    }
}
