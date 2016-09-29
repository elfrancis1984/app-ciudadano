package ec.gob.mdt.ciudadano.service;

import ec.gob.mdt.ciudadano.dto.ImagenDto;
import ec.gob.mdt.ciudadano.modelo.ListEntidadNoticiaCiu;
import ec.gob.mdt.ciudadano.util.Properties;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by francisco on 13/09/16.
 */
public interface NoticiaService {
    @GET(Properties.REST_BASE + "/noticias/ultimas")
    Call<ListEntidadNoticiaCiu> getNoticias();

    @GET("/imagen")
    Call<ImagenDto> getImagen();
}
