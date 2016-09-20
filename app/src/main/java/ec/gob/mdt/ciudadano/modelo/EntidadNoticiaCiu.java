package ec.gob.mdt.ciudadano.modelo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 06/09/16.
 */
public class EntidadNoticiaCiu {
    @SerializedName("id")
    public Integer ciuId;
    @SerializedName("titulo")
    public String ciuTitulo;
    @SerializedName("cuerpo")
    public String ciuCuerpo;
    @SerializedName("imagen")
    public String ciuImagen;

    public Integer getCiuId() {
        return ciuId;
    }

    public void setCiuId(Integer ciuId) {
        this.ciuId = ciuId;
    }

    public String getCiuTitulo() {
        return ciuTitulo;
    }

    public void setCiuTitulo(String ciuTitulo) {
        this.ciuTitulo = ciuTitulo;
    }

    public String getCiuCuerpo() {
        return ciuCuerpo;
    }

    public void setCiuCuerpo(String ciuCuerpo) {
        this.ciuCuerpo = ciuCuerpo;
    }

    public String getCiuImagen() {
        return ciuImagen;
    }

    public void setCiuImagen(String ciuImagen) {
        this.ciuImagen = ciuImagen;
    }



    public EntidadNoticiaCiu(){

    }

    public EntidadNoticiaCiu(Integer ciuId, String ciuTitulo, String ciuCuerpo, String ciuImagen) {
        this.ciuId = ciuId;
        this.ciuTitulo = ciuTitulo;
        this.ciuCuerpo = ciuCuerpo;
        this.ciuImagen = ciuImagen;
    }

}
