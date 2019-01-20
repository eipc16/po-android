package com.po.pwr.mountainmaps.Utils.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.HikingTrail;
import com.po.pwr.mountainmaps.Models.Point;
import com.po.pwr.mountainmaps.R;

import java.util.List;

public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(View v);
    }

    private List<Point> points;
    private final OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;

        public MyViewHolder(@NonNull RelativeLayout layout) {
            super(layout);
            this.layout = layout;
        }

        public void bind(final RelativeLayout item, final OnItemClickListener listener) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v);
                }
            });
        }
    }

    public PointListAdapter(List<Point> points, OnItemClickListener listener) {
        this.points = points;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RelativeLayout l = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.point, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(l);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        TextView name = myViewHolder.layout.findViewById(R.id.pointName);
        name.setText(points.get(i).getName());
        myViewHolder.bind(myViewHolder.layout, listener);
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

}
