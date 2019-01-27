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
import com.po.pwr.mountainmaps.Models.BadgeModel;
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


/**
 * Fragment zawierający dane o nastepnej odznace i ViewPager ze zdobytymi odznakami
 */
public class BadgeDisplayFragment extends Fragment {
    public final static Integer id = 2;
    private String title;

    public BadgeDisplayFragment() {
        //Create new BadgeDisplayFragment
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

    /** Metoda wyświetlająca dane o następnej do zdobycia odznace
     * @param view Obecny widok
     */
    private void loadNextBadgeData(final View view) {
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

    /** Metoda tworząca ViewPager z odznakami turysty
     * @param view Obecny widok
     */
    private void loadUserBadges(final View view) {
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<List<BadgeModel>>() {

            @Override
            public ResponseEntity<List<BadgeModel>> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, new ParameterizedTypeReference<List<BadgeModel>>() {
                });
            }

            @Override
            public void onTaskExecuted(ResponseEntity<List<BadgeModel>> result) {
                Log.d("Elo", result.getBody().toString());
                ViewPager viewPager = view.findViewById(R.id.badge_viewpager);
                viewPager.setAdapter(new DisplayBadgePagerAdapter(getContext(), result.getBody()));
            }

        }).execute(request_address + "/hikers/" + hiker_id + "/badges");
    }

}
