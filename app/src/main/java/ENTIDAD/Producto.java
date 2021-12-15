package ENTIDAD;

public class Producto {
    String Codigo= "",Descripcion ="",Imagen="", tipo, tipoproducto ="";
    int Cantidad = 0, conversion, cantidadcaja, cajastarima;
    public Producto(String Codigo,String Descripcion,int cantidad, String tipo, int conversion, int cantidadcaja, int cajastarima, String tipoproducto){
        this.Codigo = Codigo;
        this.Descripcion = Descripcion;
        this.Cantidad = cantidad;
        this.tipo = tipo;
        this.conversion = conversion;
        this.cantidadcaja = cantidadcaja;
        this.cajastarima = cajastarima;
        this.tipoproducto = tipoproducto;
    }
    public void setImagen(String imagen){this.Imagen = imagen;}
    public String getImagen(){return this.Imagen;}
    public String getCodigo(){
        return this.Codigo;
    }
    public String getDescripcion(){
        return  this.Descripcion;
    }

    public int getCantida(){
        return this.Cantidad;
    }

    public String getTipo(){return this.tipo;}
    public int getConversion(){return this.conversion;}

    public int getCantidadcajaP() {
        return this.cantidadcaja;
    }

    public int getCajastarima() {
        return cajastarima;
    }

    public String getTipoproducto(){return tipoproducto;}
}
