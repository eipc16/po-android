package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.R;

import static android.content.Intent.EXTRA_TITLE;

public class HikingTrailCreatorFragment extends Fragment {


    public final static Integer id = 3;
    public String title;

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

        if(activity != null) {
            activity.curr_fragment = id;
            activity.getSupportActionBar().setTitle(title);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}