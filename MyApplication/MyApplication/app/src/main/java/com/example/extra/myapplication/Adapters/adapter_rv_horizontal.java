package com.example.extra.myapplication.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.extra.myapplication.DataClasses.Course;
import com.example.extra.myapplication.R;

import java.util.List;

public class adapter_rv_horizontal extends RecyclerView.Adapter<adapter_rv_horizontal.ViewHolder> {
    private List<Course> courses;
    private adapter_rv_h_listener listener;
    private String topic;
    int topic_id;

    public adapter_rv_horizontal(String topic, List<Course> courses, adapter_rv_h_listener listener, int topic_id){
        this.topic = topic;
        this.courses = courses;
        this.listener = listener;
        this.topic_id = topic_id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_topic, ll_courses;
        ImageView iv_course;
        TextView tv_topics, tv_couses;
        public ViewHolder(View v) {
            super(v);
            ll_topic = v.findViewById(R.id.ll_topic_names);
            ll_courses = v.findViewById(R.id.ll_course_names);
            iv_course = v.findViewById(R.id.iv_course_image);
            tv_couses = v.findViewById(R.id.tv_course_name);
            tv_topics = v.findViewById(R.id.tv_topic_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()==0){
                        listener.onCourseSelectedRVH(null, 0, topic_id);
                    }
                    else{
                        listener.onCourseSelectedRVH(courses.get(getAdapterPosition()-1), getAdapterPosition(), topic_id);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public adapter_rv_horizontal.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.course_card, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rv_horizontal.ViewHolder viewHolder, int position) {
        if(position==0){
            viewHolder.ll_courses.setVisibility(View.GONE);
            viewHolder.ll_topic.setVisibility(View.VISIBLE);
            viewHolder.tv_topics.setText(topic);
        }
        else{
            viewHolder.ll_courses.setVisibility(View.VISIBLE);
            viewHolder.ll_topic.setVisibility(View.GONE);
            viewHolder.tv_couses.setText(courses.get(position-1).getCourseName());
        }
    }

    @Override
    public int getItemCount() {
        return courses.size()+1;
    }

    public interface adapter_rv_h_listener{
        void onCourseSelectedRVH(Course course, int flag, int topicId);
        //flag=0: TopicActivity
        //flag>0: CourseActivity
    }
}
