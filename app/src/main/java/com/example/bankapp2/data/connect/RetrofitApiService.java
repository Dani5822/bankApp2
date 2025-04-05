package com.example.bankapp2.data.connect;

import com.example.bankapp2.data.model.Card;
import com.example.bankapp2.data.model.Expense;
import com.example.bankapp2.data.model.Income;
import com.example.bankapp2.data.model.LoggedInUser;
import com.example.bankapp2.data.model.RepeatableTransaction;
import com.example.bankapp2.data.model.currency;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface defining the API endpoints for the Retrofit service.
 */
public interface RetrofitApiService {

    /**
     * Retrieves all users.
     * @return A Call object to fetch the list of all users.
     */
    @GET("user")
    Call<List<LoggedInUser>> getAllUser();

    /**
     * Retrieves a user by their ID.
     * @param userid The ID of the user.
     * @param token The authorization token.
     * @return A Call object to fetch the user.
     */
    @GET("/user/userbank/{id}")
    Call<LoggedInUser> getUserById(@Path("id") String userid, @Header("authorization") String token);

    /**
     * Retrieves all expenses by card ID.
     * @param cardid The ID of the card.
     * @param token The authorization token.
     * @return A Call object to fetch the expenses.
     */
    @GET("/accounts/allex/{id}")
    Call<Expense[]> getallexbycardid(@Path("id") String cardid, @Header("authorization") String token);

    /**
     * Retrieves all incomes by card ID.
     * @param cardid The ID of the card.
     * @param token The authorization token.
     * @return A Call object to fetch the incomes.
     */
    @GET("/accounts/allin/{id}")
    Call<Income[]> getallinbycardid(@Path("id") String cardid, @Header("authorization") String token);

    /**
     * Logs in a user.
     * @param username The email of the user.
     * @param password The password of the user.
     * @return A Call object to log in the user.
     */
    @FormUrlEncoded
    @POST("user/login")
    Call<LoggedInUser> loginUser(
            @Field("email") String username,
            @Field("password") String password
    );

    /**
     * Registers a new user.
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return A Call object to register the user.
     */
    @FormUrlEncoded
    @POST("user")
    Call<LoggedInUser> registerUser(
            @Field("firstName") String firstname,
            @Field("lastName") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Retrieves the currency exchange rate for a given currency.
     * @param currency The currency code.
     * @return A Call object to fetch the currency exchange rate.
     */
    @GET("currency-api@latest/v1/currencies/{currency}.json")
    Call<currency> getEur(@Path("currency") String currency);

    /**
     * Retrieves the currency exchange rate for a given currency on a specific date.
     * @param date The date for the exchange rate.
     * @param currency The currency code.
     * @return A Call object to fetch the currency exchange rate.
     */
    @GET("currency-api@{date}/v1/currencies/{currencies}.json")
    Call<currency> GetCurrencyByDate(@Path("date") String date, @Path("currencies") String currency);

    /**
     * Retrieves the currency exchange rates in JSON format.
     * @param currency The currency code.
     * @return A Call object to fetch the currency exchange rates in JSON format.
     */
    @GET("currency-api@latest/v1/currencies/{currency}.json")
    Call<JsonObject> getRatesJson(@Path("currency") String currency);

    /**
     * Creates a new card.
     * @param token The authorization token.
     * @param currency The currency of the card.
     * @param ownerName The name of the card owner.
     * @param userId The ID of the user.
     * @return A Call object to create the card.
     */
    @POST("accounts")
    @FormUrlEncoded
    Call<Card> createCard(@Header("authorization") String token, @Field("currency") String currency, @Field("ownerName") String ownerName, @Field("userId") String userId);

    /**
     * Retrieves users by card ID.
     * @param cardid The ID of the card.
     * @param token The authorization token.
     * @return A Call object to fetch the users.
     */
    @GET("accounts/user/{id}")
    Call<Card[]> getUsersByCardID(@Path("id") String cardid, @Header("authorization") String token);

    /**
     * Retrieves card users.
     * @param cardid The ID of the card.
     * @param token The authorization token.
     * @return A Call object to fetch the card users.
     */
    @GET("accounts/alluser/{id}")
    Call<Card> getCardUsers(@Path("id") String cardid, @Header("authorization") String token);

    /**
     * Disconnects a user from a card.
     * @param token The authorization token.
     * @param cardid The ID of the card.
     * @param userId The ID of the user.
     * @return A Call object to disconnect the user.
     */
    @PATCH("accounts/disconnect/{id}")
    @FormUrlEncoded
    Call<Card> disconnectUser(@Header("authorization") String token, @Path("id") String cardid, @Field("userId") String userId);

    /**
     * Updates a card.
     * @param token The authorization token.
     * @param cardid The ID of the card.
     * @param currency The currency of the card.
     * @return A Call object to update the card.
     */
    @PATCH("accounts/{id}")
    @FormUrlEncoded
    Call<Card> updateCard(@Header("authorization") String token, @Path("id") String cardid, @Field("currency") String currency);

    /**
     * Updates a card user.
     * @param token The authorization token.
     * @param cardid The ID of the card.
     * @param userId The ID of the user.
     * @return A Call object to update the card user.
     */
    @PATCH("accounts/user/{id}")
    @FormUrlEncoded
    Call<Card> updateCardUser(@Header("authorization") String token, @Path("id") String cardid, @Field("userId") String userId);

    /**
     * Updates the card user list by email.
     * @param token The authorization token.
     * @param cardid The ID of the card.
     * @param email The email of the user.
     * @return A Call object to update the card user list.
     */
    @PATCH("accounts/user/email/{id}")
    @FormUrlEncoded
    Call<Card> updateCardUserList(@Header("authorization") String token, @Path("id") String cardid, @Field("email") String email);

    /**
     * Deletes a card.
     * @param token The authorization token.
     * @param cardid The ID of the card.
     * @return A Call object to delete the card.
     */
    @DELETE("accounts/{id}")
    Call<Card> deleteCard(@Header("authorization") String token, @Path("id") String cardid);

    /**
     * Creates a new income transaction.
     * @param token The authorization token.
     * @param amount The amount of the income.
     * @param description The description of the income.
     * @param cardId The ID of the bank account.
     * @param category The category of the income.
     * @param userId The ID of the user.
     * @return A Call object to create the income.
     */
    @POST("Income")
    @FormUrlEncoded
    Call<Income> createIncome(
            @Header("authorization") String token,
            @Field("total") Double amount,
            @Field("description") String description,
            @Field("bankAccountId") String cardId,
            @Field("category") String category,
            @Field("userId") String userId);

    /**
     * Creates a new expense transaction.
     * @param token The authorization token.
     * @param amount The amount of the expense.
     * @param description The description of the expense.
     * @param cardId The ID of the bank account.
     * @param category The category of the expense.
     * @param userId The ID of the user.
     * @return A Call object to create the expense.
     */
    @POST("expense")
    @FormUrlEncoded
    Call<Expense> createExpense(
            @Header("authorization") String token,
            @Field("total") Double amount,
            @Field("description") String description,
            @Field("bankAccountId") String cardId,
            @Field("category") String category,
            @Field("userId") String userId
    );

    /**
     * Creates a new repeatable transaction.
     * @param token The authorization token.
     * @param amount The total amount.
     * @param category The category of the transaction.
     * @param description The description of the transaction.
     * @param cardId The ID of the bank account.
     * @param repeatAmount The repeat amount.
     * @param repeatMetric The repeat metric.
     * @param repeatStart The start date of the repeat.
     * @param repeatEnd The end date of the repeat.
     * @param userId The ID of the user.
     * @param name The name of the transaction.
     * @return A Call object to create the repeatable transaction.
     */
    @POST("repeatabletransaction")
    @FormUrlEncoded
    Call<RepeatableTransaction> createRepeatableTransaction(
            @Header("authorization") String token,
            @Field("total") Double amount,
            @Field("category") String category,
            @Field("description") String description,
            @Field("accountId") String cardId,
            @Field("repeatAmount") int repeatAmount,
            @Field("repeatMetric") String repeatMetric,
            @Field("repeatStart") String repeatStart,
            @Field("repeatEnd") String repeatEnd,
            @Field("userId") String userId,
            @Field("name") String name
    );

    /**
     * Updates a repeatable transaction.
     * @param token The authorization token.
     * @param accountid The ID of the account.
     * @param userId The ID of the user.
     * @return A Call object to update the repeatable transaction.
     */
    @PATCH("repeatabletransaction/update/{id}")
    @FormUrlEncoded
    Call<RepeatableTransaction> updateRepeatableTransactions(
            @Header("authorization") String token,
            @Path("id") String accountid,
            @Field("userId") String userId
    );

    /**
     * Retrieves a card by its ID.
     * @param cardid The ID of the card.
     * @param token The authorization token.
     * @return A Call object to fetch the card.
     */
    @GET("accounts/{id}")
    Call<Card> getCardById(@Path("id") String cardid, @Header("authorization") String token);

    /**
     * Creates a new transfer.
     * @param accessToken The authorization token.
     * @param total The total amount.
     * @param from The ID of the source account.
     * @param to The ID of the destination account.
     * @param userId The ID of the user.
     * @return A Call object to create the transfer.
     */
    @POST("accounts/transfer")
    @FormUrlEncoded
    Call<Card> createTransfer(@Header("authorization") String accessToken,
                              @Field("ammount") Double total,
                              @Field("accountfrom") String from,
                              @Field("accountto") String to,
                              @Field("userId") String userId);

    /**
     * Retrieves an expense by its ID.
     * @param expenseid The ID of the expense.
     * @param token The authorization token.
     * @return A Call object to fetch the expense.
     */
    @GET("expense/{id}")
    Call<Expense> getExpenseById(@Path("id") String expenseid, @Header("authorization") String token);

    /**
     * Retrieves an income by its ID.
     * @param incomeid The ID of the income.
     * @param token The authorization token.
     * @return A Call object to fetch the income.
     */
    @GET("income/{id}")
    Call<Income> getIncomeById(@Path("id") String incomeid, @Header("authorization") String token);

    /**
     * Retrieves a repeatable transaction by its ID.
     * @param repeatableTransactionId The ID of the repeatable transaction.
     * @param token The authorization token.
     * @return A Call object to fetch the repeatable transaction.
     */
    @GET("repeatabletransaction/{id}")
    Call<RepeatableTransaction> getRepeatableTransactionById(@Path("id") String repeatableTransactionId, @Header("authorization") String token);

    /**
     * Retrieves a repeatable transaction with expenses by its ID.
     * @param repeatableTransactionId The ID of the repeatable transaction.
     * @param token The authorization token.
     * @return A Call object to fetch the repeatable transaction with expenses.
     */
    @GET("repeatabletransaction/expense/{id}")
    Call<RepeatableTransaction> getRepeatableTransactionByIdWithExpenses(@Path("id") String repeatableTransactionId, @Header("authorization") String token);

    /**
     * Deletes a repeatable transaction.
     * @param repeatableTransactionId The ID of the repeatable transaction.
     * @param token The authorization token.
     * @return A Call object to delete the repeatable transaction.
     */
    @DELETE("repeatabletransaction/{id}")
    Call<RepeatableTransaction> deleteRepeatableTransaction(@Path("id") String repeatableTransactionId, @Header("authorization") String token);

    /**
     * Stops a repeatable transaction.
     * @param repeatableTransactionId The ID of the repeatable transaction.
     * @param token The authorization token.
     * @return A Call object to stop the repeatable transaction.
     */
    @DELETE("repeatabletransaction/stop/{id}")
    Call<RepeatableTransaction> stopRepeatableTransaction(@Path("id") String repeatableTransactionId, @Header("authorization") String token);

    /**
     * Deletes an expense transaction.
     * @param id The ID of the expense transaction.
     * @param token The authorization token.
     * @return A Call object to delete the expense transaction.
     */
    @DELETE("expense/{id}")
    Call<Expense> deleteExpenseTransaction(@Path("id") String id,@Header("authorization") String token);

    /**
     * Deletes an income transaction.
     * @param id The ID of the income transaction.
     * @param token The authorization token.
     * @return A Call object to delete the income transaction.
     */
    @DELETE("Income/{id}")
    Call<Income> deleteIncomeTransaction(@Path("id") String id,@Header("authorization") String token);

    /**
     * Retrieves all repeatable transactions by card ID.
     * @param cardid The ID of the card.
     * @param token The authorization token.
     * @return A Call object to fetch the repeatable transactions.
     */
    @GET("accounts/allrepeat/{id}")
    Call<RepeatableTransaction[]> getAllRepeatableTransactions(@Path("id") String cardid, @Header("authorization") String token);

    /**
     * Updates a repeatable transaction.
     * @param token The authorization token.
     * @param repeatableTransactionId The ID of the repeatable transaction.
     * @param amount The total amount.
     * @param category The category of the transaction.
     * @param description The description of the transaction.
     * @param repeatAmount The repeat amount.
     * @param repeatMetric The repeat metric.
     * @param repeatStart The start date of the repeat.
     * @param repeatEnd The end date of the repeat.
     * @param name The name of the transaction.
     * @return A Call object to update the repeatable transaction.
     */
    @PATCH("repeatabletransaction/{id}")
    @FormUrlEncoded
    Call<RepeatableTransaction> updateRepeatableTransaction(
            @Header("authorization") String token,
            @Path("id") String repeatableTransactionId,
            @Field("total") Double amount,
            @Field("category") String category,
            @Field("description") String description,
            @Field("repeatAmount") int repeatAmount,
            @Field("repeatMetric") String repeatMetric,
            @Field("repeatStart") String repeatStart,
            @Field("repeatEnd") String repeatEnd,
            @Field("name") String name
    );
}
