package ec.gob.mdt.ciudadano.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.dao.DaoUsuario;
import ec.gob.mdt.ciudadano.modelo.RestEntityUsuario;
import ec.gob.mdt.ciudadano.service.RegistroUsuarioService;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.RestUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private Intent act;
    private SharedPreferences sharedPreferences;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_surname) EditText _surnameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    @InjectView(R.id.input_cedula) TextView _cedulaText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        SignupActivity.this.setTitle(R.string.activitySignupName);
        sharedPreferences = getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, MODE_PRIVATE);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_loginActivity();
            }
        });
    }

    public void view_loginActivity() {
        this.act = new Intent(this, LoginActivity.class);
        startActivity(act);
        finish();
    }

    public void view_pinActivity() {
        this.act = new Intent(this, PinActivity.class);
        startActivity(act);
        finish();
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String usuario = _cedulaText.getText().toString();
        String password = _passwordText.getText().toString();

        //----------------------------------------------------------------

        RegistroUsuarioService service = RestUtils.connectRest(Properties.REST_URL, Properties.CONTENT_TYPE_JSON).create(RegistroUsuarioService.class);

        RestEntityUsuario usuarioRest = new RestEntityUsuario();
        usuarioRest.setNombre(name);
        usuarioRest.setApellidos(surname);
        usuarioRest.setCorreo(email);
        usuarioRest.setIdentificacion(usuario);
        usuarioRest.setContrasenna(password);


        Call<ResponseBody> call = service.registrarUsuario(usuarioRest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.raw().code() == 201){
                    try {
                        String codigo = response.body().string();
                        Toast.makeText(getApplicationContext(), codigo,Toast.LENGTH_SHORT).show();
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, "true");
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_USER, _cedulaText.getText().toString());
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, _nameText.getText().toString());
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, _surnameText.getText().toString());
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, _emailText.getText().toString());
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_PASS, _passwordText.getText().toString());
                        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_PIN, codigo);
                        onSignupSuccess();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        Toast.makeText(SignupActivity.this, response.errorBody().string(),Toast.LENGTH_LONG).show();
                        _signupButton.setEnabled(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignupActivity.this, Properties.MENSAJE_ERROR_REST_SIGNUP,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                _signupButton.setEnabled(true);
            }
        });

        // TODO: Implement your own signup logic here.

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    public void onSignupSuccess() {
        guardarUsuarioLocal();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        view_pinActivity();
    }

    private void guardarUsuarioLocal(){
        RestEntityUsuario temp = new RestEntityUsuario();
        temp.setIdentificacion(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER,""));
        temp.setNombre(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES,""));
        temp.setApellidos(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS,""));
        temp.setCorreo(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL,""));
        temp.setContrasenna(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_PASS,""));
        DaoUsuario.guardarUsuario(temp);
    }

    private void saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.commit();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String cedula = _cedulaText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(Properties.MENSAJE_ERROR_NOMBRE);
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (surname.isEmpty() || surname.length() < 3) {
            _surnameText.setError(Properties.MENSAJE_ERROR_APELLIDO);
            valid = false;
        } else {
            _surnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(Properties.MENSAJE_ERROR_EMAIL);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (cedula.isEmpty() || cedula.length() < 10 || cedula.length() > 10) {
            _cedulaText.setError(Properties.MENSAJE_ERROR_CEDULA);
            valid = false;
        } else {
            _cedulaText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(Properties.MENSAJE_ERROR_PASSWORD);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
