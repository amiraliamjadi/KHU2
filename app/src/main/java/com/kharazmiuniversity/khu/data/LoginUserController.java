package com.kharazmiuniversity.khu.data;

import android.util.Log;

import com.google.gson.Gson;
import com.kharazmiuniversity.khu.models.ErrorResponse;
import com.kharazmiuniversity.khu.models.Token;
import com.kharazmiuniversity.khu.models.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginUserController
{

    KhuAPI.LoginUserCallback loginUserCallback;

    public LoginUserController(KhuAPI.LoginUserCallback loginUserCallback) {
        this.loginUserCallback = loginUserCallback;
    }




    public void start(User user)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KhuAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KhuAPI khuAPI = retrofit.create(KhuAPI.class);
        Call<Token> call = khuAPI.loginUser(user);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response)
            {
                if (response.isSuccessful())
                {
                    loginUserCallback.onResponse(true ,null ,response.body() );

                }

                else {
                    try
                    {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(),ErrorResponse.class);
                        loginUserCallback.onResponse(false, errorResponse ,null);
                    }
                    catch (IOException e)
                    {
                        System.err.println(e.getMessage());
                    }

                }


            }

            @Override
            public void onFailure(Call<Token> call, Throwable t)
            {
                System.out.println("a");
                loginUserCallback.onFailure(t.getCause().getMessage());
            }
        });

        /*call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response)
            {

                if (response.isSuccessful())
                {
                    System.out.println("d");
                    TokenResponse tokenResponse = response.body();
                    loginUserCallback.onResponse(true ,null ,tokenResponse );

                }
                else {
                    try
                    {
                        System.out.println("e");
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(),ErrorResponse.class);
                        loginUserCallback.onResponse(false, errorResponse ,null);
                    }
                    catch (IOException e)
                    {
                        System.err.println(e.getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t)
            {
                loginUserCallback.onFailure(t.getCause().getMessage());
                System.out.println("f");

            }
        });*/

    }
}
