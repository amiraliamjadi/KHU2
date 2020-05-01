package com.kharazmiuniversity.khu.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupMessageResponse
{

    @SerializedName("records")
    private List<GroupMessage> groupMessages;


    public List<GroupMessage> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(List<GroupMessage> groupMessages) {
        this.groupMessages = groupMessages;
    }

    public GroupMessageResponse()
    {

    }
}
