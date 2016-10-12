package ec.gob.mdt.ciudadano.webInterface;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.fragment.MainFragment;

/**
 * Created by francisco on 16/09/16.
 */
public class WebAppInterface {

    Context mContext;
    FragmentTransaction fragmentTransaction;

    /** Instantiate the interface and set the context, transaction */
    public WebAppInterface(Context c, FragmentTransaction f) {
        mContext = c;
        fragmentTransaction = f;
    }

    /** Interacction from the web page */
    @JavascriptInterface
    public void interacction(String toast) {
        MainFragment fragment = new MainFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragment,"frag_mainFragment");
        fragmentTransaction.addToBackStack("frag_mainFragment");
        fragmentTransaction.commit();
    }

}
