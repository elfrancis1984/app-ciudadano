package ec.gob.mdt.ciudadano.util;

import android.os.StrictMode;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by francisco on 16/09/16.
 */
public class WebUtils {
    public static boolean isServerOnLine(String url){
        int iHTTPStatus;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Making HTTP request
        try {
            URL pageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) pageUrl.openConnection();
            iHTTPStatus = conn.getResponseCode();
            conn.connect();
            if(iHTTPStatus == 200)
                return true;
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
