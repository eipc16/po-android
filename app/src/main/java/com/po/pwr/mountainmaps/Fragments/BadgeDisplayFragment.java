package com.po.pwr.mountainmaps.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.Badge;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.DisplayBadgePagerAdapter;
import com.po.pwr.mountainmaps.Utils.NextBadgeTask;
import com.po.pwr.mountainmaps.Utils.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class BadgeDisplayFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String hiker_id = "1";

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

        /* Pobierz i przygotuj dane o nastepnej odznace */
        try {
            NextBadgeTask task = (NextBadgeTask) new NextBadgeTask(getContext(), view).execute("http://10.0.2.2:8080/hikers/" + hiker_id + "/next");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Pobierz i przygotuj liste odznak usera */
        ArrayList<Badge> badgeList = new ArrayList<>();
        JSONArray json = null;
        try {
            json = new JSONArray(loadJSONFromAsset(view.getContext(), "badges.json"));
            for(int i = 0; i < json.length(); i++) {
                JSONObject e = json.getJSONObject(i);
                badgeList.add(new Badge(
                        Integer.parseInt(e.getString("id")),
                        e.getString("display_name"),
                        "badge_" + (Integer.parseInt(e.getString("id")) - 2),
                        e.getString("date")
                ));
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
