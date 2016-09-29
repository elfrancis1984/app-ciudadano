package ec.gob.mdt.ciudadano.util;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ec.gob.mdt.ciudadano.dto.NoticiaDto;
import ec.gob.mdt.ciudadano.modelo.ListEntidadNoticiaCiu;

public class Sesion extends Application {

    private static Sesion instance;

    public String perIdentificacion;
    public String mensaje;
    public String mensajeVersionAntigua;

    public ListEntidadNoticiaCiu listaNoticias;

    public static long CANTON_ID;

    public boolean tieneAcceso;
    public boolean validarConexion;
    public boolean botonBorrarBaseDisponible;

    public static final String APP_VERSION = "v 1.7.7";
    public static int resourceConexion;

    public static final String CODIGO_SEGURIDAD = "1111111333333313";

    public Sesion() {
        instance = this;
        this.tieneAcceso = false;
    }

    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable e) {
                if (isUIThread()) {
                    handleUncaughtException(thread, e);
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            handleUncaughtException(thread, e);
                        }
                    });
                }
            }
        });
    }

    public boolean isUIThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace();

        Intent intent = new Intent("ec.gob.mdt.sgi.ERROR");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        System.exit(1);
    }

    public static Context getContest() {
        return instance;
    }

    public void cerrarSesion() {
//        entidadPlanificacionSgi = new EntidadPlanificacionSgi();
//        empleadoEmpresaSgi = new EntidadEmpleadoEmpresaSgi();
        this.tieneAcceso = false;
        this.mensaje = "";
        this.perIdentificacion = "";
    }

    public boolean isAccessToNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean pingSGIServer() {
        try (Socket socket = new Socket()) {
//            socket.connect(new InetSocketAddress(SERVICIOS.SERVER_ADDRESS, SERVICIOS.SERVER_PORT), SERVICIOS.TIMEOUT);
//            return socket.isConnected();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void changeMobileData(boolean status) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
            connectivityManagerField.setAccessible(true);
            final Object connectivityManager = connectivityManagerField.get(conman);
            final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(connectivityManager, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Activity> getActivities() throws Exception {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);

        Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
        if (activities == null)
            return null;

        List<Activity> result = new ArrayList<>();

        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field activityField = activityRecordClass.getDeclaredField("activity");
            activityField.setAccessible(true);
            result.add((Activity) activityField.get(activityRecord));
        }
        return result;
    }

    public String getDeviceInfo() {
        String info = "";
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String macAddress = manager.getConnectionInfo().getMacAddress();
        String deviceName = BluetoothAdapter.getDefaultAdapter().getName();
        info += "Dispositivo:" + deviceName + ";;MAC:" + macAddress;
        return info;
    }
}