package com.kharazmiuniversity.khu.data;

import com.kharazmiuniversity.khu.models.ChannelMessageResponse;
import com.kharazmiuniversity.khu.models.GroupMessageResponse;
import com.kharazmiuniversity.khu.models.RequestChannelMessage;
import com.kharazmiuniversity.khu.models.RequestGroupMessage;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChannelMessageController
{
    KhuAPI.ChannelMessageCallback channelMessageCallback;

    public ChannelMessageController(KhuAPI.ChannelMessageCallback channelMessageCallback) {
        this.channelMessageCallback = channelMessageCallback;
    }

    public void start(RequestChannelMessage requestChannelMessage)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KhuAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KhuAPI khuAPI = retrofit.create(KhuAPI.class);
        Call<ChannelMessageResponse> call = khuAPI.channelMessageMethod(requestChannelMessage);

        call.enqueue(new Callback<ChannelMessageResponse>() {
            @Override
            public void onResponse(Call<ChannelMessageResponse> call, Response<ChannelMessageResponse> response)
            {
                if (response.isSuccessful())
                {
                    channelMessageCallback.onResponse(response.body().getChannelMessages() , true);


                }
                else {
                    channelMessageCallback.onResponse(new ArrayList<>(), false);
                }
            }

            @Override
            public void onFailure(Call<ChannelMessageResponse> call, Throwable t)
            {
                channelMessageCallback.onFailure(t.getMessage());
            }
        });


    }
}
