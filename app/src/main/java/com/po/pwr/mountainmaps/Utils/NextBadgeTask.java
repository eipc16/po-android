package com.po.pwr.mountainmaps.Utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.po.pwr.mountainmaps.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NextBadgeTask extends RequestTask {

    private Context context;
    private View view;

    public NextBadgeTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..

        TextView nextBadgeName;
        TextView currentPoints;
        TextView requiredPoints;
        TextView missingPoints;

        nextBadgeName = view.findViewById(R.id.badge_next);
        currentPoints = view.findViewById(R.id.badge_points);
        requiredPoints = view.findViewById(R.id.badge_req_points);
        missingPoints = view.findViewById(R.id.badge_diff);

        JSONObject next_badge = null;
        try {
            next_badge = new JSONObject(result);

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
}
