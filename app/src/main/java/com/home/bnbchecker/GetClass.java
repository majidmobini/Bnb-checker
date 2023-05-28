package com.home.bnbchecker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetClass {

    static String GetData(String urlString)
    {
        String response = "";
        try {

            URL url = new URL(urlString);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(30000);
            connect.setConnectTimeout(30000);
            connect.setRequestMethod("GET");
           // String encoded = android.util.Base64.encodeToString(("username"+":"+"password").getBytes(), Base64.NO_WRAP);
          //  connect.setRequestProperty("Authorization","Basic "+encoded);
            connect.setDoInput(true);
            //connect.setDoOutput(true);

           /* OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jso.toString());
            writer.flush();
            writer.close();
            os.close();*/
            Log.d("Calling",urlString);
            int responseCode = connect.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                while ((line = br.readLine()) != null)
                {
                    response+=line;
                }
            }
            else
            {
                Log.e("ServerError",connect.getResponseMessage());
                return "";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
