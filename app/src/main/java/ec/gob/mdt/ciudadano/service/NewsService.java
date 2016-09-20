package ec.gob.mdt.ciudadano.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ec.gob.mdt.ciudadano.util.DateTimeUtils;
import ec.gob.mdt.ciudadano.util.NotifyUtil;

public class NewsService extends Service {
    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("Service", "News Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "News Service started");
         DateTimeUtils.cron(new Runnable() {
            public void run() {
                try {
                    NotifyUtil nU = new NotifyUtil(getApplicationContext());
                    nU.generaNotificacion(); //TODO solo esta de ejemplo
                }catch(Exception ex) {
                    ex.printStackTrace(); //or loggger would be better
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
}
