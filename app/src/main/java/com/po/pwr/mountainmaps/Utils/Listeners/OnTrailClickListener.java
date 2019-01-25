package com.po.pwr.mountainmaps.Utils.Listeners;

import android.view.View;

public interface OnTrailClickListener {
    void onItemClick(View v, Integer trailId);
    boolean onLongLick(View v, Integer trailId);
}