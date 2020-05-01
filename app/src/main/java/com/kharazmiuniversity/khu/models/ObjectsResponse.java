package com.kharazmiuniversity.khu.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectsResponse
{
    @SerializedName("group_records")
    private List<Group> groups;


    @SerializedName("channel_records")
    private List<Channel> channels;


    public ObjectsResponse() {
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }
}
