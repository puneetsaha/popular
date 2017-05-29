package com.example.puneetkumar.myapplication.webservices.http;

import java.util.HashMap;

/**
 * Created by kevin-alien on 25/11/2014.
 */
public interface IHttpClient {

    /**
     * Send a get request to the server and returns the string for it
     *
     * @param url
     * @param params
     * @return the server response
     */
    public String getData(final String url, final HashMap<String, String> params);

    /**
     * Send a post request to the server and returns the string for it
     *
     * @param url
     * @param params
     * @return the server response
     */
    public String postData(final String url,
                           final HashMap<String, String> params);

}
