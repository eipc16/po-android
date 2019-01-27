package com.po.pwr.mountainmaps.Fragments.Settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.po.pwr.mountainmaps.Fragments.Badge.BadgeDetailFragment;
import com.po.pwr.mountainmaps.R;

import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;


/**
 * Testowy fragment umozliwajacy zmiane obecnego turysty
 */
public class HikerSelectionFragment extends Fragment {
    public final static Integer id = 3;
    public String title;

    public static BadgeDetailFragment newInstance() {
        return new BadgeDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_hiker_selection, container, false);

        final Button hikerButton = view.findViewById(R.id.hikerButton);
        final EditText hikerId = view.findViewById(R.id.hikerId);

        final Button ipButton = view.findViewById(R.id.ipButton);
        final EditText ipAdress = view.findViewById(R.id.ipAdress);

        hikerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiker_id = Integer.valueOf(hikerId.getText().toString());
            }
        });

        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_address = ipAdress.getText().toString();
            }
        });

        hikerId.setText(hiker_id.toString());
        ipAdress.setText(request_address);

        title = getResources().getString(R.string.settings_drawer);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
