package com.po.pwr.mountainmaps.Utils;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import com.po.pwr.mountainmaps.R;

import org.json.JSONException;
import org.json.JSONObject;


public class DrawerNameTask extends RequestTask {
    private Context context;
    private View view;

    public DrawerNameTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUser = headerView.findViewById(R.id.nav_title);

        try {
            JSONObject hikerCredentials = new JSONObject(result);
            String newUser = hikerCredentials.getString("first_name") + " " + hikerCredentials.getString("last_name");
            navUser.setText(newUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
