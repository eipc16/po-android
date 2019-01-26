package com.po.pwr.mountainmaps.Utils.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/** Klasa obslugujaca asynchroniczne zapytania do serwera
 * @param <T> Klasa zwracanego obiektu
 */
public class SpringRequestTask<T> extends AsyncTask<String, Void, ResponseEntity<T>> {

    /**
     * Metoda zapytania (GET, POST, itp)
     */
    private final HttpMethod method;

    /**
     * Sluchacz definiujacy zachowanie klasy
     */
    private final OnSpringTaskListener<T> listener;

    /** Interfejs zapytania
     * @param <T> Klasa obslugiwanego obiektu
     */
    public interface OnSpringTaskListener<T> {
        ResponseEntity<T> request(RestTemplate restTemplate, String url, HttpMethod method);
        void onTaskExecuted(ResponseEntity<T> result);
    }

    /** Konstruktor klasy
     * @param method Metoda zapytania (GET, POST, itp)
     * @param listener Sluchacz definiujacy zachowanie klasy
     */
    public SpringRequestTask(HttpMethod method, OnSpringTaskListener<T> listener) {
        this.method = method;
        this.listener = listener;
    }

    /** Definiuje dzialania wykonywane w tle
     * @param uri Parametry wywolania
     * @return Odpowiedz z serwera
     */
    @Override
    protected ResponseEntity<T> doInBackground(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = null;

        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            response = listener.request(restTemplate, url, method);
        } catch (Exception e) {
            Log.e("RequestTask Error", e.toString());
        }

        return response;
    }

    /** Definiuje dzialania wykonane po zakonczeniu zapytania
     * @param result Obiekt pobrany z serwera
     */
    @Override
    protected void onPostExecute(ResponseEntity<T> result) {
        super.onPostExecute(result);
        //Do anything with response..

        if(result != null) {
            listener.onTaskExecuted(result);
        } else {
            Log.e("Async Request Error", "Result is NULL");
        }
    }
}