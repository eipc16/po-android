package com.po.pwr.mountainmaps.Models;

import android.arch.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.Locale;

/**
 * Model odznaki turysty
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BadgeModel extends ViewModel {

    /**
     * Id odznaki
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * Wyswietlana nazwa odznaki
     */
    @JsonProperty("display_name")
    private String display_name;

    /**
     * Nazwa pliku z obrazem odznaki
     */
    @JsonProperty("name")
    private String name;

    /**
     * Data zdobycia
     */
    @JsonProperty("date")
    private Date date;

    /**
     * Konstrukor domyslny modelu odznaki
     */
    public BadgeModel() {
        //Used to map fields from JSON format
    }

    /** Konstrukor odznaki
     * @param id Id odznaki
     * @param display_name Wyswietlana nazwa odznaki
     * @param name Nazwa pliku z obrazem odznaki
     * @param date Data zdobycia odznaki
     */
    public BadgeModel(Integer id, String display_name, String name, Date date) {
        this.id = id;
        this.display_name = display_name;
        this.name = name;
        this.date = date;
    }

    /** Pozwala na pobranie id odznaki
     * @return Id odznaki
     */
    public Integer getId() {
        return id;
    }

    /** Pozwala na ustawienie nowego id odznaki
     * @param id Nowe id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /** Pozwala na pobranie wyswietlanej nazwy odznaki
     * @return Pobierz nazwe odznaki
     */
    public String getDisplayName() {
        return display_name;
    }

    /** Pozwala na zmiane wyswietlanej nazwy odznaki
     * @param display_name Nowa nazwa
     */
    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }

    /** Pozwala na pobranie nazwy pliku z obrazem odznaki
     * @return Nazwa pliku z obrazem odznaki
     */
    public String getName() {
        return name;
    }

    /** Pozwala na pobranie daty zdobycia odznaki
     * @return Data zdobycia odznaki
     */
    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BadgeModel)) {
            return false;
        }

        return ((BadgeModel) o).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "ID: %d, DisplayName: %s, Name: %s, Date: %s", getId(), getDisplayName(), getName(), getDate());
    }
}
