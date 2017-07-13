package com.example.samsung.qiwi_users_balances.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QiwisUsersAPI {

    @GET("index.json")
    Call<JsonQiwisUsers> getUsers();

    @GET("users/{id}/index.json")
    Call<JsonQiwisUsersBalances> getBalancesById(@Path("id") int id);

}
