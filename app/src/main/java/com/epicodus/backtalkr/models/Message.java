package com.epicodus.backtalkr.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 3/30/16.
 */

@Parcel
public class Message {
    String content;

    private Message() {

    }

    private Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}