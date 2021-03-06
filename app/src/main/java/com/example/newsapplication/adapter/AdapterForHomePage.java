package com.example.newsapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.contexttag.Tag;
import com.example.newsapplication.views.NewsOneActivity;
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

                Intent intent = new Intent(context, NewsOneActivity.class);
                            intent.putExtra(Tag.ADAPTERPOSITION,i);
                            context.startActivity(intent);


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
