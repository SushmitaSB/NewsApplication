package com.example.newsapplication.model.eventbus;

import com.example.newsapplication.model.News;

import java.util.List;

public class EventBusPojo {
    List<News> newsList;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public EventBusPojo(List<News> newsList){
        this.newsList = newsList;
    }
}
