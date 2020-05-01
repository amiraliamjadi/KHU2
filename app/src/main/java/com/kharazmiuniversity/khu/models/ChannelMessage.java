package com.kharazmiuniversity.khu.models;

import com.google.gson.annotations.SerializedName;

public class ChannelMessage
{
    @SerializedName("user_name")
    private String name;

    @SerializedName("text")
    private String text;

    @SerializedName("channel_message_id")
    private String id;

    @SerializedName("channelmessages_time")
    private String date;

    @SerializedName("username")
    private String username;








    public ChannelMessage() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
