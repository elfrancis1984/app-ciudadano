package ec.gob.mdt.ciudadano.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.adapter.NoticiaAdapter;
import ec.gob.mdt.ciudadano.dao.DaoNoticia;
import ec.gob.mdt.ciudadano.dto.NoticiaDto;
import ec.gob.mdt.ciudadano.modelo.EntidadNoticiaCiu;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticiasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NoticiasFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Activity act;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List<NoticiaDto> items = new ArrayList<>();


    public NoticiasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        act = getActivity();
        act.setTitle(R.string.fragmentNoticiasName);

//        List<NoticiaDto> items = new ArrayList<>();

        /*items.add(new NoticiaDto(R.drawable.tanglong, "Noticia 1"));
        items.add(new NoticiaDto(R.drawable.flowercopter, "Noticia 2"));
        items.add(new NoticiaDto(R.drawable.dino, "Noticia 3"));
        items.add(new NoticiaDto(R.drawable.mastermorphix, "Noticia 4"));*/

        if(items.isEmpty())
            for(EntidadNoticiaCiu noticia : DaoNoticia.getEntidadNoticiaCiu()){
                items.add(new NoticiaDto(noticia.ciuTitulo, noticia.ciuCuerpo,noticia.ciuImagen));
                //items.add(new NoticiaDto(noticia.ciuTitulo, noticia.ciuCuerpo,noticia.ciuImagenBlob));
            }

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(act);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new NoticiaAdapter(items, act);
        recycler.setAdapter(adapter);

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
