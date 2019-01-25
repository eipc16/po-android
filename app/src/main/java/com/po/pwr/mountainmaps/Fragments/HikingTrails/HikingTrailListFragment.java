package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.BadgeViewModel;
import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.DisplayBadgePagerAdapter;
import com.po.pwr.mountainmaps.Utils.Adapters.HikingTrailListAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.RequestTask;
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;


public class HikingTrailListFragment extends Fragment {
    public final static Integer id = 0;
    public String title;

    private final Set<HikingTrailViewModel> hikingTrails = new HashSet<>();

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

    public void loadUserHikingTrails(final View view) {
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<List<HikingTrailViewModel>>() {
            @Override
            public ResponseEntity<List<HikingTrailViewModel>> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, new ParameterizedTypeReference<List<HikingTrailViewModel>>() {
                });
            }

            @Override
            public void onTaskExecuted(ResponseEntity<List<HikingTrailViewModel>> result) {
                hikingTrails.addAll(result.getBody());
                updateAdapter(view);
            }

        }).execute(request_address + "/hikers/" + hiker_id + "/hiking_trails");
    }

    private void updateAdapter(View view) {
        final List<HikingTrailViewModel> hikingTrailList = new ArrayList<>();
        hikingTrailList.addAll(hikingTrails);

        final RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.listContainer);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new HikingTrailListAdapter(hikingTrailList, getContext(), getFragmentManager()));
    }


    @Override
    public void onResume() {
        super.onResume();
        hikingTrails.clear();
        loadUserHikingTrails(getView());
        updateAdapter(getView());
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
