package ec.gob.mdt.ciudadano.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.dao.DaoUsuario;
import ec.gob.mdt.ciudadano.modelo.RestEntityUsuario;
import ec.gob.mdt.ciudadano.util.PhotoUtils;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PerfilFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Activity act;
    private View view;
    private SharedPreferences sharedPreferences;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_PICTURE = 2;
    public static final int REQUEST_CAMERA = 1;

    String mCurrentPhotoPath;

    private CircleImageView perfilImage;
    private ImageView girarImage;
    private EditText nombresEditText;
    private EditText apellidosEditText;
    private EditText emailEditText;
    private EditText cedulaEditText;
    private EditText contrasennaEditText;
    private Button botonActualizar;
    NavigationView navigationView = null;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
//        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.TextLabel);
        // clone the inflater using the ContextThemeWrapper
//        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        // inflate the layout using the cloned inflater, not default inflater
//        View view = localInflater.inflate(R.layout.fragment_perfil, container, false);
        //getContext().getTheme().applyStyle(R.style.TextLabel, true);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        act = getActivity();
        act.setTitle(R.string.fragmentPerfilName);
        sharedPreferences = act.getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, getContext().getApplicationContext().MODE_PRIVATE);

        girarImage = (ImageView) view.findViewById(R.id.img_girar);
        perfilImage = (CircleImageView) view.findViewById(R.id.img_perfil);
        nombresEditText = (EditText) view.findViewById(R.id.input_name_p);
        apellidosEditText = (EditText) view.findViewById(R.id.input_surname_p);
        emailEditText = (EditText) view.findViewById(R.id.input_email_p);
        cedulaEditText = (EditText) view.findViewById(R.id.input_cedula_p);
        contrasennaEditText = (EditText) view.findViewById(R.id.input_password_p);
        botonActualizar = (Button) view.findViewById(R.id.btn_update);

        nombresEditText.setText(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_NOMBRES, ""));
        apellidosEditText.setText(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_APELLIDOS, ""));
        emailEditText.setText(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_EMAIL, ""));
        cedulaEditText.setText(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER, ""));
        contrasennaEditText.setText(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_PASS, ""));

        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MainActivity.REQUEST_CAMERA);

        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPerfil();
            }
        });
        girarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)perfilImage.getDrawable()).getBitmap();
                Bitmap bitmapTemp = PhotoUtils.rotate(bitmap,90);
                PhotoUtils.guardarImagen(act,sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER, ""),bitmapTemp);
                perfilImage.setImageBitmap(bitmapTemp);
                cambiarAvatarMenu(bitmapTemp);
            }
        });

        registerForContextMenu(perfilImage);

        Bitmap imageBitmapPerfil = PhotoUtils.retrieveImagenByPath(sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER, ""),act);

        if(imageBitmapPerfil != null)
            perfilImage.setImageBitmap(imageBitmapPerfil);
        else
            perfilImage.setImageResource(R.drawable.perfil_avatar); //ImageBitmap(imageBitmapPerfil);

        return view;
    }

    private void tomarFotoPerfil(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(act.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static void camara(Activity activity){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void seleccionarFotoPerfil(){

        Intent takePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        if (takePictureIntent.resolveActivity(act.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent,SELECT_PICTURE);
        }
    }

    private void actualizarPerfil(){
        RestEntityUsuario temp = new RestEntityUsuario();
        temp.setNombre(nombresEditText.getText().toString().trim());
        temp.setApellidos(apellidosEditText.getText().toString().trim());
        temp.setCorreo(emailEditText.getText().toString().trim());
        temp.setIdentificacion(cedulaEditText.getText().toString().trim());
        temp.setContrasenna(contrasennaEditText.getText().toString().trim());

        if(validate(temp)) {
            DaoUsuario.actualizarUsuario(temp);
            ToastUtil.showCustomToast(view.getContext(), Properties.MENSAJE_ACTUALIZACION_EXITOSA);
        }
    }


    /*private void view_MainActivity(){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }*/

    public boolean validate(RestEntityUsuario usuario) {
        boolean valid = true;

        if (usuario.getNombre().isEmpty() || usuario.getNombre().length() < 3) {
            nombresEditText.setError(Properties.MENSAJE_ERROR_NOMBRE);
            valid = false;
        } else {
            nombresEditText.setError(null);
        }

        if (usuario.getApellidos().isEmpty() || usuario.getApellidos().length() < 3) {
            apellidosEditText.setError(Properties.MENSAJE_ERROR_APELLIDO);
            valid = false;
        } else {
            apellidosEditText.setError(null);
        }

        if (usuario.getCorreo().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usuario.getCorreo()).matches()) {
            emailEditText.setError(Properties.MENSAJE_ERROR_EMAIL);
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (usuario.getIdentificacion().isEmpty() || usuario.getIdentificacion().length() < 10 || usuario.getIdentificacion().length() > 10) {
            cedulaEditText.setError(Properties.MENSAJE_ERROR_CEDULA);
            valid = false;
        } else {
            cedulaEditText.setError(null);
        }

        if (usuario.getContrasenna().isEmpty() || usuario.getContrasenna().length() < 4 || usuario.getContrasenna().length() > 10) {
            contrasennaEditText.setError(Properties.MENSAJE_ERROR_PASSWORD);
            valid = false;
        } else {
            contrasennaEditText.setError(null);
        }

        return valid;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == act.RESULT_OK)
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) data.getParcelableExtra("data");
                PhotoUtils.guardarImagen(act,sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER, ""),imageBitmap);
                perfilImage.setImageBitmap(imageBitmap);
                cambiarAvatarMenu(imageBitmap);
                girarImage.setVisibility(View.VISIBLE);
            } else if (requestCode == SELECT_PICTURE){
                Uri selectedImage = data.getData();
                InputStream is;
                try {
                    is = act.getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);
                    PhotoUtils.guardarImagen(act,sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_USER, ""),bitmap);
                    perfilImage.setImageBitmap(bitmap);
                    cambiarAvatarMenu(bitmap);
                    girarImage.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {}
            }
    }

    private void cambiarAvatarMenu(Bitmap imagen){
        navigationView = (NavigationView) act.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView perfilAvatar = (CircleImageView) headerView.findViewById(R.id.profile_image);
        perfilAvatar.setImageBitmap(imagen);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Establecer una foto de perfil");
        menu.add(0, v.getId(), 0, "Nueva foto de perfil");
        menu.add(0, v.getId(), 0, "Importar desde la galería");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Nueva foto de perfil") {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
            } else {
                // permission has been granted, continue as usual
                tomarFotoPerfil();
            }
        } else if (item.getTitle() == "Importar desde la galería") {
            seleccionarFotoPerfil();
        } else {
            return false;
        }
        return true;
    }

}
