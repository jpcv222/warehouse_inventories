package ENTIDAD;


/**
 * Created by IT on 18/07/2019.
 */

public class Login {

     private String usuario="";
     private String contrasena="";
     private String idcedis="";
     private String nombre="";
     private String imei="";
     private String idsecurity = "";
     private String tipo;
    public Login(String usuario, String contrasena, String idcedis, String nombre, String imei, String idsecurity, String tipo){
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.idcedis = idcedis;
        this.nombre = nombre;
        this.imei = imei;
        this.idsecurity = idsecurity;
        this.tipo = tipo;
    }


    public String getIdsecurity() {
        return idsecurity;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getIdcedis(){
        return idcedis;
    }

    public String getNombre(){return nombre;}
    public String getImei(){return imei;}

    public String getTipo() {
        return tipo;
    }
}
