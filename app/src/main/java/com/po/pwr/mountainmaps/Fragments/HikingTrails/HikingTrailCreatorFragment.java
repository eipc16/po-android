package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.Models.PointViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.HikingTrailListAdapter;
import com.po.pwr.mountainmaps.Utils.Adapters.PointListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static android.content.Intent.EXTRA_TITLE;

public class HikingTrailCreatorFragment extends Fragment {


    public final static Integer id = 3;
    public String title;

    private HikingTrailViewModel hikingTrail;
    private List<PointViewModel> currentTrailPoints;

    public HikingTrailCreatorFragment() {
    }

    public static HikingTrailCreatorFragment newInstance(String title, HikingTrailViewModel hikingTrail) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(2);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putSerializable("trail", hikingTrail);
        fragment.setArguments(bundle);
        return fragment;
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

        currentTrailPoints = new ArrayList<>();

        title = getArguments().getString(EXTRA_TITLE);
        hikingTrail = (HikingTrailViewModel) getArguments().getSerializable("trail");

        if(activity != null) {
            activity.curr_fragment = id;
            activity.getSupportActionBar().setTitle(title);
        }

        if(hikingTrail != null) {
            EditText editName = view.findViewById(R.id.hikingTrailName);
            EditText editDate = view.findViewById(R.id.hikingTrailDate);

            editName.setText(hikingTrail.getName());
            editDate.setText(hikingTrail.getDate().toString());

            HashMap<Integer, PointViewModel> allPoints = ((MainActivity) getActivity()).pointSet;

            for (Integer i : hikingTrail.getPoints())
                currentTrailPoints.add(allPoints.get(i));
        }

        setUpPointList(view);

        return view;
    }

    public void setUpPointList(View view) {
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.hikingTrailPointsList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new PointListAdapter(currentTrailPoints, new PointListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Integer position) {
                Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}