package com.po.pwr.mountainmaps.Utils.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        public ConstraintLayout layout;

        public MyViewHolder(@NonNull ConstraintLayout layout) {
            super(layout);
            this.layout = layout;
        }

        public void bind(final ConstraintLayout item, final OnItemClickListener listener) {
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
        ConstraintLayout l = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.point, viewGroup, false);
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
