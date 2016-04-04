package com.example.elli.uchews;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Chris on 4/4/2016.
 */
public class WebServiceConnector {

    public enum Method {
        GET,
        POST
    }

    /**
     * Opens a HttpURLConnection to the provided url
     * @param url_path Base url path to the webservice
     * @param methodType The HTTP connection method of type {@link com.example.elli.uchews.WebServiceConnector.Method}
     * @param params A HashMap containing the string key-value pairs
     * @return A {@link HttpURLConnection} which is ready to be read from
     * @throws IOException
     */
    public static HttpURLConnection openWebServiceConnection(String url_path, Method methodType, HashMap<String, String> params) throws IOException {
        if(methodType == Method.GET)
        {
            url_path = handleGETParams(url_path, params);
        }

        URL url = new URL(url_path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(methodType.name());
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");

        //Write params
        if(methodType == Method.POST) {
            byte[] paramData = handlePOSTParams(params);
            int paramLength = paramData.length;
            conn.setRequestProperty("Content-Length", Integer.toString(paramLength));
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(paramData);
            wr.close();
        }

        return conn;
    }

    public static String readResponse(HttpURLConnection conn) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response ="";
        String line = "";
        while((line = reader.readLine()) != null)
        {
            response = response.concat(line + "\n");
        }

        reader.close();

        return response;
    }

    /**
     *
     * @param baseURL
     * @param params
     * @return The new url path with params appended in http url encoded form
     */
    private static String handleGETParams(String baseURL, HashMap<String, String> params){
        return baseURL + "?" + stringifyParams(params);
    }

    private static byte[] handlePOSTParams(HashMap<String, String> params){
        String newParams = stringifyParams(params);
        return newParams.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * @param params The params in key value form
     * @return The params in following http-url-encoded standard
     */
    private static String stringifyParams(HashMap<String, String> params){
        String paramString = "";
        Iterator<String> keys = params.keySet().iterator();

        while(keys.hasNext()){
            String key = keys.next();
            String value = params.get(key);

            paramString += key + "=" + value + "&";
        }

        //Remove trailing '&'
        paramString = paramString.substring(0, paramString.length()-1);

        return paramString;
    }


}
