package com.po.pwr.mountainmaps.Utils.Tasks;

import android.os.AsyncTask;

public class RequestTask extends AsyncTask<String, String, String> {

    public interface OnTaskExecutedListener {
        void onTaskExecuted(String result);
    }

    private OnTaskExecutedListener listener;


    public RequestTask(OnTaskExecutedListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... uri) {
       // HttpClient httpclient = new DefaultHttpClient();
//        HttpResponse response;
//        String responseString = null;
//        try {
//           // response = httpclient.execute(new HttpGet(uri[0]));
//            StatusLine statusLine = response.getStatusLine();
//            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                response.getEntity().writeTo(out);
//                responseString = out.toString();
//                out.close();
//            } else{
//                //Closes the connection.
//                response.getEntity().getContent().close();
//                throw new IOException(statusLine.getReasonPhrase());
//            }
//        } //catch (ClientProtocolException e) {
//            //TODO Handle problems..
//      //  } catch (IOException e) {
//            //TODO Handle problems..
//      //  }

        return " ";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..

        if(result == null)
            result = new String("[]");

        listener.onTaskExecuted(result);
    }
}