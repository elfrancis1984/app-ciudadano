package ec.gob.mdt.ciudadano.dto;

/**
 * Created by francisco on 07/09/16.
 */
public class NoticiaDto {
    private String titulo;
    private String cuerpo;
    private String imagen;

    public NoticiaDto(String titulo, String cuerpo, String imagen) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.imagen = imagen;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCuerpo() {return cuerpo;}

    public void setCuerpo(String cuerpo) {this.cuerpo = cuerpo;}

}
