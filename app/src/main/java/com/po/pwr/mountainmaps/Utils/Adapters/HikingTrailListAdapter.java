package com.po.pwr.mountainmaps.Utils.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Fragments.HikingTrails.HikingTrailCreatorFragment;
import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Listeners.OnTrailClickListener;

import java.util.List;

public class HikingTrailListAdapter extends RecyclerView.Adapter<HikingTrailListAdapter.MyViewHolder> implements OnTrailClickListener {

    private List<HikingTrailViewModel> hikingTrails;
    private Context context;
    private FragmentManager fragmentManager;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout layout;

        public MyViewHolder(@NonNull ConstraintLayout  layout) {
            super(layout);
            this.layout = layout;
        }

        public void bind(final ConstraintLayout item, final OnTrailClickListener listener) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });

            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return listener.onLongLick(v, getAdapterPosition());
                }
            });
        }
    }

    public HikingTrailListAdapter(List<HikingTrailViewModel> hikingTrails, Context context, FragmentManager fragmentManager) {
        this.hikingTrails = hikingTrails;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout  l = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hiking_trail, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        TextView name = myViewHolder.layout.findViewById(R.id.hikingtrailName);
        name.setText(hikingTrails.get(i).getName());

        TextView date = myViewHolder.layout.findViewById(R.id.hikingTrailDate);
        date.setText(hikingTrails.get(i).getDate().toString());

        ImageView statusImage = myViewHolder.layout.findViewById(R.id.hikingTrailStatus);

        TextView point = myViewHolder.layout.findViewById(R.id.hikingTrailPoints);

        HikingTrailViewModel hikingTrail = hikingTrails.get(i);

        String startPointName = context.getResources().getString(R.string.track_list_unkown);
        String endPointName = context.getResources().getString(R.string.track_list_unkown);

        if(hikingTrail.getPoints().size() > 1)
            startPointName = MainActivity.pointSet.get(hikingTrail.getPoints().get(0)).getName();
            endPointName = MainActivity.pointSet.get(hikingTrail.getPoints().get(hikingTrails.get(i).getPoints().size() - 1)).getName();

        if(!hikingTrail.isFinished())
            statusImage.setVisibility(View.INVISIBLE);

        point.setText(context.getResources().getString(R.string.track_list_begin_end, startPointName, endPointName));

        myViewHolder.bind(myViewHolder.layout, this);
    }

    private void removeElement(int position) {
        if(position > -1) {
            hikingTrails.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position, hikingTrails.size());
        }

        Log.d("New position: ", Integer.valueOf(position).toString());
        Log.d("New array: ", hikingTrails.toString());
        Log.d("New array size: ", Integer.valueOf(hikingTrails.size()).toString());
    }

    @Override
    public int getItemCount() {
        return hikingTrails.size();
    }

    @Override
    public void onItemClick(View v, Integer trailId) {
        HikingTrailViewModel hikingTrail = hikingTrails.get(trailId);

        Fragment fragment = HikingTrailCreatorFragment.newInstance(context.getResources().getString(R.string.update_hikingtrail), hikingTrail);

        FragmentTransaction transaction;
        if (fragmentManager != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onLongLick(View v, Integer trailId) {
        if(hikingTrails.get(trailId).isFinished())
            return false;

        Toast.makeText(context, "removing", Toast.LENGTH_SHORT).show();

        return true;
    }
}
