package com.po.pwr.mountainmaps.Utils.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

public class HikingTrailListAdapter extends RecyclerView.Adapter<HikingTrailListAdapter.MyViewHolder> implements OnTrailClickListener {

    final List<HikingTrailViewModel> hikingTrails;
    final Context context;
    private final FragmentManager fragmentManager;

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
        return new MyViewHolder(l);
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

        if(hikingTrail.getPoints().size() > 1) {
            startPointName = MainActivity.pointSet.get(hikingTrail.getPoints().get(0)).getName();
            endPointName = MainActivity.pointSet.get(hikingTrail.getPoints().get(hikingTrails.get(i).getPoints().size() - 1)).getName();
        }

        if(!hikingTrail.isFinished()) {
            statusImage.setVisibility(View.INVISIBLE);
        }

        point.setText(context.getResources().getString(R.string.track_list_begin_end, startPointName, endPointName));

        myViewHolder.bind(myViewHolder.layout, this);
    }

    void removeElement(int position) {
        if(position > -1) {
            hikingTrails.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position, hikingTrails.size());
        }

        Log.d("New position: ", Integer.valueOf(position).toString());
        Log.d("New array: ", hikingTrails.toString());
        Log.d("New array size: ", Integer.valueOf(hikingTrails.size()).toString());
    }

    private AlertDialog createDialog(final Integer position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.track_list_dialog_remove)
                .setPositiveButton(R.string.track_list_dialog_agree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("POSITION", position.toString());

                        new SpringRequestTask<>(HttpMethod.POST, new SpringRequestTask.OnSpringTaskListener<String>() {
                            @Override
                            public ResponseEntity<String> request(RestTemplate restTemplate, String url, HttpMethod method) {
                                return restTemplate.exchange(url, method, null, String.class);
                            }

                            @Override
                            public void onTaskExecuted(ResponseEntity<String> result) {
                                String response = result.getBody();
                                Log.d("RESPONSE", response);
                                if("deleted".equals(response)) {
                                    Toast.makeText(context, "Trasa została usunięta!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Błąd przy usuwaniu trasy!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }).execute(request_address + "/hikers/" + hiker_id + "/hiking_trails/delete?trail_id=" + hikingTrails.get(position).getId());
                        removeElement(position);
                    }
                })
                .setNegativeButton(R.string.track_list_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public int getItemCount() {
        return hikingTrails.size();
    }

    @Override
    public void onItemClick(View v, Integer position) {
        HikingTrailViewModel hikingTrail = hikingTrails.get(position);

        if(!hikingTrails.get(position).isFinished()) {
            Fragment fragment = HikingTrailCreatorFragment.newInstance(context.getResources().getString(R.string.update_hikingtrail), hikingTrail);

            FragmentTransaction transaction;
            if (fragmentManager != null) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment, "trailCreator");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    @Override
    public boolean onLongLick(View v, Integer position) {
        if(hikingTrails.get(position).isFinished()) {
            return false;
        }

        AlertDialog removeDialog = createDialog(position);
        removeDialog.show();

        return true;
    }
}
