package com.kharazmiuniversity.khu;

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

public class ChannelActivity extends AppCompatActivity
{
    private WebSocket webSocket;
    private ChannelActivity.MessageAdapter messageAdapter;


    @Override
    protected void onStop()
    {
        super.onStop();
        webSocket.cancel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);


        ListView messageList = findViewById(R.id.message_list_channel);


        instantiateWebsocket();


        JSONObject userConnection = new JSONObject();
        try {
            userConnection.put("authorization_status",false);
            userConnection.put("channel_id_athorize",GroupAdapter.objectId);
            userConnection.put("group_id_athorize",0);
            userConnection.put("username_athorize",MyPreferenceManager.getInstance(ChannelActivity.this).getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        webSocket.send(userConnection.toString());




        messageAdapter = new ChannelActivity.MessageAdapter();
        messageList.setAdapter(messageAdapter);



    }

    private void instantiateWebsocket()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://10.0.2.2:8080").build();
        ChannelActivity.SocketListener socketListener = new ChannelActivity.SocketListener(this);
        webSocket = client.newWebSocket(request,socketListener);
    }

    public class SocketListener extends WebSocketListener
    {
        public ChannelActivity channelActivity;

        public SocketListener(ChannelActivity channelActivity)
        {
            this.channelActivity = channelActivity;
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


            channelActivity.runOnUiThread(new Runnable() {
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

            channelActivity.runOnUiThread(new Runnable() {
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
