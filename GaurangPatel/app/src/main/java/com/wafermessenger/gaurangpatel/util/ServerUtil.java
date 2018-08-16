package com.wafermessenger.gaurangpatel.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerUtil {

    public ServerUtil() {
    }

    public String getDataFromUrl(String strUrl) throws MalformedURLException, IOException {
        String data = null;
        Log.i("url ==", "" + strUrl);
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream is = new BufferedInputStream(conn.getInputStream());
            byte[] buffer = new byte[5120];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toString();
        } else {
            return (data);
        }


    }


//	Below code getPostData won't work since
//	If you change your target sdk to Android M (API 23) then all deprecated classes related to HTTP client are no longer available and Android-async-http won't work.
//	You need to add "useLibrary 'org.apache.http.legacy'" in your gradle configuration.

	/*
	public String  getPostData(String url,List<NameValuePair> nameValuePairs) {
		String postData="";
		int statusCode=500;
		// Create a new HttpClient and Post Header
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 30000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 30000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpPost httppost = new HttpPost(url);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpClient.execute(httppost);
			statusCode= response.getStatusLine().getStatusCode();
			postData = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return postData;
	}

	*/

}

