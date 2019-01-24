package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrail;
import com.po.pwr.mountainmaps.Models.Point;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.PointListAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

public class HikingTrailCreatorFragment extends Fragment implements View.OnClickListener {

    private HikingTrailCreatorViewModel mViewModel;

    public final static Integer id = 3;
    public String title;

    //title = (getResources().getString(R.string.new_hikingtrail));
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Point> trailPoints = new ArrayList<>();

    private String oldName = "";
    private String oldDate = "";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    public HikingTrailCreatorFragment() {
    }

    public static HikingTrailCreatorFragment newInstance(String title) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EXTRA_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static HikingTrailCreatorFragment newInstance(String title, String name, String date) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(3);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString("trailName", name);
        bundle.putString("trailDate", date);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.hiking_trail_creator_fragment, container, false);

        MainActivity activity = ((MainActivity) getActivity());
        title = getArguments().getString(EXTRA_TITLE);

        String trailName = getArguments().getString("trailName");
        String trailDate = getArguments().getString("trailDate");

        if(trailName != null) {
            TextView trailNameView = view.findViewById(R.id.hikingTrailName);
            oldName = trailName;
            trailNameView.setText(trailName);
        }

        if(trailDate != null) {
            TextView trailNameView = view.findViewById(R.id.hikingTrailDate);
            oldDate = trailDate;
            trailNameView.setText(trailDate);
        }

        final Button addButton = view.findViewById(R.id.addButton);
        final Button saveButton = view.findViewById(R.id.saveButton);
        final Button infoButton = view.findViewById(R.id.infoButton);
        addButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);

        if (activity != null) {
            activity.curr_fragment = id;
            activity.getSupportActionBar().setTitle(title);
        }

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {


                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        points.add(new Point(jsonObject.getInt("id"), jsonObject.getString("name")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("points", points.toString());


                recyclerView = view.findViewById(R.id.hikingTrailPointsList);
                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                adapter = new PointListAdapter(trailPoints, new PointListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View lv) {
                    }
                });
                recyclerView.setAdapter(adapter);


            }
        }).execute(request_address + "/points/all");


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HikingTrailCreatorViewModel.class);
        // TODO: Use the ViewModel
    }

    public void trailCreate(View v) {
        int id = v.getId();
        final TextView nameText = getView().findViewById(R.id.hikingTrailName);
        final TextView dateText = getView().findViewById(R.id.hikingTrailDate);


        String name = nameText.getText().toString();
        Log.d("name", name);
        String date = dateText.getText().toString();

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {

            }
        }).execute(request_address + "/hikers/" + hiker_id + "/add/hiking_trails?name=" + name);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < trailPoints.size(); i++) {
            Point p = trailPoints.get(i);
            stringBuilder.append(p.getId());
            if (i < trailPoints.size() - 1)
                stringBuilder.append(',');
        }

        String data = stringBuilder.toString();

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }).execute(request_address + "/hiking_trails/update/" + name + "/points?data=" + data);
    }

    public void trailModify(View v) {
        final TextView nameText = getView().findViewById(R.id.hikingTrailName);
        final TextView dateText = getView().findViewById(R.id.hikingTrailDate);


        final String name = nameText.getText().toString();
        Log.d("name", name);
        String date = dateText.getText().toString();

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {
                //zmieniono nazwe
                Log.d("result", result);
                if(result.equals("{}") || name.equals(oldName)){

                    //nowa nazwa trasy
                    if(!name.equals(oldName)) {
//                        //Usun trase o poprzedniej nazwie
//                        new RequestTask(new RequestTask.OnTaskExecutedListener() {
//                            @Override
//                            public void onTaskExecuted(String result) {
//                            }
//                        }).execute(request_address + "/hikers/" + hiker_id + "/delete/hiking_trails?name=" + oldName);
                        oldName = name;
                        //Dodaj trase o nowej nazwie
                        new RequestTask(new RequestTask.OnTaskExecutedListener() {
                            @Override
                            public void onTaskExecuted(String result) {
                            }
                        }).execute(request_address + "/hikers/" + hiker_id + "/add/hiking_trails?name=" + name);
                    }

                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < trailPoints.size(); i++) {
                        Point p = trailPoints.get(i);
                        stringBuilder.append(p.getId());
                        if (i < trailPoints.size() - 1)
                            stringBuilder.append(',');
                    }

                    String data = stringBuilder.toString();

                    new RequestTask(new RequestTask.OnTaskExecutedListener() {
                        @Override
                        public void onTaskExecuted(String result) {
                            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }).execute(request_address + "/hiking_trails/update/" + name + "/points?data=" + data);

                } else {
                    //trasa o nowej nazwie juz istnieje
                    Toast.makeText(getContext(), "Trasa o podanej nazwie juz istnieje!", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(request_address + "/hiking_trails/details/" + name);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.infoButton) {
            final TextView nameText = getView().findViewById(R.id.hikingTrailName);
            final String name = nameText.getText().toString();
            Log.d("infoBtn", name);
            new RequestTask(new RequestTask.OnTaskExecutedListener() {
                @Override
                public void onTaskExecuted(String result) {
                    try {
                        JSONObject json = new JSONObject(result);

                        Double distance = json.getDouble("dist") / 1000;
                        Integer points = json.getInt("points");
                        Double time = json.getDouble("time");

                        Integer hours = (int) Math.floor(time);
                        Integer minutes = (int) (60 * (time - Math.floor(time)));
                        //Toast.makeText(getContext(), distance + " " + points + " " + time, Toast.LENGTH_SHORT).show();

                        final Dialog dialog = new Dialog(getContext()); // Context, this, etc.
                        dialog.setContentView(R.layout.details_dialog);

                        TextView distView = dialog.findViewById(R.id.dialog_dist);
                        distView.setText(getResources().getString(R.string.details_dist, distance));

                        TextView pointsView = dialog.findViewById(R.id.dialog_points);
                        pointsView.setText(getResources().getString(R.string.details_points, points));

                        TextView timeView = dialog.findViewById(R.id.dialog_time);
                        timeView.setText(getResources().getString(R.string.details_time, hours, minutes));

                        dialog.setTitle("Informacje o trasie " + name);
                        dialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute(request_address + "/hiking_trails/details/" + name);
        } else if (id == R.id.addButton) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            for (Point p : points)
                popupMenu.getMenu().add(p.getName());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Point point = null;
                    for (Point p : points) {
                        if (p.getName().contentEquals(item.getTitle())) {
                            point = p;
                            break;
                        }
                    }
                    trailPoints.add(point);

                    adapter.notifyDataSetChanged();

                    return true;
                }
            });
        } else if (id == R.id.saveButton) {
            if(!oldName.equals("") && !oldDate.equals("")) {
                trailModify(v);
            } else {
                trailModify(v);
            }
        }
    }
}
