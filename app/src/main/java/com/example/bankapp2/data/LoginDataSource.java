package com.example.bankapp2.data;


import com.example.bankapp2.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            return new Result.Success<>(null);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
}