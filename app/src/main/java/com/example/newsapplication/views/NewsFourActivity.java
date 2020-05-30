package com.example.newsapplication.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.newsapplication.R;
import com.example.newsapplication.adapter.AdapterForNewsOne;
import com.example.newsapplication.model.News;
import com.example.newsapplication.model.eventbus.EventBusPojo;
import com.example.newsapplication.servermodel.ServerManager;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFourActivity extends AppCompatActivity {

    @BindView(R.id.rvId4)
    RecyclerView recyclerView;

    @BindView(R.id.mainLayout4)
    LinearLayout linearLayout;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_four);
        ButterKnife.bind(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        ServerManager serverManager = new ServerManager(NewsFourActivity.this);
        serverManager.fetchDataForUpdateTechCrunch();
        Snackbar snackbar;
        snackbar = Snackbar.make(linearLayout, "Double click to view news", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        snackbar.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusPojo(EventBusPojo eventBusPojo) {
        List<News> newsList = eventBusPojo.getNewsList();
        if (newsList != null) {
            mAdapter = new AdapterForNewsOne(NewsFourActivity.this, newsList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}