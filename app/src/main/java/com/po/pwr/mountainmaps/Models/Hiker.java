package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hiker extends ViewModel {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    public Hiker(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Hiker() {

    }

    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
