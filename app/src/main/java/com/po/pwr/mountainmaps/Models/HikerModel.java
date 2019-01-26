package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HikerModel extends ViewModel {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    public HikerModel(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public HikerModel() {
        //Used to map fields from JSON format
    }

    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
