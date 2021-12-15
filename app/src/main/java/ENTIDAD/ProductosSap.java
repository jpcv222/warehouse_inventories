package ENTIDAD;

import android.support.annotation.NonNull;

public class ProductosSap {
    private String producto;
    private int cantidad;

    public ProductosSap(String producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    @NonNull
    @Override
    public String toString() {
        return producto+" "+cantidad;
    }
}
