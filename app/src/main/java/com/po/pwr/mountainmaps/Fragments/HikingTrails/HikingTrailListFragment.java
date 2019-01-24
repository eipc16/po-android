package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrail;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.HikingTrailListAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;


public class HikingTrailListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final static Integer id = 0;
    public String title;


    private OnFragmentInteractionListener mListener;

    public HikingTrailListFragment() {
    }

    public static HikingTrailListFragment newInstance(String title) {
        HikingTrailListFragment fragment = new HikingTrailListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_hikingtrail_list, container, false);
        title = getResources().getString(R.string.trips_drawer);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        new RequestTask(new RequestTask.OnTaskExecutedListener() {
            @Override
            public void onTaskExecuted(String result) {
                RecyclerView mRecyclerView;
                RecyclerView.Adapter mAdapter;
                RecyclerView.LayoutManager mLayoutManager;

                ArrayList<HikingTrail> hikingTrails = new ArrayList<>();
                JSONArray json = null;
                try {
                    json = new JSONArray(result);
                    for(int i = 0; i < json.length(); i++) {
                        JSONObject e = json.getJSONObject(i);
                        hikingTrails.add(new HikingTrail(
                                Integer.parseInt(e.getString("id")),
                                e.getString("name"),
                                e.getString("date").substring(0, 10)
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("arraylist_log", hikingTrails.toString());
                mRecyclerView = view.findViewById(R.id.listContainer);
                mRecyclerView.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new HikingTrailListAdapter(hikingTrails, new HikingTrailListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v) {
//                        String name = ((TextView) v.findViewById(R.id.hikingtrailName)).getText().toString();
//                        String date = ((TextView) v.findViewById(R.id.hikingTrailDate)).getText().toString();
//
//                        Fragment fragment = HikingTrailCreatorFragment.newInstance(getResources().getString(R.string.update_hikingtrail), name, date);
//
//
//                        FragmentTransaction transaction = null;
//                        if (getFragmentManager() != null) {
//                            transaction = getFragmentManager().beginTransaction();
//                            transaction.replace(R.id.fragmentContainer, fragment);
//                            transaction.addToBackStack(null);
//                            transaction.commit();
//                        }

                    }
                });
                mRecyclerView.setAdapter(mAdapter);
            }
        }).execute(request_address + "/hikers/" + hiker_id + "/hiking_trails");


        FloatingActionButton floatingButton = view.findViewById(R.id.newHikingTrailButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = HikingTrailCreatorFragment.newInstance(getResources().getString(R.string.new_hikingtrail));

                FragmentTransaction transaction = null;
                if (getFragmentManager() != null) {
                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

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
        void onFragmentInteraction(Uri uri);
    }
}
