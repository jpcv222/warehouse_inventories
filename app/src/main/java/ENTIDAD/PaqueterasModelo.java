package ENTIDAD;



public class PaqueterasModelo {
    private int idpaquetera;
    private String paquetera;


    public PaqueterasModelo(int idpaquetera, String paquetera) {
        this.idpaquetera = idpaquetera;
        this.paquetera = paquetera;
    }

    public int getIdpaquetera() {
        return idpaquetera;
    }

    public String getPaquetera() {
        return paquetera;
    }


    @Override
    public String toString() {
        return paquetera;
    }
}
