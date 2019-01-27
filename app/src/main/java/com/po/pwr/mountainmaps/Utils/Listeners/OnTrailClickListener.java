package com.po.pwr.mountainmaps.Utils.Listeners;

import android.view.View;

/**
 * Listener wykorzystywany w liscie tras turysty
 */
public interface OnTrailClickListener {
    void onItemClick(View v, Integer position);
    boolean onLongLick(View v, Integer position);
}