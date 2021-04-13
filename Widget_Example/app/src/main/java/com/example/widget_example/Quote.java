package com.example.widget_example;

public class Quote implements Comparable< Quote >{
    private String quote;
    private String topic;
    private String author;
    private int id;

    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // Convert JSONObject of quote to string in correct format
    @Override
    public String toString() { return quote + "\n\n - " + author + "\n Topic: " + topic; }
    @Override
    public int compareTo(Quote o) { return this.getId().compareTo(o.getId()); }
}
