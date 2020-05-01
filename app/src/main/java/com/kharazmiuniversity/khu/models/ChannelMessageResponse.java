package com.kharazmiuniversity.khu.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelMessageResponse
{
    @SerializedName("records")
    private List<ChannelMessage> channelMessages;


    public List<ChannelMessage> getChannelMessages() {
        return channelMessages;
    }

    public void setChannelMessages(List<ChannelMessage> channelMessages) {
        this.channelMessages = channelMessages;
    }

    public ChannelMessageResponse()
    {

    }
}
