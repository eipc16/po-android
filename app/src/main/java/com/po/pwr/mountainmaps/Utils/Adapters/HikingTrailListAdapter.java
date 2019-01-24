package com.po.pwr.mountainmaps.Utils.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.R;

import java.util.List;

public class HikingTrailListAdapter extends RecyclerView.Adapter<HikingTrailListAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, Integer trailId);
    }

    private List<HikingTrailViewModel> hikingTrails;
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
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public HikingTrailListAdapter(List<HikingTrailViewModel> hikingTrails, OnItemClickListener listener) {
        this.hikingTrails = hikingTrails;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RelativeLayout l = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hiking_trail, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        TextView name = myViewHolder.layout.findViewById(R.id.hikingtrailName);
        name.setText(hikingTrails.get(i).name);
        TextView date = myViewHolder.layout.findViewById(R.id.hikingTrailDate);
        date.setText(hikingTrails.get(i).date.toString());

        myViewHolder.bind(myViewHolder.layout, listener);
    }

    @Override
    public int getItemCount() {
        return hikingTrails.size();
    }
}
