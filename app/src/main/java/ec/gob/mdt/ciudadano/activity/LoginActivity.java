package ec.gob.mdt.ciudadano.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.dao.DaoUsuario;
import ec.gob.mdt.ciudadano.modelo.RestEntityUsuario;
import ec.gob.mdt.ciudadano.service.LoginService;
import ec.gob.mdt.ciudadano.service.NewsService;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.RestUtils;
import ec.gob.mdt.ciudadano.util.ToastUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private EditText cedulaEditText;
    private EditText passEditText;
    private Button btnLogin;
    private Intent act;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginActivity.this.setTitle(R.string.activityLoginName);

        sharedPreferences = getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, MODE_PRIVATE);

        cedulaEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    public void checkLogin() {

        final String cedula = cedulaEditText.getText().toString();
        if(!isValidUser(cedula)){
            //Set error message for user field
            cedulaEditText.setError("La cédula no puede estar vacia");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            passEditText.setError("La contraseña no puede estar vacia");
        }

        if(isValidUser(cedula) && isValidPassword(pass))
        {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Iniciando sesion...");
            progressDialog.show();

            LoginService service = RestUtils.connectRest(Properties.REST_URL, Properties.CONTENT_TYPE_TEXT).create(LoginService.class);
            Call<ResponseBody> call = service.login(cedula,pass);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.raw().code() == 200){
                        try {
                            String respuesta = response.body().string();
                            JsonParser parser = new JsonParser();
                            JsonObject info = parser.parse(respuesta).getAsJsonObject();

                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, "true");
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_USER, cedula);
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, info.get("nombres").getAsString());
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, info.get("apellidos").getAsString());
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, info.get("correo").getAsString());
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_PASS, pass);
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_TOKEN, info.get("token").getAsString());
                            iniciaServicioNoticias();
                            getInfoDevice();
                            //cargaUsuario(cedula);
                            view_mainActivity();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            ToastUtil.showCustomToast(LoginActivity.this,response.errorBody().string());
                            //Toast.makeText(LoginActivity.this, response.errorBody().string(),Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ToastUtil.showCustomToast(LoginActivity.this,Properties.MENSAJE_ERROR_REST_LOGIN);
                    /*Toast toast = Toast.makeText(LoginActivity.this, Properties.MENSAJE_ERROR_REST_LOGIN,Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();*/
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void getInfoDevice(){
        Log.w("Info Kernel",System.getProperty("os.version")); // OS version
        Log.w("Info",android.os.Build.BOARD);
        Log.w("Info",android.os.Build.BOOTLOADER);
        Log.w("Info Marca",android.os.Build.BRAND);
        Log.w("Info Compilacion",android.os.Build.DISPLAY);
        Log.w("Info",android.os.Build.HARDWARE);
        Log.w("Info",android.os.Build.HOST);
        Log.w("Info",android.os.Build.MANUFACTURER);
        Log.w("Info Android", Build.VERSION.RELEASE);      // API Level
        Log.w("Info",android.os.Build.DEVICE);           // Device
        Log.w("Info Modelo",android.os.Build.MODEL);            // Model
        Log.w("Info",android.os.Build.PRODUCT);
    }

    private void cargaUsuario(String identificacion){
        RestEntityUsuario usuario = DaoUsuario.recuperarUsuario(identificacion);
        if(usuario != null){
            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, usuario.getNombre());
            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, usuario.getApellidos());
            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, usuario.getCorreo());
        }

    }

    private void iniciaServicioNoticias(){
        startService(new Intent(this, NewsService.class)); //TODO inicia service noticias
    }

    private boolean isValidUser(String cedula){
        if(cedula.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    /*// validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }*/

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }

    public void view_mainActivity() {
        this.act = new Intent(this, MainActivity.class);
        startActivity(act);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void view_signUpActivity(View arg0) {
        this.act = new Intent(this, SignupActivity.class);
        startActivity(act);
        finish();
    }

    private void saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.commit();
    }
}
