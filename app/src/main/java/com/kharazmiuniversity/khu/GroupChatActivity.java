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
import com.kharazmiuniversity.khu.data.GroupMessageController;
import com.kharazmiuniversity.khu.data.KhuAPI;
import com.kharazmiuniversity.khu.models.GroupMessage;
import com.kharazmiuniversity.khu.models.RequestGroupMessage;

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

public class GroupChatActivity extends AppCompatActivity
{

    private RecyclerView mMessageRecycler;
    public GroupMessageAdapter mMessageAdapter;
    public List<GroupMessage> messageList = new ArrayList<>();

    private ProgressBar progressBar;


    private WebSocket webSocket;




    KhuAPI.GroupMessageCallback groupMessageCallback = new KhuAPI.GroupMessageCallback() {
        @Override
        public void onResponse(List<GroupMessage> groupMessageList , boolean isMessageAvailable)
        {
            if (isMessageAvailable)
            {
                if (progressBar.isShown())
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }


                messageList.clear();

                messageList.addAll(groupMessageList);
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
        mMessageAdapter = new GroupMessageAdapter(this,messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));


        progressBar.setVisibility(View.VISIBLE);




        GroupMessageController groupMessageController = new GroupMessageController(groupMessageCallback);
        RequestGroupMessage requestGroupMessage = new RequestGroupMessage();

        requestGroupMessage.setGroupId(GroupAdapter.objectId);

        groupMessageController.start(requestGroupMessage);




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
            userConnection.put("group_id_athorize",GroupAdapter.objectId);
            userConnection.put("channel_id_athorize",0);
            userConnection.put("username_athorize",MyPreferenceManager.getInstance(GroupChatActivity.this).getUsername());
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
                    data.put("username",MyPreferenceManager.getInstance(GroupChatActivity.this).getUsername());
                    data.put("message",message);
                    data.put("object_id", GroupAdapter.objectId);
                    data.put("channel_status",false);
                    data.put("group_status",true);
                    data.put("date",fDate);
                    data.put("user_name", MyPreferenceManager.getInstance(GroupChatActivity.this).getUser_name());

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }


                if (!message.isEmpty())
                {
                    webSocket.send(data.toString());
                    messageBox.setText("");

                    // add sent item to recycler

                    GroupMessage groupMessage = new GroupMessage();
                    groupMessage.setText(message);
                    groupMessage.setUsername(MyPreferenceManager.getInstance(GroupChatActivity.this).getUsername());


                    groupMessage.setDate(fDate);

                    mMessageAdapter.insertSent(groupMessage);


                }

            }
        });




    }




    private void instantiateWebsocket()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://10.0.2.2:8080").build();
        GroupChatActivity.SocketListener socketListener = new GroupChatActivity.SocketListener(this);
        webSocket = client.newWebSocket(request,socketListener);
    }

    public class SocketListener extends WebSocketListener
    {
        public GroupChatActivity groupChatActivity;

        public SocketListener(GroupChatActivity groupChatActivity)
        {
            this.groupChatActivity = groupChatActivity;
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


            groupChatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {

                    // add received items to recycler

                    Gson gson = new Gson();
                    GroupMessage groupMessage = gson.fromJson(text,GroupMessage.class);

                    mMessageAdapter.insertSent(groupMessage);

                }
            });

        }



        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

            groupChatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Connection established",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }








}
