package com.po.pwr.mountainmaps.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.po.pwr.mountainmaps.Models.Badge;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.DisplayBadgePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class BadgeDisplayFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BadgeDisplayFragment() {
    }

    public static BadgeDisplayFragment newInstance(String param1, String param2) {
        BadgeDisplayFragment fragment = new BadgeDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badge_display, container, false);

        ArrayList<Badge> badgeList = new ArrayList<>();

        JSONObject json = null;
        try {
            json = new JSONObject(loadJSONFromAsset(view.getContext(), "badges.json"));
            JSONArray json_array = json.getJSONArray("badges");
            for(int i = 0; i < json_array.length(); i++) {
                JSONObject json_element = json_array.getJSONObject(i);
                JSONArray json_dates = json_element.getJSONArray("date");
                JSONArray json_points = json_element.getJSONArray("points");

                String displayName = "";
                String[] badgeLevels = {"Brązowa", "Srebrna", "Złota"};

                for(int j = 0; j < json_dates.length(); j++) {
                    if(!json_dates.getString(j).equals("")) {

                        displayName = json_element.getString("display_name");

                        if(json_dates.length() > 1) {
                            displayName = displayName + " "  + badgeLevels[j];
                        }

                        badgeList.add(new Badge(json_element.getInt("id") + j
                                , displayName
                                , "badge_"
                                , json_points.getInt(j)
                                , json_dates.getString(j)));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Badge b: badgeList) {
            Log.d("Badges", b.toString());
        }

        ViewPager viewPager = view.findViewById(R.id.badge_viewpager);
        viewPager.setAdapter(new DisplayBadgePagerAdapter(this.getContext(), badgeList));

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    /*

    Button button = view.findViewById(R.id.buttonJSON);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject json = new JSONObject(loadJSONFromAsset(v.getContext(), "badges.json"));
                    JSONArray json_array = json.getJSONArray("badges");
                    for(int i = 0; i < json_array.length(); i++) {
                        JSONObject json_element = json_array.getJSONObject(i);
                        Log.d("json_log", "ID: " + json_element.getInt("id") + " | Display: " + json_element.getString("display_name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
     */
}
