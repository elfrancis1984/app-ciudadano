package ec.gob.mdt.ciudadano.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.service.NewsService;
import ec.gob.mdt.ciudadano.util.Properties;

public class PinActivity extends AppCompatActivity {

    private EditText pinText;
    private Button buttonEnviar;
    private Intent act;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

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
        } else {
            pinText.setError(null);
            //--------------------
            sharedPreferences = getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, MODE_PRIVATE);
            saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, "true"); //TODO Eliminar la linea es solo para simular
            view_mainActivity();
            startService(new Intent(this, NewsService.class));
        }
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
