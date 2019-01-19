package com.po.pwr.mountainmaps.Utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.po.pwr.mountainmaps.Models.Badge;
import com.po.pwr.mountainmaps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BadgesTask extends RequestTask {

    private Context context;
    private View view;

    public BadgesTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..


        ArrayList<Badge> badgeList = new ArrayList<>();
        JSONArray json = null;
        try {
            json = new JSONArray(result);
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

        ViewPager viewPager = view.findViewById(R.id.badge_viewpager);
        viewPager.setAdapter(new DisplayBadgePagerAdapter(context, badgeList));
    }
}
