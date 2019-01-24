package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

public class HikingTrail extends ViewModel {

    public Integer id;
    public String name;
    public String date;

    public HikingTrail(Integer id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }
}
