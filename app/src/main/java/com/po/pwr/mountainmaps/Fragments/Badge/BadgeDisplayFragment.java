package com.po.pwr.mountainmaps.Fragments.Badge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.po.pwr.mountainmaps.Models.BadgeViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.DisplayBadgePagerAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;


public class BadgeDisplayFragment extends Fragment {
    public final static Integer id = 2;
    public String title;

    public BadgeDisplayFragment() {
        //Create new BadgeDisplayFragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //onCreate
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_badge_display, container, false);

        loadNextBadgeData(view);
        loadUserBadges(view);

        title = getResources().getString(R.string.badges_drawer);

        return view;
    }

    public void loadNextBadgeData(final View view) {
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<JsonNode>() {

            @Override
            public ResponseEntity<JsonNode> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, JsonNode.class);
            }

            @Override
            public void onTaskExecuted(ResponseEntity<JsonNode> result) {
                JsonNode response = result.getBody();

                final TextView nextBadgeName = view.findViewById(R.id.badge_next);
                final TextView currentPoints = view.findViewById(R.id.badge_points);
                final TextView requiredPoints = view.findViewById(R.id.badge_req_points);
                final TextView missingPoints = view.findViewById(R.id.badge_diff);

                if(response.size() > 0) {
                    String name = response.get("next_badge").asText("None");
                    Integer currentPointsValue = response.get("current_points").asInt(0);
                    Integer requiredPointsValue = response.get("required_points").asInt(0);
                    Integer missingPointsValue = response.get("missing_points").asInt(0);

                    nextBadgeName.setText(view.getContext().getString(R.string.badges_name, name));
                    currentPoints.setText(view.getContext().getString(R.string.badges_points, currentPointsValue));
                    requiredPoints.setText(view.getContext().getString(R.string.badges_req_points, requiredPointsValue));
                    missingPoints.setText(view.getContext().getString(R.string.badges_diff, missingPointsValue));
                } else {
                    nextBadgeName.setText(view.getContext().getString(R.string.badges_completed_1));
                    currentPoints.setText(view.getContext().getString(R.string.badges_completed_2));
                    requiredPoints.setVisibility(View.INVISIBLE);
                    missingPoints.setVisibility(View.INVISIBLE);
                }
            }

        }).execute(request_address + "/hikers/" + hiker_id + "/next");
    }

    public void loadUserBadges(final View view) {
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<List<BadgeViewModel>>() {

            @Override
            public ResponseEntity<List<BadgeViewModel>> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, new ParameterizedTypeReference<List<BadgeViewModel>>() {
                });
            }

            @Override
            public void onTaskExecuted(ResponseEntity<List<BadgeViewModel>> result) {
                Log.d("Elo", result.getBody().toString());
                ViewPager viewPager = view.findViewById(R.id.badge_viewpager);
                viewPager.setAdapter(new DisplayBadgePagerAdapter(getContext(), result.getBody()));
            }

        }).execute(request_address + "/hikers/" + hiker_id + "/badges");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //onAttach
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //onDetach
    }

}
