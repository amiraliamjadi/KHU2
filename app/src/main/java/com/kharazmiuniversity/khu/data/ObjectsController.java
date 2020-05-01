package com.kharazmiuniversity.khu.data;

import com.kharazmiuniversity.khu.models.GetObject;
import com.kharazmiuniversity.khu.models.ObjectsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ObjectsController
{

    KhuAPI.getObjectsCallback objectsCallback;

    public ObjectsController(KhuAPI.getObjectsCallback objectsCallback) {
        this.objectsCallback = objectsCallback;
    }

    public void start(GetObject getObject)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KhuAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KhuAPI khuAPI = retrofit.create(KhuAPI.class);
        Call<ObjectsResponse> call = khuAPI.getObjectsMethod(getObject);
        call.enqueue(new Callback<ObjectsResponse>() {
            @Override
            public void onResponse(Call<ObjectsResponse> call, Response<ObjectsResponse> response)
            {
                if (response.isSuccessful())
                {
                    objectsCallback.onResponse(response.body().getGroups() , response.body().getChannels());
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<ObjectsResponse> call, Throwable t)
            {
                objectsCallback.onFailure(t.getMessage());
            }
        });

    }
}
