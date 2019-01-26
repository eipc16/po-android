package com.po.pwr.mountainmaps.Utils.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.PointViewModel;
import com.po.pwr.mountainmaps.R;

import java.util.List;

public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.MyViewHolder> implements View.OnClickListener {

    private final List<PointViewModel> points;
    private MyViewHolder viewHolder = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pointNameView;
        public ImageView pointDeleteButton;

        public MyViewHolder(View view) {
            super(view);
            this.pointNameView = view.findViewById(R.id.pointName);
            this.pointDeleteButton = view.findViewById(R.id.pointDeleteButton);
        }
    }

    public PointListAdapter(List<PointViewModel> points) {
        this.points = points;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout l = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.point, viewGroup, false);
        View nV = l.getViewById(R.id.pointView);
        viewHolder = new MyViewHolder(nV);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        TextView name = myViewHolder.pointNameView;
        name.setText(points.get(i).getName());

        ImageView button = myViewHolder.pointDeleteButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeElement(myViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    public boolean removeElement(int position) {
        boolean removed = false;
        if(position > -1 && position < points.size()) {
            points.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position, points.size());
            removed = true;
        }

        return removed;
    }

    @Override
    public void onClick(View v) {
        removeElement(viewHolder.getAdapterPosition());
    }

}
