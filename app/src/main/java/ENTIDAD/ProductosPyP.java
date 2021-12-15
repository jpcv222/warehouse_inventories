package ENTIDAD;

public class ProductosPyP {

    private String nombreCompleto;
    private int tipo;
    private int conversion;
    private String codigo_sap;
    private int uenvio;
    private int cajastarima;
    private int cantidadvalidada;
    private boolean isChecked = false;
    private int idtraspaso;
    private String almacenorigen;
    private String almacendestino;
    private String idpedidosv;
    private int idtraspasodetalle;

    public ProductosPyP(String nombreCompleto, int tipo, int conversion, String codigo_sap, int uenvio, int cajastarima, int cantidadvalidada, boolean isChecked, int idtraspaso, String almacenorigen, String almacendestino, String idpedidosv, int idtraspasodetalle){
        this.nombreCompleto = nombreCompleto;
        this.tipo = tipo;
        this.conversion = conversion;
        this.codigo_sap = codigo_sap;
        this.uenvio = uenvio;
        this.cajastarima = cajastarima;
        this.cantidadvalidada = cantidadvalidada;
        this.isChecked = isChecked;
        this.idtraspaso = idtraspaso;
        this.almacendestino = almacendestino;
        this.almacenorigen = almacenorigen;
        this.idpedidosv = idpedidosv;
        this.idtraspasodetalle = idtraspasodetalle;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getCantidadvalidada() {
        return cantidadvalidada;
    }

    public int getTipo() {
        return tipo;
    }

    public int getConversion() {
        return conversion;
    }

    public String getCodigo_sap() {
        return codigo_sap;
    }

    public int getUenvio() {
        return uenvio;
    }

    public int getCajastarima() {
        return cajastarima;
    }

    public int getIdtraspaso() {
        return idtraspaso;
    }

    public String getAlmacendestino() {
        return almacendestino;
    }

    public String getAlmacenorigen() {
        return almacenorigen;
    }


    public String getIdpedidosv() {
        return idpedidosv;
    }

    public int getIdtraspasodetalle() {
        return idtraspasodetalle;
    }
}
