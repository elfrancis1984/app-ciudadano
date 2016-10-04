package ec.gob.mdt.ciudadano.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.dao.BaseApp;
import ec.gob.mdt.ciudadano.dao.DaoNoticia;
import ec.gob.mdt.ciudadano.dto.ImagenDto;
import ec.gob.mdt.ciudadano.fragment.DetalleNoticiasFragment;
import ec.gob.mdt.ciudadano.fragment.MainFragment;
import ec.gob.mdt.ciudadano.fragment.NoticiasFragment;
import ec.gob.mdt.ciudadano.fragment.ViewerFragment;
import ec.gob.mdt.ciudadano.modelo.EntidadNoticiaCiu;
import ec.gob.mdt.ciudadano.modelo.ListEntidadNoticiaCiu;
import ec.gob.mdt.ciudadano.service.NoticiaService;
import ec.gob.mdt.ciudadano.util.Codex;
import ec.gob.mdt.ciudadano.util.NotifyUtil;
import ec.gob.mdt.ciudadano.util.PhotoUtils;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.RestUtils;
import ec.gob.mdt.ciudadano.util.Sesion;
import ec.gob.mdt.ciudadano.util.ToastUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ViewerFragment.OnFragmentInteractionListener,
        NoticiasFragment.OnFragmentInteractionListener,
        DetalleNoticiasFragment.OnFragmentInteractionListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    private Intent act;
    private Boolean registrado;
    private String pin;
    private String nombres;
    private String apellidos;
    private String email;
    private String token;
    private SharedPreferences sharedPreferences;
    private ListEntidadNoticiaCiu listaNoticias;
    private Sesion varibleSesion;

    final String BASE_URL = "https://demo9619878.mockable.io/";

    private ProgressDialog pd;

    Handler handlerDismiss = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pd.dismiss();
        }
    };

    Handler handlerShow = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pd = ProgressDialog.show(MainActivity.this,"Por favor espere", "Cargando noticias",true);
//            pd = new ProgressDialog(MainActivity.this);
//            pd.setTitle("Por favor espere");
//            pd.setMessage("Cargando noticias");
//            pd.setProgressStyle(pd.STYLE_HORIZONTAL);
//            pd.setProgress(0);
//            pd.setMax(10);
//            pd.show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, MODE_PRIVATE);
        varibleSesion = (Sesion) getApplicationContext();

        registrado = Boolean.valueOf(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, ""));
        pin = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_PIN, "");
        nombres = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, "");
        apellidos = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, "");
        email = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, "");
        token = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_TOKEN, "");
        //---------------------
        checkIn();
        //---------------------
        //Set the fragment initially
        //NoticiasFragment fragment = new NoticiasFragment();
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment,"frag_mainFragment");
        fragmentTransaction.addToBackStack("frag_mainFragment");
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, Codex.codificador("francisco",true), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                ToastUtil.showCustomToast(MainActivity.this,Codex.codificador("francisco",true));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //How to change elements in the header programatically
        View headerView = navigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.email);
        TextView userNameText = (TextView) headerView.findViewById(R.id.username);

        userNameText.setText(nombres + " " + apellidos);
        emailText.setText(email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void generaNotificacion(){
        NotifyUtil nU = new NotifyUtil(getApplicationContext());
        nU.generaNotificacion(listaNoticias);
    }

    private void checkIn(){

        Thread process = new Thread() {
            @Override
            public void run() {
                if(!registrado){
                    view_loginActivity();
                }else if(pin.isEmpty()){
                    if(DaoNoticia.numeroFilasNoticia() == 0)
                        consultaNoticias();
                }else{
                    view_pinActivity();
                }
            }
        };
        process.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                if(getSupportFragmentManager().findFragmentByTag("frag_mainFragment") != null && getSupportFragmentManager().findFragmentByTag("frag_mainFragment").isVisible()){
                    drawer.openDrawer(GravityCompat.START);
                }else if(getSupportFragmentManager().findFragmentByTag("frag_datosPersonales") != null && getSupportFragmentManager().findFragmentByTag("frag_datosPersonales").isVisible()){
                    WebView mWebView = (WebView) findViewById(R.id.webView);
                    mWebView.loadUrl("javascript:$('.buttonBackMovil').click();");
                    /*if (mWebView != null && mWebView.canGoBack())
                        mWebView.goBack();
                    else
                        getSupportFragmentManager().popBackStack();*/
                }else
                    getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(pd!= null)
            pd.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        if (id == R.id.nav_datosPersonales) {
            //Set the fragment initially
            ViewerFragment fragment = new ViewerFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment,"frag_datosPersonales").addToBackStack("frag_datosPersonales");
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_noticias) {
            if(DaoNoticia.numeroFilasNoticia() > 0) {
                NoticiasFragment fragment = new NoticiasFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "frag_noticias").addToBackStack("frag_noticias");
                fragmentTransaction.commit();
            }else{
                consultaNoticias();
            }
        }
        else if (id == R.id.nav_session) {
            cerrarSesion();
        } else if (id == R.id.nav_sql) {
            consultaNoticias();
        }
        else if (id == R.id.nav_notify) {
            generaNotificacion();

        }/* else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cerrarSesion(){
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_REGISTRADO, "false");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_TOKEN, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_USER, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_PASS, "");
        saveSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA_PIN, "");
        view_loginActivity();
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
        Toast.makeText(MainActivity.this,"dasdas",Toast.LENGTH_SHORT).show();
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

    private void saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(key, value);
        preferencesEditor.commit();
    }

    public void consultaNoticias(){
        handlerShow.sendEmptyMessage(0);
        NoticiaService service = RestUtils.connectRestAuth(Properties.REST_URL,token).create(NoticiaService.class);
        Call<ListEntidadNoticiaCiu> call = service.getNoticias();
        call.enqueue(new Callback<ListEntidadNoticiaCiu>() {
            @Override
            public void onResponse(Call<ListEntidadNoticiaCiu> call, Response<ListEntidadNoticiaCiu> response) {
                if(response.body() == null){
                    Toast.makeText(getApplicationContext(), Properties.MENSAJE_ERROR_REST_NOTICIAS,Toast.LENGTH_LONG).show();
                    cerrarSesion();
                }else {
                    listaNoticias = response.body();
                    varibleSesion.listaNoticias = listaNoticias;
                    cargaNoticiasBd(listaNoticias.getNoticias());
                }
            }

            @Override
            public void onFailure(Call<ListEntidadNoticiaCiu> call, Throwable t) {
                Log.e("Error: ",t.getMessage());
                Toast.makeText(getApplicationContext(), Properties.MENSAJE_ERROR_REST_NOTICIAS,Toast.LENGTH_LONG).show();
                handlerDismiss.sendEmptyMessage(0);
            }
        });
    }

    private void cargaNoticiasBd(List<EntidadNoticiaCiu> temp){
        BaseApp.getInstance().borrarTablaCiuNoticia();
        DaoNoticia.guardarListaNoticia(temp);
        for(EntidadNoticiaCiu noticia: temp){
            obtenerImagenRest(noticia.getNotImagen());
        }
        handlerDismiss.sendEmptyMessage(0);
    }

    private void obtenerImagenRest(final String codigo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NoticiaService service = retrofit.create(NoticiaService.class);
        Call<ImagenDto> call = service.getImagen();
        call.enqueue(new Callback<ImagenDto>() {
            @Override
            public void onResponse(Call<ImagenDto> call, Response<ImagenDto> response) {
                ImagenDto imagen = response.body();
                byte[] bytearray = Base64.decode(imagen.getImagen(), Base64.DEFAULT);
                Bitmap bmimage = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
                PhotoUtils.guardarImagen(getApplicationContext(), codigo, bmimage);
            }

            @Override
            public void onFailure(Call<ImagenDto> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }
}