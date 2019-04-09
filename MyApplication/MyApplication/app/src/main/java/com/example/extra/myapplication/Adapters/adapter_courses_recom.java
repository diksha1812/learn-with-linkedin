package com.example.extra.myapplication.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;

import java.util.List;

/**
 * Created by Gurleen on 20-01-2019.
 */
public class adapter_courses_recom extends RecyclerView.Adapter<adapter_courses_recom.ViewHolder> {
    private List<Course> course_list;
    private adapter_rv_recom_listener listener;

    public adapter_courses_recom (List<Course> course_list, adapter_rv_recom_listener listener){
        this.course_list = course_list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_c_name;
        public ViewHolder(View v) {
            super(v);
            tv_c_name = v.findViewById(R.id.tv_c_name);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCourseSelected(course_list.get(getAdapterPosition()));
                }
            });
        }
    }
    @NonNull
    @Override
    public adapter_courses_recom.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_recom, viewGroup, false);
        adapter_courses_recom.ViewHolder vh = new adapter_courses_recom.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_courses_recom.ViewHolder viewHolder, int i) {
        viewHolder.tv_c_name.setText(course_list.get(i).getCourseName());
    }

    @Override
    public int getItemCount() {
        if(course_list!=null)
            return course_list.size();
        else return 0;
    }

    public interface adapter_rv_recom_listener{
        void onCourseSelected(Course course);
    }
}
