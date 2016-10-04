package ec.gob.mdt.ciudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import ec.gob.mdt.ciudadano.modelo.RestEntityUsuario;

/**
 * Created by francisco chalan on 03/10/16.
 */
public abstract class DaoUsuario {

    public static int numeroFilasUsuario() {
        SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM ciu_usuario", null);
        int t = c.getCount();
        c.close();
        return t;
    }

    public static void guardarUsuario(RestEntityUsuario usuario) {
        if (usuario != null) {
            SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
            String queryUsuario = " INSERT INTO ciu_usuario ( ";
            queryUsuario += "identificacion,nombre,apellidos,correo,contrasenna )  VALUES ('";
            queryUsuario += usuario.getIdentificacion();
            queryUsuario += "','";
            queryUsuario += usuario.getNombre();
            queryUsuario += "','";
            queryUsuario += usuario.getApellidos();
            queryUsuario += "','";
            queryUsuario += usuario.getCorreo();
            queryUsuario += "','";
            queryUsuario += usuario.getContrasenna();
            queryUsuario += "')";
            db.execSQL(queryUsuario);
        }
    }

    public static RestEntityUsuario recuperarUsuario(String identificacion){
        SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
        RestEntityUsuario tmp = null;
        String sql = "SELECT identificacion,nombre,apellidos,correo,contrasenna FROM ciu_usuario where identificacion = '"+identificacion+"';";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            try {
                tmp = new RestEntityUsuario();
                do {
                    tmp.setIdentificacion(c.getString(0));
                    tmp.setNombre(c.getString(1));
                    tmp.setApellidos(c.getString(2));
                    tmp.setCorreo(c.getString(3));
                    tmp.setContrasenna(c.getString(4));
                } while (c.moveToNext());
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        return tmp;
    }
}
