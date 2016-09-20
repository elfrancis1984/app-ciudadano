package ec.gob.mdt.ciudadano.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ec.gob.mdt.ciudadano.R;
import ec.gob.mdt.ciudadano.dto.NoticiaDto;
import ec.gob.mdt.ciudadano.fragment.DetalleNoticiasFragment;
import ec.gob.mdt.ciudadano.util.PhotoUtils;


/**
 * Created by francisco on 07/09/16.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder>{
    private List<NoticiaDto> items;
    private Context ctx;

    public NoticiaAdapter(List<NoticiaDto> items, Context ctx) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public NoticiaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_noticia, parent, false);
        return new NoticiaViewHolder(v,ctx,items);
    }

    @Override
    public void onBindViewHolder(NoticiaViewHolder holder, int position) {
        //holder.imagen.setImageBitmap(PhotoUtils.decodeSampledBitmapFromResource(items.get(position).getImagen(),150,150,this.ctx));
        holder.imagen.setImageBitmap(PhotoUtils.retrieveImagenByPathScaled(items.get(position).getImagen(),this.ctx,true,180,190));
        holder.nombre.setText(Html.fromHtml(items.get(position).getTitulo()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Campos respectivos de un item (noticia)
        public ImageView imagen;
        public TextView nombre;
        public List<NoticiaDto> lista = new ArrayList<NoticiaDto>();
        public Context ctx;

        public NoticiaViewHolder(View v, Context ctx, List<NoticiaDto> lista) {
            super(v);
            this.lista = lista;
            this.ctx = ctx;
            v.setOnClickListener(this);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.titulo);

        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            DetalleNoticiasFragment fragment = new DetalleNoticiasFragment();
            Bundle args = new Bundle();
            args.putString("titulo", this.lista.get(index).getTitulo());
            args.putString("cuerpo", this.lista.get(index).getCuerpo());
            args.putString("imagen", this.lista.get(index).getImagen());

            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.left_out);
            fragmentTransaction.replace(R.id.fragment_container, fragment,"frag_detalleNoticias").addToBackStack("frag_detalleNoticias");
            fragmentTransaction.commit();
        }
    }
}
