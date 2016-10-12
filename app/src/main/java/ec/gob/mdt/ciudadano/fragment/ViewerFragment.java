package ec.gob.mdt.ciudadano.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.util.Properties;
import ec.gob.mdt.ciudadano.util.ToastUtil;
import ec.gob.mdt.ciudadano.util.WebUtils;
import ec.gob.mdt.ciudadano.webInterface.WebAppInterface;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ViewerFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private WebView webview;
    private ProgressDialog progressBar;
    private Activity actividad;
    private String token;

    private SharedPreferences sharedPreferences;

    public ViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewer, container, false);

        actividad = getActivity();
        actividad.setTitle(R.string.fragmentDatosPersonalesName);
        sharedPreferences = actividad.getSharedPreferences(Properties.SHARED_PREFERENCES_USER_DATA, getContext().getApplicationContext().MODE_PRIVATE);
        token = sharedPreferences.getString(Properties.SHARED_PREFERENCES_USER_DATA_TOKEN, "");

        this.webview = (WebView) view.findViewById(R.id.webView);


        progressBar = ProgressDialog.show(getContext(), "Por favor espere", "Cargando...",true);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        AppCompatActivity temp = (AppCompatActivity) view.getContext();
        FragmentTransaction fragmentTransactionTemp = temp.getSupportFragmentManager().beginTransaction();
        webview.addJavascriptInterface(new WebAppInterface(getContext(), fragmentTransactionTemp), "Android");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /*@Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar = ProgressDialog.show(getContext(), "Por favor espere", "Cargando...",true);
            }*/

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            /*@Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                // TODO Auto-generated method stub
                super.doUpdateVisitedHistory(view, url, isReload);
            }*/

            /*@Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }*/

         /*   @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse){
                Toast.makeText(view.getContext(),Properties.MENSAJE_ERROR_SISTEMA_NO_DISPONIBLE,Toast.LENGTH_SHORT).show();
            }*/
        });
        if(WebUtils.pingServer()) {
            webview.loadUrl(Properties.SERVER_ADDRESS+"?token="+token);
        }else{
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            MainFragment fragment = new MainFragment();
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
            ToastUtil.showCustomToast(view.getContext(),Properties.MENSAJE_ERROR_SISTEMA_NO_DISPONIBLE);
            //Toast.makeText(view.getContext(),Properties.MENSAJE_ERROR_SISTEMA_NO_DISPONIBLE,Toast.LENGTH_LONG).show();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}