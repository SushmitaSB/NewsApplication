package com.example.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.newsapplication.adapter.AdapterForNewsOne;
import com.example.newsapplication.model.News;
import com.example.newsapplication.model.RetrofitApi;
import com.example.newsapplication.model.UpdateNews;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsOneActivity extends AppCompatActivity {

    @BindView(R.id.recyclerForNewsOne)
    RecyclerView recyclerView;

    private RecyclerView.Adapter mAdapter;

    private ArrayList<String> mDataSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_one);
        ButterKnife.bind(this);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            ActionBar actionBar = getActionBar();
//            if (actionBar != null) {
//                actionBar.setTitle("RecyclerView");
//            }
//        }

        // Layout Managers:
        
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // Adapter:

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi api = retrofit.create(RetrofitApi.class);
        Call<UpdateNews> call = api.getNews("bitcoin", "2020-04-27","publishedAt","35de1cffa06a48548d318541b675c79a");
        call.enqueue(new Callback<UpdateNews>() {
            @Override
            public void onResponse(Call<UpdateNews> call, Response<UpdateNews> response) {
                UpdateNews updateNews = response.body();
                List<News> newsList = updateNews.getArticles();
//                for (News n : newsList){
//                    Log.d("author", n.getAuthor());
//                    Log.d("title", n.getTitle());
//                    Log.d("description", n.getDescription());
//                    Log.d("publishedAt", n.getPublishedAt());
//                }

                if (newsList != null){
                    mAdapter = new AdapterForNewsOne(NewsOneActivity.this, newsList);
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<UpdateNews> call, Throwable t) {
                Toast.makeText(NewsOneActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



   }


}
