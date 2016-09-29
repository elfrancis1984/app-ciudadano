package ec.gob.mdt.ciudadano.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.modelo.RestEntityUsuario;
import ec.gob.mdt.ciudadano.service.NewsService;
import ec.gob.mdt.ciudadano.service.RegistroUsuarioService;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.RestUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PinActivity extends AppCompatActivity {

    private EditText pinText;
    private Button buttonEnviar;
    private Intent act;
    private SharedPreferences sharedPreferences;
    private String pinGenerado;
    private String identificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        sharedPreferences = getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, MODE_PRIVATE);
        pinGenerado = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_PIN, "");
        identificacion = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER, "");

        pinText = (EditText) findViewById(R.id.pinText);
        buttonEnviar = (Button) findViewById(R.id.button_enviar);
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaPin();
            }
        });
    }

    public void verificaPin(){
        String pin = pinText.getText().toString();
        if (pin.isEmpty()) {
            pinText.setError("El pin no puede estar vacio");
        }else if(!pin.equalsIgnoreCase(pinGenerado)){
            pinText.setError("El pin ingresado no es correcto");
        }else {
            pinText.setError(null);
            //-------------------------------------------------------------------------------
            final ProgressDialog progressDialog = new ProgressDialog(PinActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Activando usuario...");
            progressDialog.show();

            RegistroUsuarioService service = RestUtils.connectRest(Properties.REST_URL,Properties.CONTENT_TYPE_TEXT).create(RegistroUsuarioService.class);

            Call<ResponseBody> call = service.activarUsuario(identificacion,pinGenerado);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if(response.raw().code() == 200){
                        try {
                            String token = response.body().string();
                            init(token);
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
                    Log.e("Error: ",t.getMessage());
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void init(String token){
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_PIN, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_TOKEN, token);
        view_mainActivity();
        startService(new Intent(this, NewsService.class));
    }

    public void view_mainActivity() {
        this.act = new Intent(this, MainActivity.class);
        startActivity(act);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.commit();
    }
}
