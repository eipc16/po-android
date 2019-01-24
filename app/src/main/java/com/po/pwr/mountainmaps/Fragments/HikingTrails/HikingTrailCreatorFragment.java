package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.R;

import static android.content.Intent.EXTRA_TITLE;

public class HikingTrailCreatorFragment extends Fragment {


    public final static Integer id = 3;
    public String title;

    private HikingTrailViewModel hikingTrail;

    public HikingTrailCreatorFragment() {
    }

    public static HikingTrailCreatorFragment newInstance(String title, HikingTrailViewModel hikingTrail) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putSerializable("trail", hikingTrail);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.hiking_trail_creator_fragment, container, false);

        MainActivity activity = ((MainActivity) getActivity());

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
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}