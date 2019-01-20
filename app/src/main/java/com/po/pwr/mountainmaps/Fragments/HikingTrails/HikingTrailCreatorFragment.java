package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.po.pwr.mountainmaps.R;

public class HikingTrailCreatorFragment extends Fragment {

    private HikingTrailCreatorViewModel mViewModel;

    public static HikingTrailCreatorFragment newInstance() {
        return new HikingTrailCreatorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hiking_trail_creator_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HikingTrailCreatorViewModel.class);
        // TODO: Use the ViewModel
    }

}
