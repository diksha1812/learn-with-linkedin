package com.example.extra.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.extra.myapplication.Constants;
import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class adapter_rv_search extends RecyclerView.Adapter<adapter_rv_search.ViewHolder> implements Filterable {
    private List<Course> course_list;
    private Context context;
    private List<Course> course_list_filtered;
    private adapter_rv_search_listener listener;


    public adapter_rv_search (Context context, List<Course> course_list, adapter_rv_search_listener listener){
        this.context = context;
        this.course_list = course_list;
        this.listener = listener;
        this.course_list_filtered = course_list;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    course_list_filtered = course_list;
                }
                else {
                    List<Course> filtered_list = new ArrayList<>();
                    int size = course_list.size();
                    for (int x=0; x<size; x++){
                        Course row = course_list.get(x);
                        Log.e(Constants.TAG, "LIST SIZE: "+course_list.size()+" "+course_list.get(x).getCourseName());
                        if(row.getCourseName().toLowerCase().contains(charString.toLowerCase())){
                            filtered_list.add(row);
                        }
                        else
                            continue;
                    }/*
                    for (Course row : course_list){
                        Log.e(Constants.TAG, "LIST SIZE: "+course_list.size());
                        if(row.getCourseName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCourseDescription().toLowerCase().contains(charString.toLowerCase())){
                            filtered_list.add(row);
                        }
                    }*/
                    course_list_filtered = filtered_list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = course_list_filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                course_list_filtered = (ArrayList<Course>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_c_name, tv_c_description;
        public ViewHolder(View v) {
            super(v);
            tv_c_name = v.findViewById(R.id.tv_c_name);
            tv_c_description = v.findViewById(R.id.tv_c_description);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCourseSelected(course_list_filtered.get(getAdapterPosition()));
                }
            });
        }
    }
    @NonNull
    @Override
    public adapter_rv_search.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_search_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rv_search.ViewHolder viewHolder, int i) {
        viewHolder.tv_c_name.setText(course_list_filtered.get(i).getCourseName());
        viewHolder.tv_c_description.setText(course_list_filtered.get(i).getCourseDescription());
    }

    @Override
    public int getItemCount() {
        if(course_list_filtered!=null)
            return course_list_filtered.size();
        else return 0;
    }

    public interface adapter_rv_search_listener{
        void onCourseSelected(Course course);
    }
}
