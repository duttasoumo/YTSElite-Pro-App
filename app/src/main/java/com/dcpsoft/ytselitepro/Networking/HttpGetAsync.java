package com.dcpsoft.ytselitepro.Networking;

import android.os.AsyncTask;

/**
 * Created by Soumya on 10-Jul-16.
 */
public class HttpGetAsync extends AsyncTask<String,Void,String> {

    private String url;
    private AsyncCallback asyncCallback;

    public HttpGetAsync(String url, AsyncCallback asyncCallback) {
        this.url = url;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected String doInBackground(String... strings) {
        return JSONhelper.getJSON(url);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(asyncCallback!=null) asyncCallback.onStart();
    }

    @Override
    protected void onPostExecute(String jsonReceive) {
        super.onPostExecute(jsonReceive);
        if(asyncCallback!=null) asyncCallback.onFinish(jsonReceive);

    }
}
