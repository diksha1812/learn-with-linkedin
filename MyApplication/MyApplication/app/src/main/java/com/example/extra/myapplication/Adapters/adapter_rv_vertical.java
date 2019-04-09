package com.example.extra.myapplication.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.extra.myapplication.Constants;
import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;
import com.google.firebase.database.DataSnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class adapter_rv_vertical extends RecyclerView.Adapter<adapter_rv_vertical.ViewHolder>{

    private adapter_rv_horizontal.adapter_rv_h_listener context;
    List<Course> courses;
    DataSnapshot dataSnapshot;
    List<String> topicNames;

    public adapter_rv_vertical(DataSnapshot dataSnapshot, adapter_rv_horizontal.adapter_rv_h_listener context){
        this.dataSnapshot = dataSnapshot;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        RecyclerView.Adapter adapter;
        public ViewHolder(View v) {
            super(v);
            recyclerView = v.findViewById(R.id.rv_h);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @NonNull
    @Override
    public adapter_rv_vertical.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_horizontal, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rv_vertical.ViewHolder viewHolder, int i) {

        topicNames = new ArrayList<>();
        courses = new ArrayList<>();
        for(DataSnapshot temp : dataSnapshot.getChildren()) {
            topicNames.add(temp.child("TopicName").getValue().toString());

            Log.e(Constants.TAG, "Topic Index: " + temp.getKey());//working
            Log.e(Constants.TAG, "Topic Name: " + temp.child("TopicName").getValue());
        }
        for (DataSnapshot t : dataSnapshot.child(String.valueOf(i)).getChildren()){
            if(t.child("CourseName").getValue()!=null){

                if(t.child("CourseName")!=null){

                    Course c = new Course();
                    c.setCourseId(Integer.parseInt(t.getKey()));
                    c.setCourseName(t.child("CourseName").getValue().toString());
                    Log.e("zzzzzzzzzzzzz", String.valueOf(t.getKey()));
                    c.setCourseId(Integer.parseInt(String.valueOf(t.getKey())));
                    courses.add(c);
                }
            }

            Log.e(Constants.TAG, "Course Index: "+t.getKey());
            Log.e(Constants.TAG, "Course Name: "+t.child("CourseName").getValue());
        }

        viewHolder.adapter = new adapter_rv_horizontal(topicNames.get(i), courses, context, i);
        viewHolder.recyclerView.setAdapter(viewHolder.adapter);
    }

    @Override
    public int getItemCount() {
        return (int)(dataSnapshot.getChildrenCount());
    }
}

