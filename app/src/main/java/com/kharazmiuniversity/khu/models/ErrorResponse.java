package com.kharazmiuniversity.khu.models;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse
{
    @SerializedName("message")
    String error;

    public ErrorResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
