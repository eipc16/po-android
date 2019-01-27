package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HikingTrailModel extends ViewModel implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("finished")
    private Boolean finished;

    @JsonProperty("hiking_points")
    private List<Integer> pointsIds;

    public HikingTrailModel(Integer id, String name, Date date, Boolean finished) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.finished = finished;
    }

    public HikingTrailModel() {
        //Used to map fields from JSON format
    }

    public List<Integer> getPoints() {
        Log.d("Lista", pointsIds != null ? pointsIds.toString() : "Nie ma mnie");
        return this.pointsIds;
    }

    public void setPoints(List<Integer> points) {
        this.pointsIds = points;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public Boolean isFinished() {
        return this.finished;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof HikingTrailModel)) {
            return false;
        }

        HikingTrailModel other = (HikingTrailModel) o;
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
