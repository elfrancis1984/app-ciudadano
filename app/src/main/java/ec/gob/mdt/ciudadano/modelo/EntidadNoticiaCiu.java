package ec.gob.mdt.ciudadano.modelo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by francisco on 06/09/16.
 */
public class EntidadNoticiaCiu {

    @SerializedName("type")
    public String tipo;

    @SerializedName("estado")
    public boolean notEstado;

    @SerializedName("fechaActualizacion")
    public String notFechaActualizacion;

    @SerializedName("fechaCreacion")
    public String notFechaCreacion;

    @SerializedName("id")
    public Integer notId;

    @SerializedName("seleccionado")
    public boolean notSeleccionado;

    @SerializedName("usuarioCreacion")
    public String notUsuarioCreacion;

    @SerializedName("titulo")
    public String notTitulo;

    @SerializedName("cuerpo")
    public String notCuerpo;

    @SerializedName("imagen")
    public String notImagen;

    public Integer getNotId() {
        return notId;
    }

    public void setNotId(Integer notId) {
        this.notId = notId;
    }

    public String getNotTitulo() {
        return notTitulo;
    }

    public void setNotTitulo(String notTitulo) {
        this.notTitulo = notTitulo;
    }

    public String getNotCuerpo() {
        return notCuerpo;
    }

    public void setNotCuerpo(String notCuerpo) {
        this.notCuerpo = notCuerpo;
    }

    public String getNotImagen() {
        return notImagen;
    }

    public void setNotImagen(String notImagen) {
        this.notImagen = notImagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isNotEstado() {
        return notEstado;
    }

    public void setNotEstado(boolean notEstado) {
        this.notEstado = notEstado;
    }

    public String getNotFechaActualizacion() {
        return notFechaActualizacion;
    }

    public void setNotFechaActualizacion(String notFechaActualizacion) {
        this.notFechaActualizacion = notFechaActualizacion;
    }

    public String getNotFechaCreacion() {
        return notFechaCreacion;
    }

    public void setNotFechaCreacion(String notFechaCreacion) {
        this.notFechaCreacion = notFechaCreacion;
    }

    public boolean isNotSeleccionado() {
        return notSeleccionado;
    }

    public void setNotSeleccionado(boolean notSeleccionado) {
        this.notSeleccionado = notSeleccionado;
    }

    public String getNotUsuarioCreacion() {
        return notUsuarioCreacion;
    }

    public void setNotUsuarioCreacion(String notUsuarioCreacion) {
        this.notUsuarioCreacion = notUsuarioCreacion;
    }

    public EntidadNoticiaCiu(){

    }

    public EntidadNoticiaCiu(String tipo,
                             boolean notEstado,
                             String notFechaActualizacion,
                             String notFechaCreacion,
                             Integer notId,
                             boolean notSeleccionado,
                             String notUsuarioCreacion,
                             String notTitulo,
                             String notCuerpo,
                             String notImagen) {
        this.tipo = tipo;
        this.notEstado = notEstado;
        this.notFechaActualizacion = notFechaActualizacion;
        this.notFechaCreacion = notFechaCreacion;
        this.notId = notId;
        this.notSeleccionado = notSeleccionado;
        this.notUsuarioCreacion = notUsuarioCreacion;
        this.notTitulo = notTitulo;
        this.notCuerpo = notCuerpo;
        this.notImagen = notImagen;
    }

    public EntidadNoticiaCiu(Integer notId,String notTitulo,String notCuerpo,String notFechaActualizacion,String notImagen) {
        this.notId = notId;
        this.notTitulo = notTitulo;
        this.notCuerpo = notCuerpo;
        this.notFechaActualizacion = notFechaActualizacion;
        this.notImagen = notImagen;
    }
}
