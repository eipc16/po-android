package com.po.pwr.mountainmaps.Utils.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SpringRequestTask<T> extends AsyncTask<String, Void, ResponseEntity<T>> {

    private HttpMethod method;

    public interface OnSpringTaskListener<T> {
        ResponseEntity<T> request(RestTemplate restTemplate, String url, HttpMethod method);
        void onTaskExecuted(ResponseEntity<T> result);
    }

    private OnSpringTaskListener<T> listener;

    public SpringRequestTask(HttpMethod method, OnSpringTaskListener<T> listener) {
        this.method = method;
        this.listener = listener;
    }

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

    @Override
    protected void onPostExecute(ResponseEntity<T> result) {
        super.onPostExecute(result);
        //Do anything with response..

        if(result != null)
            listener.onTaskExecuted(result);
        else
            Log.e("Async Request Error", "Result is NULL");
    }
}