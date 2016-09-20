package ec.gob.mdt.ciudadano.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ec.gob.mdt.ciudadano.modelo.EntidadNoticiaCiu;

/**
 * Created by francisco on 06/09/16.
 */
public abstract class DaoNoticia {

    public static int numeroFilasCatalogo() {
        SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ciuId FROM ciu_noticia", null);
        int t = c.getCount();
        c.close();
        return t;
    }

    public static void guardarListaNoticiaNative(List<EntidadNoticiaCiu> listaNoticia){

        for (int i = 0; listaNoticia.size() > i; i++) {
            String sql = "INSERT INTO ciu_noticia (ciuId, ciuTitulo,ciuCuerpo,ciuImagenBlob) VALUES(?,?,?,?)";
            SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
            SQLiteStatement insertStmt = db.compileStatement(sql);
            insertStmt.clearBindings();
            insertStmt.bindString(1, Integer.toString(listaNoticia.get(i).ciuId));
            insertStmt.bindString(2, listaNoticia.get(i).ciuTitulo);
            insertStmt.bindString(3, listaNoticia.get(i).ciuCuerpo);
            //insertStmt.bindBlob(4, listaNoticia.get(i).ciuImagenBlob);
            insertStmt.executeInsert();
            db.close();
        }
    }

    public static void guardarListaNoticia(List<EntidadNoticiaCiu> listaNoticia) {
        if (listaNoticia != null && listaNoticia.size() > 0) {
            SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
            String queryNoticia = " INSERT INTO ciu_noticia ( ";
            queryNoticia += "ciuId, ciuTitulo,ciuCuerpo,ciuImagen )  VALUES ";

            for (int i = 0; listaNoticia.size() > i; i++) {
                queryNoticia += agregarRegistroNoticia(listaNoticia.get(i)) + ",";
                Log.e("SQL: ", agregarRegistroNoticia(listaNoticia.get(i)));
            }

            db.execSQL(queryNoticia.substring(0,queryNoticia.length()-1));
        }
    }

    private static String agregarRegistroNoticia(EntidadNoticiaCiu noticia){
        String registro = "(";
        registro += noticia.ciuId;
        registro += ",'";
        registro += noticia.ciuTitulo.trim();
        registro += "','";
        registro += noticia.ciuCuerpo.trim();
        registro += "','";
        registro += noticia.ciuImagen.trim();
        registro += "')";
        return registro;
    }

    public static List<EntidadNoticiaCiu> getEntidadNoticiaCiu() {
        SQLiteDatabase db = BaseApp.getInstance().getWritableDatabase();
        List<EntidadNoticiaCiu> Lista = new ArrayList<EntidadNoticiaCiu>();
        Cursor c = db.rawQuery("SELECT ciuId, ciuTitulo,ciuCuerpo,ciuImagen FROM ciu_noticia order by ciuId desc;", null);
        if (c.moveToFirst()) {
            try {
                do {
                    EntidadNoticiaCiu tmp = new EntidadNoticiaCiu(c.getInt(0),c.getString(1),c.getString(2),c.getString(3));
                    Lista.add(tmp);
                } while (c.moveToNext());
            } finally {
                if (c != null && !c.isClosed())
                    c.close();
            }
        }
        return Lista;
    }
}
