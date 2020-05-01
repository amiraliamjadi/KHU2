package com.kharazmiuniversity.khu.models;

import com.google.gson.annotations.SerializedName;

public class Channel
{
    @SerializedName("channel_id")
    private String id;

    @SerializedName("channel_name")
    private String name;


    public Channel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
