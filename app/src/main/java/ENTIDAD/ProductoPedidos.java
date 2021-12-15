package ENTIDAD;

public class ProductoPedidos {
    private String producto;
    private int cantidad;
    private String transito_sap;
    private String codigo_sap;
    private int conversion;
    public ProductoPedidos(String producto, int cantidad, String transito_sap, String codigo_sap, int conversion){
        this.producto = producto;
        this.cantidad = cantidad;
        this.transito_sap = transito_sap;
        this.codigo_sap = codigo_sap;
        this.conversion = conversion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getProducto() {
        return producto;
    }

    public String getTransito_sap() {
        return transito_sap;
    }

    public String getCodigo_sap() {
        return codigo_sap;
    }

    public int getConversion() {
        return conversion;
    }
}
