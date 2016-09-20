package ec.gob.mdt.ciudadano.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.service.NewsService;
import ec.gob.mdt.ciudadano.util.Properties;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passEditText;
    private Intent act;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
    }

    public void checkLogin(View arg0) {

        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            //Set error message for email field
            emailEditText.setError("Email invalido");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            passEditText.setError("La contraseña no puede estar vacia");
        }

        if(isValidEmail(email) && isValidPassword(pass))
        {
            // Validation Completed
            sharedPreferences = getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, MODE_PRIVATE);
            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, "true"); //TODO Eliminar la linea es solo para simular
            view_mainActivity();
            startService(new Intent(this, NewsService.class));
        }

    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

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
