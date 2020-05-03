package com.kharazmiuniversity.khu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kharazmiuniversity.khu.data.ChannelMessageController;
import com.kharazmiuniversity.khu.data.KhuAPI;
import com.kharazmiuniversity.khu.models.ChannelMessage;
import com.kharazmiuniversity.khu.models.RequestChannelMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChannelAdminActivity extends AppCompatActivity
{
    private RecyclerView mMessageRecycler;
    public ChannelMessageAdapter mMessageAdapter;


    public List<ChannelMessage> messageListChannel = new ArrayList<>();

    private ProgressBar progressBar;


    private WebSocket webSocket;





    KhuAPI.ChannelMessageCallback channelMessageCallback = new KhuAPI.ChannelMessageCallback() {
        @Override
        public void onResponse(List<ChannelMessage> channelMessageList, boolean isMessageAvailable)
        {

            if (isMessageAvailable)
            {
                if (progressBar.isShown())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }


                messageListChannel.clear();

                messageListChannel.addAll(channelMessageList);
                mMessageAdapter.notifyDataSetChanged();
            }
            else
            {
                if (progressBar.isShown())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                Toast.makeText(getApplicationContext(),"no message here",Toast.LENGTH_SHORT).show();

            }


        }

        @Override
        public void onFailure(String cause)
        {

        }
    };




    @Override
    protected void onStop()
    {
        super.onStop();
        webSocket.cancel();
    }


    @Override
    protected void onStart() {
        super.onStart();



        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new ChannelMessageAdapter(this, messageListChannel);
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));


        progressBar.setVisibility(View.VISIBLE);


        ChannelMessageController channelMessageController = new ChannelMessageController(channelMessageCallback);
        RequestChannelMessage requestChannelMessage = new RequestChannelMessage();

        requestChannelMessage.setChannelId(GroupAdapter.objectId);

        channelMessageController.start(requestChannelMessage);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);


        progressBar = findViewById(R.id.in_chat_menu_progress_bar);

        //


        EditText messageBox = (EditText) findViewById(R.id.edittext_chatbox) ;
        Button sendButton = (Button) findViewById(R.id.button_chatbox_send);

        instantiateWebsocket();


        JSONObject userConnection = new JSONObject();
        try {
            userConnection.put("authorization_status",false);
            userConnection.put("channel_id_athorize",GroupAdapter.objectId);
            userConnection.put("group_id_athorize",0);
            userConnection.put("username_athorize",MyPreferenceManager.getInstance(ChannelAdminActivity.this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        webSocket.send(userConnection.toString());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String message = messageBox.getText().toString();
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cDate);

                JSONObject data = new JSONObject();

                try {
                    data.put("authorization_status",true);
                    data.put("username",MyPreferenceManager.getInstance(ChannelAdminActivity.this).getUsername());
                    data.put("message",message);
                    data.put("object_id", GroupAdapter.objectId);
                    data.put("channel_status",true);
                    data.put("group_status",false);
                    data.put("date",fDate);
                    data.put("user_name", MyPreferenceManager.getInstance(ChannelAdminActivity.this).getUser_name());

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }


                if (!message.isEmpty())
                {
                    webSocket.send(data.toString());
                    messageBox.setText("");

                    // add sent item to recycler

                    ChannelMessage channelMessage = new ChannelMessage();
                    channelMessage.setText(message);
                    channelMessage.setUsername(MyPreferenceManager.getInstance(ChannelAdminActivity.this).getUsername());


                    channelMessage.setDate(fDate);

                    mMessageAdapter.insertSent(channelMessage);


                }

            }
        });




    }




    private void instantiateWebsocket()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://10.0.2.2:8080").build();
        ChannelAdminActivity.SocketListener socketListener = new ChannelAdminActivity.SocketListener(this);
        webSocket = client.newWebSocket(request,socketListener);
    }

    public class SocketListener extends WebSocketListener
    {
        public ChannelAdminActivity channelAdminActivity;

        public SocketListener(ChannelAdminActivity channelAdminActivity)
        {
            this.channelAdminActivity = channelAdminActivity;
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


            channelAdminActivity.runOnUiThread(new Runnable() {
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

            channelAdminActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Connection established",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }












}
