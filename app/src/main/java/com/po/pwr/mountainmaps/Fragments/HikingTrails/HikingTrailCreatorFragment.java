package com.po.pwr.mountainmaps.Fragments.HikingTrails;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
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

import java.util.ArrayList;
import java.util.LinkedList;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

public class HikingTrailCreatorFragment extends Fragment implements View.OnClickListener {

    private HikingTrailCreatorViewModel mViewModel;

    public final static Integer id = 3;
    public String title;

    //title = (getResources().getString(R.string.new_hikingtrail));
    ArrayList <Point> points = new ArrayList<>();

    public HikingTrailCreatorFragment() {
    }

    public static HikingTrailCreatorFragment newInstance(String title) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EXTRA_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.hiking_trail_creator_fragment, container, false);

        MainActivity activity = ((MainActivity) getActivity());
        title = getArguments().getString(EXTRA_TITLE);

        final Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        if(activity != null) {
            activity.curr_fragment = id;
            activity.getSupportActionBar().setTitle(title);
        }

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {


                RecyclerView recyclerView;
                RecyclerView.Adapter adapter;
                RecyclerView.LayoutManager layoutManager;

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

                /*
                recyclerView = view.findViewById(R.id.hikingTrailPointsList);
                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                adapter = new PointListAdapter(points, new PointListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v) {

                    }
                });
                recyclerView.setAdapter(adapter);
*/


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

    public void addPoint(View v) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.addButton) {

            PopupMenu popupMenu = new PopupMenu(getActivity(), v);

            for (Point p: points)
                popupMenu.getMenu().add(p.getName());
            popupMenu.show();
        }


    }
}
