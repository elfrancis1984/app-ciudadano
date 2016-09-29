package ec.gob.mdt.ciudadano.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.service.LoginService;
import ec.gob.mdt.ciudadano.service.NewsService;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.RestUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private EditText cedulaEditText;
    private EditText passEditText;
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
    }

    public void checkLogin(View arg0) {

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
            progressDialog.setMessage("Iniciando sesion...");
            progressDialog.show();
            LoginService service = RestUtils.connectRest(Properties.REST_URL, Properties.CONTENT_TYPE_TEXT).create(LoginService.class);

            Call<ResponseBody> call = service.login(cedula,pass);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.raw().code() == 200){
                        try {
                            String token = response.body().string();
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, "true");
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_TOKEN, token);
                            view_mainActivity();

                            /*saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_USER, _cedulaText.getText().toString());
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, _nameText.getText().toString());
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, _surnameText.getText().toString());
                            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, _emailText.getText().toString());*/
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            Toast.makeText(getApplicationContext(), response.errorBody().string(),Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), Properties.MENSAJE_ERROR_REST_LOGIN,Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
            startService(new Intent(this, NewsService.class)); //TODO inicia service noticias
        }

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
