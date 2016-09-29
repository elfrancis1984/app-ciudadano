package ec.gob.mdt.ciudadano.service;

import ec.gob.mdt.ciudadano.util.Properties;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by francisco chalan on 28/09/16.
 */
public interface LoginService {
    @POST(Properties.REST_BASE + "/login/{user}/{pass}")
    Call<ResponseBody> login(@Path("user") String usuario, @Path("pass") String clave);
}
