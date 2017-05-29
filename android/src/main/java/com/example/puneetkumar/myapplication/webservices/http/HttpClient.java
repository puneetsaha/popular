package com.example.puneetkumar.myapplication.webservices.http;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;

/**
 * Created by kevin-alien on 25/11/2014.
 */
public class HttpClient implements IHttpClient {

    public String TAG = HttpClient.class.getCanonicalName();

    okhttp3.OkHttpClient mClient;

    String serverErrorMessage = "not_available";

    public HttpClient() {
        mClient = new okhttp3.OkHttpClient ();
    }

    @Override
    public String getData(final String url, final HashMap<String, String> params) {
        final Uri.Builder builder = Uri.parse(url).buildUpon();
        for (final String key : params.keySet()) {
            builder.appendQueryParameter(key, params.get(key));
        }
        final okhttp3.Request request = new okhttp3.Request.Builder().get()
                .url(builder.toString()).build();
        try {
            mClient.newCall(request).execute();
            return mClient.newCall(request).execute().body().string();
        } catch (final IOException e) {
            e.printStackTrace();
            return serverErrorMessage;
        }

    }

    @Override
    public String postData(final String url,
                           final HashMap<String, String> params) {

        final FormBody.Builder builder = new FormBody.Builder();
        for (final String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        try {
            Log.d(TAG, "Calling url " + url);
            final okhttp3.Request req = new okhttp3.Request.Builder().url(url)
                    .post(builder.build()).build();
            final okhttp3.Response resp = mClient.newCall(req).execute();

            return resp.body().string();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
