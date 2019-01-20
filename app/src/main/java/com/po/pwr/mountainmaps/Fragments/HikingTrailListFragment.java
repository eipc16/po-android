package com.po.pwr.mountainmaps.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.HikingTrailListAdapter;
import com.po.pwr.mountainmaps.Utils.HikingTrailTask;

import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HikingTrailListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HikingTrailListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HikingTrailListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final static Integer id = 0;
    public String title;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HikingTrailListFragment() {
    }

    public static HikingTrailListFragment newInstance(String param1, String param2) {
        HikingTrailListFragment fragment = new HikingTrailListFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hikingtrail_list, container, false);
        title = getResources().getString(R.string.trips_drawer);

        HikingTrailTask task = (HikingTrailTask) new HikingTrailTask(getContext(), view).execute(request_address + "/hikers/" + hiker_id + "/hiking_trails");

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
