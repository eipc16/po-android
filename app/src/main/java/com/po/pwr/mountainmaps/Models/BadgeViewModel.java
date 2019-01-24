package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BadgeViewModel extends ViewModel {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("display_name")
    private String display_name;

    @JsonProperty("name")
    private String name;

    @JsonProperty("date")
    private Date date;

    public BadgeViewModel() {

    }

    public BadgeViewModel(Integer id, String display_name, String name, Date date) {
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


    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "ID: %d, DisplayName: %s, Name: %s, Date: %s", getId(), getDisplayName(), getName(), getDate());
    }
}
