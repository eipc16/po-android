package com.po.pwr.mountainmaps.Utils;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.HikingTrail;
import com.po.pwr.mountainmaps.R;

import java.util.List;

public class HikingTrailListAdapter extends RecyclerView.Adapter<HikingTrailListAdapter.MyViewHolder> {

    private List<HikingTrail> hikingTrails;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layout;

        public MyViewHolder(@NonNull RelativeLayout layout) {
            super(layout);
            this.layout = layout;
        }
    }

    public HikingTrailListAdapter(List<HikingTrail> hikingTrails) {
        this.hikingTrails = hikingTrails;
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
        date.setText(hikingTrails.get(i).date);
    }

    @Override
    public int getItemCount() {
        return hikingTrails.size();
    }
}
