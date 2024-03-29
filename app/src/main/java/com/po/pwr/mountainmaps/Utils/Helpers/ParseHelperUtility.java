package com.po.pwr.mountainmaps.Utils.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fasterxml.jackson.databind.JsonNode;
import com.po.pwr.mountainmaps.R;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Pomocnicza klasa do parsowania danych
 */
public final class ParseHelperUtility {

    private ParseHelperUtility() { /*supress Utility warning */ }

    /** Metoda parsujaca date
     * @param dateString Ciag znakow w formacie daty
     * @return Data zgodna z formatem SQL
     */
    public static Date parseDate(String dateString) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        java.util.Date date = null;

        try {
            date = sdf1.parse(dateString);
        } catch (ParseException e) {
            Log.e("HikingTrailCreator", e.toString());
        }

        return new Date(Objects.requireNonNull(date).getTime());
    }

    /** Metoda przygotowujaca ciag znaku z danymi trasy
     * @param context Obecny kontekst
     * @param view Obecny widok
     * @param response Dane do wyswietlenia
     * @return Ciag znakow do wyswietlenia
     */
    public static String prepareDialogData(Context context, View view, JsonNode response) {
        EditText editName = view.findViewById(R.id.hikingTrailName);

        Double distance = response.get("dist").asDouble(0) / 1000;
        Integer points = response.get("points").asInt(0);
        Double time = response.get("time").asDouble(0);

        Integer hours = (int) Math.floor(time);
        Integer minutes = (int) (60 * (time - Math.floor(time)));

        String result = "";
        result += context.getResources().getString(R.string.details_title, editName.getText().toString()) + "\n";
        result += context.getResources().getString(R.string.details_dist, distance) + "\n";
        result += context.getResources().getString(R.string.details_points, points) + "\n";
        result += context.getResources().getString(R.string.details_time, hours,  minutes);

        return result;
    }
}
