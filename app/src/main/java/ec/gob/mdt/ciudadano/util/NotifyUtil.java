package ec.gob.mdt.ciudadano.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;

import ec.gob.mdt.ciudadano.activity.MainActivity;
import ec.gob.mdt.ciudadano.R;

/**
 * Created by francisco on 16/09/16.
 */
public class NotifyUtil {
    // Variables de la notificacion
    NotificationManager nm;
    Notification notif;
    static String ns = Context.NOTIFICATION_SERVICE;
    //private static final String DESDE_NOTIFICACION = "desdeNotificacion";

    //Defino el icono de la notificacion en la barra de notificacion
    int icono = R.drawable.icononotificacion;

    Context ctx;

    public NotifyUtil(Context ctx) {
        this.ctx = ctx;
    }

    public void generaNotificacion(){
        nm = (NotificationManager) ctx.getSystemService(ns);
        notificacion(icono, "Descargar nuevas noticias?","App ciudadano", "Hay nuevas noticias disponibles");
        nm.cancelAll();
        nm.notify(1, notif);
    }

    @SuppressWarnings("deprecation")
    public void notificacion(int icon, CharSequence textoEstado, CharSequence titulo, CharSequence texto) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(icon)
                        .setLargeIcon((((BitmapDrawable) ctx.getResources().getDrawable(icon)).getBitmap()))
                        .setContentTitle(titulo)
                        .setContentText(textoEstado)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                        //.setLights(0x1E90FF, 1000, 500)
                        .setAutoCancel(true);
                        //.setContentInfo("10")
                        //.setTicker(texto);

        NotificationCompat.InboxStyle inBoxStyle = new NotificationCompat.InboxStyle();
        String[] events = {"noticia 1","noticia 2","noticia 3","noticia 4","noticia 5","noticia 6"};
        inBoxStyle.setBigContentTitle("Nuevas noticias:");
        for (int i=0; i < events.length; i++) {
            inBoxStyle.addLine(events[i]);
        }
        mBuilder.setContentInfo(""+events.length);
        mBuilder.setStyle(inBoxStyle);

        Intent notIntent = new Intent(ctx, MainActivity.class);
        //notIntent.putExtra(DESDE_NOTIFICACION,true);
        PendingIntent contIntent = PendingIntent.getActivity(ctx, 0, notIntent, 0);
        mBuilder.setContentIntent(contIntent);
        notif = mBuilder.build();
    }
}