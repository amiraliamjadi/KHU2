package com.kharazmiuniversity.khu.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadImageController
{
    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(KhuAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
