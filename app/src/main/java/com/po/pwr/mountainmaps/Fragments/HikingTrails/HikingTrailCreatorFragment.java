package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.Models.PointViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.HikingTrailListAdapter;
import com.po.pwr.mountainmaps.Utils.Adapters.PointListAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

public class HikingTrailCreatorFragment extends Fragment {


    public final static Integer id = 3;
    public String title;

    private HikingTrailViewModel hikingTrail;
    private final List<PointViewModel> currentTrailPoints = new ArrayList<>();

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

        title = getArguments().getString(EXTRA_TITLE);
        hikingTrail = (HikingTrailViewModel) getArguments().getSerializable("trail");

        updateData(view);
        updatePointList(view, currentTrailPoints);

        setUpReverseButton(view);
        setUpInfoButton(view);
        setUpAddButton(view);
        setUpSaveButton(view);

        return view;
    }

    public void setUpReverseButton(final View view) {
        Button reverseButton = view.findViewById(R.id.reverseButton);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTrailPoints.size() > 1) {
                    Collections.reverse(currentTrailPoints);
                    updatePointList(getView(), currentTrailPoints);
                }
            }
        });
    }

    public void setUpInfoButton(final View view) {
        Button infoButton = view.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTrailPoints.size() > 1) {
                    String infoRequest = request_address + "/points/details?points=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        infoRequest += currentTrailPoints.get(i).getId();

                        if (i < currentTrailPoints.size() - 1)
                            infoRequest += ",";
                    }

                    Log.d("info_request", infoRequest);

                    new SpringRequestTask<JsonNode>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<JsonNode>() {
                        @Override
                        public ResponseEntity<JsonNode> request(RestTemplate restTemplate, String url, HttpMethod method) {
                            return restTemplate.exchange(url, method, null, JsonNode.class);
                        }

                        @Override
                        public void onTaskExecuted(ResponseEntity<JsonNode> result) {
                            JsonNode response = result.getBody();
                            String info = prepareDialogData(response);
                            showInfoDialog(info);
                        }
                    }).execute(infoRequest);
                } else {
                    Toast.makeText(getContext(), "Podana trasa nie jest prawidłowa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpAddButton(final View view) {
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSearchDialog();
            }
        });
    }

    public void setUpSaveButton(final View view) {
        //createSearchDialog();
    }

    public void createSearchDialog() {
        final PointViewModel result = new PointViewModel();

        new SimpleSearchDialogCompat<>(getContext(), getResources().getString(R.string.search_dialog_title),
                "Podaj nazwę odcinka", null, initSearchData(), new SearchResultListener<Searchable>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                for(Map.Entry<Integer, PointViewModel> entry : MainActivity.pointSet.entrySet()) {
                    if(entry.getValue().getName().equals(searchable.getTitle())) {
                        Log.d("model " + entry.getValue().getId(), entry.getValue().toString());
                        result.setId(entry.getValue().getId());
                        result.setName(entry.getValue().getName());

                    }
                }

                if(result.getName() != null) {
                    currentTrailPoints.add(result);
                    updatePointList(getView(), currentTrailPoints);
                } else {
                    Toast.makeText(getContext(), "Podany odcinek nie istnieje!", Toast.LENGTH_SHORT).show();
                }
                baseSearchDialogCompat.dismiss();
            }
        }).show();
    }

    public ArrayList<Searchable> initSearchData() {
        ArrayList<Searchable> searchList = new ArrayList<>();

        for(Map.Entry<Integer, PointViewModel> entry : MainActivity.pointSet.entrySet()) {
            if(currentTrailPoints.isEmpty() || !entry.getValue().sameName(currentTrailPoints.get(currentTrailPoints.size() - 1))) {
                searchList.add(entry.getValue());
            }
        }

        return searchList;
    }

    public void updateData(final View view) {
        MainActivity activity = ((MainActivity) getActivity());

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
    }
    public void updatePointList(View view, List<PointViewModel> newPointList) {
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.hikingTrailPointsList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        
        PointListAdapter mRecyclerAdapter = new PointListAdapter(newPointList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    public String prepareDialogData(JsonNode response) {
        EditText editName = getView().findViewById(R.id.hikingTrailName);

        Double distance = response.get("dist").asDouble(0) / 1000;
        Integer points = response.get("points").asInt(0);
        Double time = response.get("time").asDouble(0);

        Integer hours = (int) Math.floor(time);
        Integer minutes = (int) (60 * (time - Math.floor(time)));

        String result = "";
        result += getResources().getString(R.string.details_title, editName.getText().toString()) + "\n";
        result += getResources().getString(R.string.details_dist, distance) + "\n";
        result += getResources().getString(R.string.details_points, points) + "\n";
        result += getResources().getString(R.string.details_time, hours,  minutes);

        return result;
    }

    public void showInfoDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}