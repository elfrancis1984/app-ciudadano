package ec.gob.mdt.ciudadano.service;

import ec.gob.mdt.ciudadano.modelo.RestEntityUsuario;
import ec.gob.mdt.ciudadano.util.Properties;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by francisco chalan on 27/09/16.
 */
public interface RegistroUsuarioService {
    @POST(Properties.REST_BASE + "/usuarios/registro")
    Call<ResponseBody> registrarUsuario(@Body RestEntityUsuario usuario);

    @POST(Properties.REST_BASE + "/usuarios/activar/{identificacion}/{codigo}")
    Call<ResponseBody> activarUsuario(@Path("identificacion") String identificacion, @Path("codigo") String codigo);
}
