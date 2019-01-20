package com.po.pwr.mountainmaps.Utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.po.pwr.mountainmaps.Models.Badge;
import com.po.pwr.mountainmaps.Models.HikingTrail;
import com.po.pwr.mountainmaps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HikingTrailTask extends RequestTask {
    private Context context;
    private View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HikingTrailTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..

        //result = "[{\"date\":\"2019-01-01\",\"name\":\"badge_2\",\"id\":\"2\" }, { \"date\":\"2019-01-02\",\"name\":\"badge_3\",\"id\":\"3\" }]";

        ArrayList<HikingTrail> hikingTrails = new ArrayList<>();
        JSONArray json = null;
        try {
            json = new JSONArray(result);
            Log.d("json_log", result);
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

        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HikingTrailListAdapter(hikingTrails);
        mRecyclerView.setAdapter(mAdapter);
    }
}
