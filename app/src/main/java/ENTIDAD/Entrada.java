package ENTIDAD;

public class Entrada extends Producto {
    String Fecha = "";
    int ID = 0;
    int cantidadtarima;
    int cantidadcaja;
    int cantidadpaquete;
    int cantidadunidad;
    int cajastarima;
    String tipoproducto = "";
    int uenvio = 0;
    public Entrada(String Codigo, String Descripcion, int cantidad, String Fecha, int conversion,int cantidadtarima, int cantidadcaja, int cantidadpaquete, int cantidadunidad, int cajastarima, String tipoproducto, int uenvio) {
        super(Codigo, Descripcion, cantidad, Fecha, conversion, cantidadcaja, cajastarima, tipoproducto);
        this.Fecha = Fecha;
        this.cantidadcaja = cantidadcaja;
        this.cantidadpaquete = cantidadpaquete;
        this.cantidadtarima = cantidadtarima;
        this.cantidadunidad = cantidadunidad;
        this.cajastarima = cajastarima;
        this.tipoproducto = tipoproducto;
        this.uenvio = uenvio;
    }
    public String GetFecha(){
        return this.Fecha;
    }
    public int GetID(){return this.ID;}
    public void setID(int id){this.ID = id;}

    public int getCantidadtarima() {
        return cantidadtarima;
    }
    public void setCantidadtarima(int cantidadtarima) {
        this.cantidadtarima = cantidadtarima;
    }

    public int getCantidadcaja() {
        return cantidadcaja;
    }
    public void setCantidadcaja(int cantidadcaja) {
        this.cantidadcaja = cantidadcaja;
    }

    public int getCantidadpaquete() {
        return cantidadpaquete;
    }
   /* public void setCantidadpaquete(int cantidadpaquete) {
        this.cantidadpaquete = cantidadpaquete;
    }*/

    public int getCantidadunidad() {
        return cantidadunidad;
    }
   /* public void setCantidadunidad(int cantidadunidad) {
        this.cantidadunidad = cantidadunidad;
    }*/

   public int getCajastarima(){
       return cajastarima;
   }

   public String getTipoproducto(){return tipoproducto;}

    public int getUenvio() {
        return uenvio;
    }
}
