package com.wafermessenger.gaurangpatel.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InternetUtil {
    private static final String TAG = "InternetUtil";
    private Context context;

    public InternetUtil(Context cnt) {
        context = cnt;
    }

    public static boolean ping(String u) {
        try {
            URL url = new URL(u);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(5000); // Time is in Milliseconds to wait for ping response
            urlc.connect();
            InputStreamReader in = new InputStreamReader((InputStream) urlc.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line;
            StringBuffer text = new StringBuffer();
            do {
                line = buff.readLine();
                text.append(line + "\n");
            } while (line != null);

            if (text.toString().length() < 400) {
                return false;
            }
            if (urlc.getResponseCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e1) {
            Log.e(TAG, "Erroe in URL to ping " + e1);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Error in ping " + e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error in ping " + e);
            return false;
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//	    android.os.Build.VERSION.SDK_INT = 19 for KitKat 4.4.2
        if (android.os.Build.VERSION.SDK_INT > 19) {
            return (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting());
        } else {
            if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
//		            return ping("http://www.google.com");
                return true;
            } else {
                return false;
            }
        }

    }

    public boolean isNetworkAvailableSimple() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
