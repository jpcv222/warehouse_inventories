package ENTIDAD;

public class InventarioModelo {
    private String producto;
    private int tipo;
    private int conversion;
    private String codigo_sap;
    private int uenvio;
    private int cantidadconteo;
    private int idproducto;
    private int idsubproducto;
    public InventarioModelo(String producto, int tipo, int conversion, String codigo_sap, int uenvio, int idproducto, int idsubproducto){
        this.producto = producto;
        this.tipo = tipo;
        this.conversion = conversion;
        this.codigo_sap = codigo_sap;
        this.uenvio = uenvio;
        this.idsubproducto = idsubproducto;
        this.idproducto = idproducto;
    }

    public String getProducto() {
        return producto;
    }

    public int getConversion() {
        return conversion;
    }

    public String getCodigo_sap() {
        return codigo_sap;
    }

    public int getTipo() {
        return tipo;
    }

    public int getUenvio() {
        return uenvio;
    }

    public int getCantidadconteo() {
        return cantidadconteo;
    }

    public void setCantidadconteo(int cantidadconteo) {
        this.cantidadconteo = cantidadconteo;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public int getIdsubproducto() {
        return idsubproducto;
    }
}
