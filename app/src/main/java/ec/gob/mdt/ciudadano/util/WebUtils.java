package ec.gob.mdt.ciudadano.util;

import android.os.StrictMode;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

import retrofit2.http.POST;

/**
 * Created by francisco on 16/09/16.
 */
public class WebUtils {

    public static boolean pingServer() {
        accesoInternet();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(Properties.SERVER_URL, Properties.SERVER_PORT), Properties.TIMEOUT);
            return socket.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void accesoInternet() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
