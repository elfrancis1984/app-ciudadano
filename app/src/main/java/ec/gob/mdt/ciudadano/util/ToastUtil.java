package ec.gob.mdt.ciudadano.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ec.gob.mdt.ciudadano.R;

/**
 * Created by francisco chalan on 03/10/16.
 */
public class ToastUtil {

    public static void showToastLong(Context context, String mensaje){
        Toast toast = Toast.makeText(context,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public static void showToastShort(Context context, String mensaje){
        Toast toast = Toast.makeText(context,mensaje,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public static void showCustomToast(Context context, String mensaje){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity)context).findViewById(R.id.custom_toast_container));
        int Y = ((AppCompatActivity)context).getSupportActionBar().getHeight();

        TextView text = (TextView) layout.findViewById(R.id.text_custom_toast);
        text.setText(mensaje);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP|Gravity.END, 0, Y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
