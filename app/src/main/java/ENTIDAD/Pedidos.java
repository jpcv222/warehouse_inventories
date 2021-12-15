package ENTIDAD;

public class Pedidos {
    private int idpedidofinal;
    private String canal;
    private int idpedido;
    private String ncliente;
    private String paquetera;

    public Pedidos(int idpedidofinal, String canal, int idpedido, String ncliente, String paquetera){
        this.idpedidofinal = idpedidofinal;
        this.canal = canal;
        this.idpedido = idpedido;
        this.ncliente = ncliente;
        this.paquetera = paquetera;
    }

    public String getNcliente() {
        return ncliente;
    }

    public int getIdpedidofinal() {
        return idpedidofinal;
    }

    public String getCanal() {
        return canal;
    }

    public int getIdpedido() {
        return idpedido;
    }

    public String getPaquetera() {
        return paquetera;
    }
}
