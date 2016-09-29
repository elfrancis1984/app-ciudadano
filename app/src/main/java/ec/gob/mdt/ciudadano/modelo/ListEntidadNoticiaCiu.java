package ec.gob.mdt.ciudadano.modelo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by francisco on 13/09/16.
 */
public class ListEntidadNoticiaCiu {
    @SerializedName("noticias")
    List<EntidadNoticiaCiu> noticias;

    public List<EntidadNoticiaCiu> getNoticias() {
        return noticias;
    }

    public void setNoticias(List<EntidadNoticiaCiu> noticias) {
        this.noticias = noticias;
    }
}
