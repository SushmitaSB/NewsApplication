package com.example.newsapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.NewsFiveActivity;
import com.example.newsapplication.NewsFourActivity;
import com.example.newsapplication.NewsOneActivity;
import com.example.newsapplication.NewsThreeActivity;
import com.example.newsapplication.NewsTwoActivity;
import com.example.newsapplication.R;

import java.util.ArrayList;

public class AdapterForHomePage extends RecyclerView.Adapter <AdapterForHomePage.MyViewHolder>{

    Context context;
    ArrayList<String> arrayList;

    public AdapterForHomePage(Context context, ArrayList<String> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_layout_for_home_page_recycler, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {

        viewHolder.textView.setText(arrayList.get(i));

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "id:" + view.getId(), Toast.LENGTH_SHORT).show();

                switch (i){
                    case 0:
                        Intent intent = new Intent(context, NewsOneActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        Intent in = new Intent(context, NewsTwoActivity.class);
                        context.startActivity(in);
                        break;
                    case 2:
                        Intent newIntent = new Intent(context, NewsThreeActivity.class);
                        context.startActivity(newIntent);
                        break;
                    case 3: Intent i = new Intent(context, NewsFourActivity.class);
                        context.startActivity(i);
                        break;
                    case 4: Intent pass = new Intent(context, NewsFiveActivity.class);
                        context.startActivity(pass);
                        break;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewId);
        }

    }
}
