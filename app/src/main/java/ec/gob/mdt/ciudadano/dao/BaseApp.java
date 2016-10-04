package ec.gob.mdt.ciudadano.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ec.gob.mdt.ciudadano.util.Sesion;

public class BaseApp extends SQLiteOpenHelper {

    static String tablaNoticia = " CREATE TABLE ciu_noticia( " +
            " id integer  NOT NULL PRIMARY KEY," +
            " titulo text NOT NULL, " +
            " cuerpo text NOT NULL, " +
            " fecha text NOT NULL, " +
            " imagen text ) ";

    static String tablaUsuario = " CREATE TABLE ciu_usuario( " +
            " identificacion text  NOT NULL PRIMARY KEY," +
            " nombre text NOT NULL, " +
            " apellidos text NOT NULL, " +
            " correo text NOT NULL, " +
            " contrasenna text NOT NULL ) ";

    private static BaseApp instance;

    public static BaseApp getInstance() {
        return instance == null ? instance = new BaseApp() : instance;
    }

    private BaseApp() {
        super(Sesion.getContest(), "baseApp", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        crearTodaTabla(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        eliminarTodaTabla(db);
        crearTodaTabla(db);
    }

    public void eliminarCrearBase() {
        SQLiteDatabase db = this.getWritableDatabase();
        eliminarTodaTabla(db);
        crearTodaTabla(db);
    }

    private void crearTodaTabla(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL(tablaNoticia);
        db.execSQL(tablaUsuario);
    }

    private void eliminarTodaTabla(SQLiteDatabase db) {
        db.execSQL("DROP TABLE ciu_noticia");
        db.execSQL("DROP TABLE ciu_usuario");
    }

    public void borrarTablaCiuNoticia() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM ciu_noticia");
    }

    public void borrarTablaCiuUsuario() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM ciu_usuario");
    }
}