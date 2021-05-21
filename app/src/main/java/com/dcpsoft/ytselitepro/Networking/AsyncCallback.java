package com.dcpsoft.ytselitepro.Networking;

/**
 * Created by Soumya on 07-Nov-15.
 */
public interface AsyncCallback {
    void onStart();
    void onFinish(String jsonReceive);

}
