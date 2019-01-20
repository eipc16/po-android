package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrail;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Tasks.RequestTask;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

public class HikingTrailCreatorFragment extends Fragment {

    private HikingTrailCreatorViewModel mViewModel;

    public final static Integer id = 3;
    public String title;

    public HikingTrailCreatorFragment() {
        //title = (getResources().getString(R.string.new_hikingtrail));
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

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {

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

}
