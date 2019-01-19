package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import java.util.Locale;

public class Badge extends ViewModel {

    private Integer id;
    private String display_name, name;
    private String date;

    public Badge(Integer id, String display_name, String name, String date) {
        this.id = id;
        this.display_name = display_name;
        this.name = name;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getName() {
        return name;
    }


    public String getDate() {
        return date;
    }

    @Override
    public String toString() {

        return String.format(Locale.US, "ID: %d, DisplayName: %s, Name: %s, Date: %s", getId(), getDisplayName(), getName(), getDate());
    }
}
