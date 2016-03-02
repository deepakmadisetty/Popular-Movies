package com.example.android.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Deepak on 3/2/16.
 */
public class Review {
    private String id;
    private String author;
    private String content;

    public Review(JSONObject review) throws JSONException {
        this.id = review.getString("id");
        this.author = review.getString("author");
        this.content = review.getString("content");
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
