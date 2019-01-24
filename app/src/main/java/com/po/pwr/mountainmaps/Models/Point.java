package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Point extends ViewModel {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("id")
    private String name;

    public Point(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Point() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean equals(Point p) {
        return p.name.equals(this.name);
    }
}
