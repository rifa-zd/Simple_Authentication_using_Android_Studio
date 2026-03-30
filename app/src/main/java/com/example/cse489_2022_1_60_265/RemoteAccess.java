package com.example.cse489_2022_1_60_265;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URLEncodedUtils;
import java.io.*;
import java.util.List;
import java.net.*;
@SuppressWarnings("ALL")
public class RemoteAccess {
//Singleton class
    private String TAG = "RemoteAccess";
    private static RemoteAccess instance = new RemoteAccess();
    private RemoteAccess() {}
    public static RemoteAccess getInstance() {
        return instance;
    }

//    wrapper methos - makeHttpRequest - where req send, which method to send req, what are the req
    public String makeHttpRequest(String url, String method, List<NameValuePair> params) {
//whole task is a long running task -> need to be thread, asynchronus
        HttpURLConnection http = null;
        InputStream is = null;
        String data = "";
        // Making HTTP request
        try {
            // check for request method
            if (method.equals("POST")) {
                //httpClient = new DefaultHttpClient();
                if(params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
//                    converting all param to URL
                    url += "?" + paramString;
                }
            }
            System.out.println("@RemoteAccess-"+": "+ url);
//            opening that url
            URL urlc = new URL(url);
            http = (HttpURLConnection) urlc.openConnection();
            System.out.println("Remot Access Here 3");
            http.connect(); //sending ping req to server
//            if success then will have string data in return
            is = http.getInputStream(); //is = input string
            System.out.println("Remot Access Here 4");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            System.out.println("Returning");
            return sb.toString(); //whole data came from server snd returning to the method
        } catch (Exception e) {
            System.out.println("Catching");
            e.printStackTrace();
        }
        try {
            System.out.println("remote Acess 5");
            http.disconnect();
        } catch (Exception e) {
        }
        return null;
    }
}