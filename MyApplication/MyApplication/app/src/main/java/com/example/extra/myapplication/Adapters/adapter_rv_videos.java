package com.example.extra.myapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.extra.myapplication.DataClasses.LessonInfo;
import com.example.extra.myapplication.R;

import java.util.List;

public class adapter_rv_videos extends RecyclerView.Adapter<adapter_rv_videos.MyViewHolder> {
    private Context context;
    private List<LessonInfo> lessonInfoList;
    private adapter_rv_videos_listener listener;

    public adapter_rv_videos(Context context, List<LessonInfo> lessonInfoList, adapter_rv_videos_listener listener){
        this.context = context;
        this.lessonInfoList = lessonInfoList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView l_no, l_name, l_desc;

        public MyViewHolder(View view) {
            super(view);
            l_no = view.findViewById(R.id.tv_l_no);
            l_name = view.findViewById(R.id.tv_l_name);
            l_desc = view.findViewById(R.id.tv_l_desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onVideoClickedListener(lessonInfoList.get(getAdapterPosition()));
                }
            });
        }
    }
    @NonNull
    @Override
    public adapter_rv_videos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_video_list_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_rv_videos.MyViewHolder holder, int i) {
        final LessonInfo lessonInfo = lessonInfoList.get(i);
        holder.l_no.setText(String.valueOf(lessonInfo.getLesson_no()));
        holder.l_name.setText(lessonInfo.getLesson_name());
        holder.l_desc.setText(lessonInfo.getLesson_desc());
    }

    @Override
    public int getItemCount() {
        return lessonInfoList.size();
    }

    public interface adapter_rv_videos_listener{
        void onVideoClickedListener(LessonInfo lessonInfo);
    }
}

