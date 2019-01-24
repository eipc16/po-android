package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HikerViewModel extends ViewModel {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    public HikerViewModel(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public HikerViewModel() {

    }

    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
