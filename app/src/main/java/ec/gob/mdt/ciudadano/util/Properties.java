package ec.gob.mdt.ciudadano.util;


public class Properties {

//    public final static String SISTEMA_NOMBRE = "APP-CIUDADANO";

    public final static String CONTENT_TYPE_JSON = "application/json";
    public final static String CONTENT_TYPE_TEXT = "text/plain";

    public final static String SHARED_PREFERENCES_USER_DATA = "USER_DATA";
    public final static String SHARED_PREFERENCES_USER_DATA_USER = "USER_NAME";
    public final static String SHARED_PREFERENCES_USER_DATA_PASS = "USER_PASS";
    public final static String SHARED_PREFERENCES_USER_DATA_ZONE = "USER_ZONE";

    public final static String SHARED_PREFERENCES_USER_DATA_NOMBRES = "USER_NOMBRES";
    public final static String SHARED_PREFERENCES_USER_DATA_APELLIDOS = "USER_APELLIDOS";
    public final static String SHARED_PREFERENCES_USER_DATA_EMAIL = "USER_EMAIL";
    public final static String SHARED_PREFERENCES_USER_DATA_REGISTRADO = "USER_REGISTRADO";
    public final static String SHARED_PREFERENCES_USER_DATA_PIN = "USER_PIN";
    public final static String SHARED_PREFERENCES_USER_DATA_TOKEN = "USER_TOKEN";

    public final static String MENSAJE_ERROR_REST_NOTICIAS = ":( Estamos presentando problemas al recuperar las noticias.";
    public final static String MENSAJE_ERROR_REST_LOGIN = ":( Estamos presentando problemas al iniciar la sesión.";
    public final static String MENSAJE_ERROR_REST_SIGNUP = ":( Estamos presentando problemas al realizar el registro.";
    public final static String MENSAJE_ERROR_REST_PIN = ":( Estamos presentando problemas al realizar la activación de su cuenta.";

    public final static String MENSAJE_ERROR_SISTEMA_NO_DISPONIBLE = ":( El sistema no se encuentra disponible";

    public final static String MENSAJE_ERROR_NOMBRE = "El nombre debe contener al menos 3 letras.";
    public final static String MENSAJE_ERROR_APELLIDO = "El apellido debe contener al menos 3 letras.";
    public final static String MENSAJE_ERROR_EMAIL = "Ingrese un mail valido.";
    public final static String MENSAJE_ERROR_CEDULA = "Ingrese una cédula de indentidad válida.";
    public final static String MENSAJE_ERROR_PASSWORD = "El password debe contener entre 4 y 10 caracteres alfanuméricos.";

    public final static String SERVER_ADDRESS = "http://serviciosgidesarrollo.trabajo.gob.ec:8080/ciudadano/movil/indexPaged.xhtml";
    public final static String REST_URL = "http://serviciosgidesarrollo.trabajo.gob.ec:8080/";
    public final static String REST_BASE = "/ciudadano/rest";

    public final static String SERVER_URL = "serviciosgidesarrollo.trabajo.gob.ec";
    public final static int TIMEOUT = 10000;
    public final static int SERVER_PORT = 8080;

    /*public final static String SERVER_URL = "www.google.com";
    public final static int TIMEOUT = 60000;
    public final static int SERVER_PORT = 80;*/

}