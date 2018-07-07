package com.example.android.popularmoviesparttwo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Nick on 6/18/2018.
 */

public class Connection {
    protected static String urlMovieRequest(URL url) {
        System.out.println("URL " + url.toString());
        try {
            String jsonResponse = null;
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(20000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // 200 is good, 401 is unauthorized
                System.out.println("RESPONSE CODE "+urlConnection.getResponseCode());
                if (urlConnection.getResponseCode() == 200) {
                    System.out.println("CONNECTION WORKED");
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readStream(inputStream);
                    System.out.println("JSON IN CONNECTION: " + jsonResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            System.out.println("JSON RESPONSE: " + jsonResponse);
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                builder.append(currentLine);
                currentLine = bufferedReader.readLine();
            }
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}