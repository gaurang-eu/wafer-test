package util;

import java.io.IOException;

public class ServerUtil {
    public ServerUtil() {

    }

    public String getDataFromUrl(String url) throws IOException, Error {
        String data = "";

        return data;
    }

//	public String getDataFromUrl(String url) throws ClientProtocolException, IOException{
//		String data="";
//		Log.i("url ==",""+url);
//		HttpParams httpParameters = new BasicHttpParams();
//		// Set the timeout in milliseconds until a connection is established.
//		// The default value is zero, that means the timeout is not used.
//		int timeoutConnection = 10000;
//		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//		// Set the default socket timeout (SO_TIMEOUT)
//		// in milliseconds which is the timeout for waiting for data.
//		int timeoutSocket = 50000;
//		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//
//		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
//		HttpGet httpGet = new HttpGet(url);
//		HttpResponse httpResponse = httpClient.execute(httpGet);
//		DevLogger.logError(ServerUtil.class, httpResponse.getStatusLine().toString());
//		HttpEntity httpEntity = httpResponse.getEntity();
//		data = EntityUtils.toString(httpEntity);
//		return(data);
//	}


}
