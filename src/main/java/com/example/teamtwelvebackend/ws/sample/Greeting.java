package com.example.teamtwelvebackend.ws.sample;

public class Greeting {

    private String type;

    private String content;

    public Greeting() {
    }

    public Greeting(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
