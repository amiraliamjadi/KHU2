package com.kharazmiuniversity.khu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class GroupChatActivity extends AppCompatActivity
{

    private WebSocket webSocket;
    private MessageAdapter messageAdapter;


    @Override
    protected void onStop()
    {
        super.onStop();
        webSocket.cancel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        ListView messageList = findViewById(R.id.message_list);
        final EditText messageBox = findViewById(R.id.message_box);
        TextView send = findViewById(R.id.send);


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


        messageAdapter = new MessageAdapter();
        messageList.setAdapter(messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String message = messageBox.getText().toString();
                JSONObject data = new JSONObject();

                try {
                    data.put("authorization_status",true);
                    data.put("username",MyPreferenceManager.getInstance(GroupChatActivity.this).getUsername());
                    data.put("message",message);
                    data.put("object_id", GroupAdapter.objectId);
                    data.put("channel_status",false);
                    data.put("group_status",true);
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (!message.isEmpty())
                {
                    webSocket.send(data.toString());
                    messageBox.setText("");

                    JSONObject jsonObject = new JSONObject();
                    try
                    {
                        jsonObject.put("message",message);
                        jsonObject.put("byServer",false);

                        messageAdapter.addItem(jsonObject);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    private void instantiateWebsocket()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://10.0.2.2:8080").build();
        SocketListener socketListener = new SocketListener(this);
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
                    JSONObject jsonObject = new JSONObject();
                    try
                    {

                        jsonObject.put("message", text);
                        jsonObject.put("byServer",true);


                        messageAdapter.addItem(jsonObject);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


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


    public class MessageAdapter extends BaseAdapter
    {

        List<JSONObject> messagesList = new ArrayList<>();

        @Override
        public int getCount() {
            return messagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return messagesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.message_list_item,parent,false );
            }


            TextView sentMessage = convertView.findViewById(R.id.sent_message);
            TextView recievedMessage = convertView.findViewById(R.id.recieved_message);

            JSONObject item = messagesList.get(position);

            try
            {
                if (item.getBoolean("byServer"))
                {
                    recievedMessage.setVisibility(View.VISIBLE);
                    recievedMessage.setText(item.getString("message"));
                    sentMessage.setVisibility(View.INVISIBLE);
                }
                else
                {
                    sentMessage.setVisibility(View.VISIBLE);
                    sentMessage.setText(item.getString("message"));
                    recievedMessage.setVisibility(View.INVISIBLE);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return convertView;

        }

        private void addItem (JSONObject item)
        {
            messagesList.add(item);
            notifyDataSetChanged();
        }


    }


}
