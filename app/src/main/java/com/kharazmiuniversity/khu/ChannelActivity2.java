package com.kharazmiuniversity.khu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kharazmiuniversity.khu.models.ChannelMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChannelActivity2 extends AppCompatActivity
{
    private RecyclerView mMessageRecycler;
    public ChannelMessageAdapter mMessageAdapter;


    private WebSocket webSocket;



    @Override
    protected void onStop()
    {
        super.onStop();
        webSocket.cancel();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_channel);


        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new ChannelMessageAdapter(this, GroupAdapter.messageListChannel);
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));


        //



        instantiateWebsocket();


        JSONObject userConnection = new JSONObject();
        try {
            userConnection.put("authorization_status",false);
            userConnection.put("channel_id_athorize",GroupAdapter.objectId);
            userConnection.put("group_id_athorize",0);
            userConnection.put("username_athorize",MyPreferenceManager.getInstance(ChannelActivity2.this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        webSocket.send(userConnection.toString());



    }




    private void instantiateWebsocket()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://10.0.2.2:8080").build();
        ChannelActivity2.SocketListener socketListener = new ChannelActivity2.SocketListener(this);
        webSocket = client.newWebSocket(request,socketListener);
    }

    public class SocketListener extends WebSocketListener
    {
        public ChannelActivity2 channelActivity2;

        public SocketListener(ChannelActivity2 channelActivity2)
        {
            this.channelActivity2 = channelActivity2;
        }


        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull final String text) {
            super.onMessage(webSocket, text);


            channelActivity2.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {

                    // add received items to recycler

                    Gson gson = new Gson();
                    ChannelMessage channelMessage = gson.fromJson(text,ChannelMessage.class);

                    mMessageAdapter.insertSent(channelMessage);

                }
            });

        }



        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

            channelActivity2.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Connection established",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }













}
