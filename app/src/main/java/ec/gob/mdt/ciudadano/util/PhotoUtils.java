package ec.gob.mdt.ciudadano.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.StrictMode;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by francisco on 09/09/16.
 */
public class PhotoUtils {

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String img, int reqWidth, int reqHeight, Context ctx) {

        final File filesDir = ctx.getApplicationContext().getFilesDir();

        File imgFile = new File(filesDir.getAbsolutePath()+"/" + img +".jpg");

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
    }

    public static Bitmap retrieveImagenByPath(String name, Context ctx){
        try {
            final File filesDir = ctx.getApplicationContext().getFilesDir();
            File imgFile = new File(filesDir.getAbsolutePath()+"/" + name +".jpg");
            if(imgFile.exists())
                return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            else
                return null;
        }catch (Exception e){
            return null;
        }
    }

    public static Bitmap retrieveImagenByPathScaled(String name, Context ctx,  boolean scaled, int width, int height){
        try {
            final File filesDir = ctx.getApplicationContext().getFilesDir();
            File imgFile = new File(filesDir.getAbsolutePath()+"/" + name +".jpg");
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            if(scaled)
                return Bitmap.createScaledBitmap(bitmap, width, height, true);
            else
                return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        }catch (Exception e){
            return null;
        }
    }

    public static Bitmap obtenerImagenUrl(String url){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn= (HttpURLConnection) imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            return BitmapFactory.decodeStream(conn.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void guardarImagen(Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        //File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File dirImages =  context.getApplicationContext().getFilesDir();
        File myPath = new File(dirImages, nombre + ".jpg");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        //return myPath.getAbsolutePath();
    }

    public static BitmapDrawable getThumbnail(String name, Context ctx){

        final File filesDir = ctx.getApplicationContext().getFilesDir();
        File imgFile = new File(filesDir.getAbsolutePath()+"/" + name +".jpg");

        Bitmap bitmapOrg = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        int newWidth = 200;
        int newHeight = 200;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap =  Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

        return bmd;
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
