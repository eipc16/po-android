package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ir.mirrajabi.searchdialog.core.Searchable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PointModel extends ViewModel implements Comparable<PointModel>, Searchable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    public PointModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public PointModel() {
        //Used to map fields from JSON format
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
        return "PointModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PointModel)) {
            return false;
        }

        PointModel other = (PointModel) o;
        return this.id.equals(other.id);
    }

    @Override
    public int compareTo(PointModel p) {
        return Integer.compare(this.id, p.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String getTitle() {
        return this.getName();
    }

    public boolean sameName(PointModel p) {
        return this.name.equals(p.name);
    }
}
