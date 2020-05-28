package com.example.newsapplication.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {

   String Base_Url = "https://newsapi.org";

    @GET("/v2/everything")
    Call<UpdateNews> getNews(@Query("q") String bitcoin,
                             @Query("from") String from,
                             @Query("sortBy") String publishedAt,
                             @Query("apiKey") String apiKey);

    @GET("/v2/top-headlines")
    Call<UpdateHeadLine> getHeadLines(@Query("country") String country,
                                      @Query("category") String category,
                                      @Query("apiKey") String apiKey);

}
