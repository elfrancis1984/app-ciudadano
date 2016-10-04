package ec.gob.mdt.ciudadano.util;

import android.os.StrictMode;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by francisco chalan on 27/09/16.
 */
public class RestUtils {

    public static Retrofit connectRest(String url, final String tipo){
        accesoInternet();
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Content-Type", tipo).build();
                return chain.proceed(newRequest);
            }
        };
        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS);
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return  retrofit;
    }

    public static Retrofit connectRestAuth(String url, final String token){
        accesoInternet();
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("s_header_params", token).build();
                return chain.proceed(newRequest);
            }
        };
        // Add the interceptor to OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS);
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return  retrofit;
    }

    public static void accesoInternet() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
