package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HikingTrailViewModel extends ViewModel {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("date")
    public Date date;

    @JsonProperty("finished")
    public Boolean finished;

    public HikingTrailViewModel(Integer id, String name, Date date, Boolean finished) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.finished = finished;
    }

    public HikingTrailViewModel() {

    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof HikingTrailViewModel))
            return false;

        HikingTrailViewModel other = (HikingTrailViewModel) o;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "HikingTrail: " + id + " | Name: " + name;
    }
}
