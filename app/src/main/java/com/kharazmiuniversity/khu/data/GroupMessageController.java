package com.kharazmiuniversity.khu.data;

import android.util.Log;

import com.kharazmiuniversity.khu.models.GroupMessageResponse;
import com.kharazmiuniversity.khu.models.RequestGroupMessage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupMessageController
{
    KhuAPI.GroupMessageCallback groupMessageCallback;

    public GroupMessageController(KhuAPI.GroupMessageCallback groupMessageCallback) {
        this.groupMessageCallback = groupMessageCallback;
    }

    public void start(RequestGroupMessage requestGroupMessage)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KhuAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KhuAPI khuAPI = retrofit.create(KhuAPI.class);
        Call<GroupMessageResponse> call = khuAPI.groupMessageMethod(requestGroupMessage);

        call.enqueue(new Callback<GroupMessageResponse>() {
            @Override
            public void onResponse(Call<GroupMessageResponse> call, Response<GroupMessageResponse> response)
            {
                if (response.isSuccessful())
                {
                   groupMessageCallback.onResponse(response.body().getGroupMessages() , true);


                }
                else {
                    groupMessageCallback.onResponse(new ArrayList<>(), false);
                }
            }

            @Override
            public void onFailure(Call<GroupMessageResponse> call, Throwable t)
            {
                groupMessageCallback.onFailure(t.getMessage());
            }
        });


    }
}
