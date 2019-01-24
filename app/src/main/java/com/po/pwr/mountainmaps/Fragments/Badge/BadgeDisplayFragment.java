package com.po.pwr.mountainmaps.Fragments.Badge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.Badge;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.DisplayBadgePagerAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;


public class BadgeDisplayFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final static Integer id = 2;
    public String title;

    private String mParam1 = "";
    private String mParam2 = "";

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
        final View view = inflater.inflate(R.layout.fragment_badge_display, container, false);

        final TextView nextBadgeName = view.findViewById(R.id.badge_next);
        final TextView currentPoints = view.findViewById(R.id.badge_points);
        final TextView requiredPoints = view.findViewById(R.id.badge_req_points);
        final TextView missingPoints = view.findViewById(R.id.badge_diff);

        /* Pobierz i przygotuj dane o nastepnej odznace */
        try {
            //NextBadgeTask task = (NextBadgeTask) new NextBadgeTask(getContext(), view).execute(request_address + "/hikers/" + hiker_id + "/next");
            new RequestTask(new RequestTask.OnTaskExecutedListener() {
                @Override
                public void onTaskExecuted(String result) {
                    try {
                        JSONObject next_badge = new JSONObject(result);

                        if(next_badge.length() > 0) {
                            String name = null;
                            try {
                                name = next_badge.getString("next_badge");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Integer currentPointsValue = Integer.parseInt(next_badge.getString("current_points"));
                            Integer requiredPointsValue = Integer.parseInt(next_badge.getString("required_points"));
                            Integer missingPointsValue = Integer.parseInt(next_badge.getString("missing_points"));

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute(request_address + "/hikers/" + hiker_id + "/next");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Pobierz i przygotuj liste odznak usera */
        try {
            //BadgesTask task = (BadgesTask) new BadgesTask(getContext(), view).execute(request_address + "/hikers/" + hiker_id + "/badges");
            new RequestTask(new RequestTask.OnTaskExecutedListener() {
                @Override
                public void onTaskExecuted(String result) {
                    ArrayList<Badge> badgeList = new ArrayList<>();
                    JSONArray json = null;
                    try {
                        json = new JSONArray(result);
                        for(int i = 0; i < json.length(); i++) {
                            JSONObject e = json.getJSONObject(i);
                            badgeList.add(new Badge(
                                    e.getInt("id"),
                                    e.getString("display_name"),
                                    "badge_" + (Integer.parseInt(e.getString("id")) - 2),
                                    (Date) e.get("date")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ViewPager viewPager = view.findViewById(R.id.badge_viewpager);
                    viewPager.setAdapter(new DisplayBadgePagerAdapter(getContext(), badgeList));
                }
            }).execute(request_address + "/hikers/" + hiker_id + "/badges");
        } catch (Exception e) {
            e.printStackTrace();
        }

        title = getResources().getString(R.string.badges_drawer);

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

    public String loadJSONfromURL(String path, String method) throws Exception {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending GET request to URL" + path);
        System.out.println("Reponse Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        return response.toString();
    }
}
