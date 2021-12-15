package ENTIDAD;

import java.util.Calendar;

public class Total extends Entrada {
    int Total = 0;
    int Cantidadtarima = 0;
    int Cantidadcaja = 0;
    int Cantidadpaquete = 0;
    int Cantidadunidad = 0;
    int unidadenvio = 0;
    int cajastarima = 0;
    String Codigo;
    String tipoproducto = "";

    public Total(String Codigo, String Descripcion, int cantidad, int unidadenvio, String Fecha, int conversion, int cantidadtarima, int cantidadcaja, int cantidadpaquete, int cantidadunidad, int cajastarima, String tipoproducto) {
        super(Codigo, Descripcion, cantidad, Fecha, conversion, cantidadtarima, cantidadcaja, cantidadpaquete,cantidadunidad, cajastarima, tipoproducto, unidadenvio);
        this.Total = cantidad;
        this.Codigo = Codigo;
        this.Cantidadcaja = cantidadcaja;
        this.Cantidadpaquete = cantidadpaquete;
        this.Cantidadunidad = cantidadunidad;
        this.Cantidadtarima = cantidadtarima;
        this.unidadenvio = unidadenvio;
        this.cajastarima = cajastarima;
        this.tipoproducto = tipoproducto;
    }
    public int getTotal(){
        return Total;
    }
    public void setTotal(int total){this.Total=total;}

    public void setCantidadpaquete(int cantidadpaquete){this.Cantidadpaquete = cantidadpaquete;}

    public void setCantidadunidad(int cantidadunidad){this.Cantidadunidad = cantidadunidad;}

    public void setCantidadtarima(int cantidadtarima){this.Cantidadtarima = cantidadtarima;}

    public void setCantidadcaja(int cantidadcaja){this.Cantidadtarima = cantidadcaja;}

    public int getCantidadcaja() {
        return Cantidadcaja;
    }

    public String getCodigo(){
        return Codigo;
    }

    public int getUnidadenvio(){return this.unidadenvio;}

    public int getCantidadtarima() {
        return Cantidadtarima;
    }

    public int getCantidadpaquete() {
        return Cantidadpaquete;
    }

    public int getCantidadunidad() {
        return Cantidadunidad;
    }

    public int getCajastarima(){

        return cajastarima;

    }

    public String getTipoproducto(){return tipoproducto;}
}
