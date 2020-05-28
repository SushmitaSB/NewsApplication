package com.example.newsapplication.servermodel;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.newsapplication.NewsOneActivity;
import com.example.newsapplication.adapter.AdapterForNewsOne;
import com.example.newsapplication.model.ApiClient;
import com.example.newsapplication.model.News;
import com.example.newsapplication.model.RetrofitApi;
import com.example.newsapplication.model.UpdateNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerManager {
    public static final String TAG = "ServerManager";
    private Context mContext;
    private RetrofitApi retrofitApi;
    private UpdateNews updateNews = null;

    public ServerManager(Context mContext){
        this.mContext = mContext;
        retrofitApi = ApiClient.getClient().create(RetrofitApi.class);
    }
    public UpdateNews fetchDataFromServer(){
        Call<UpdateNews> call = retrofitApi.getNews("bitcoin", "2020-04-27","publishedAt","35de1cffa06a48548d318541b675c79a");


//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    updateNews = call.execute().body();
//                }catch (Exception ex){
//                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        call.enqueue(new Callback<UpdateNews>() {
            @Override
            public void onResponse(Call<UpdateNews> call, Response<UpdateNews> response) {
                updateNews = response.body();

              //  List<News> newsList = updateNews.getArticles();
            }

            @Override
            public void onFailure(Call<UpdateNews> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        if (updateNews != null){
            return updateNews;
        }else {
            return updateNews;
        }

    }
}
