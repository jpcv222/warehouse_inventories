package ENTIDAD;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.PandP;
import com.example.desarrolloii.inventario.R;

import java.util.ArrayList;


public class AdaptadorPedidos extends ArrayAdapter<Pedidos> {
    public static ArrayList<Pedidos> listProduct;
    private Context context;
    private String estatus;
    public AdaptadorPedidos(ArrayList<Pedidos> listProduct, Context context, String estatus) {
        super(context, R.layout.row_pedidos, listProduct);
        this.listProduct = listProduct;
        this.context = context;
        this.estatus = estatus;
    }

    private static class ProductHolder {
        public TextView txtNoPedido, txtCanal, txtTituloNcliente, txtCliente;
        public ImageView imgEstatus;

    }

    public String getEstatus() {
        return estatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = null;
        ProductHolder holder = new ProductHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_pedidos, null);
        } else {
            v = convertView;
        }
        Pedidos pedido = this.listProduct.get(position);
        holder.txtCanal = (TextView)v.findViewById(R.id.txtCanal);
        holder.txtNoPedido = (TextView)v.findViewById(R.id.txtNoPedido);
        holder.imgEstatus = (ImageView)v.findViewById(R.id.imgEstatus);
        holder.txtTituloNcliente = (TextView)v.findViewById(R.id.txtNCliente);
        holder.txtCliente = (TextView)v.findViewById(R.id.txtCliente);

        holder.txtNoPedido.setText(""+pedido.getIdpedidofinal());
        holder.txtCanal.setText(pedido.getCanal());

        if(pedido.getNcliente().contains("CEDIS")){
            holder.txtTituloNcliente.setText("CEDIS: ");
        }else{
            holder.txtTituloNcliente.setText("CLIENTE: ");
        }

        holder.txtCliente.setText(pedido.getNcliente());

        if(getEstatus().equals("EMPLAYE")){
            holder.imgEstatus.setImageResource(R.drawable.ic_pack);
        }
        if(getEstatus().equals("ENVIADO")){
            holder.imgEstatus.setImageResource(R.drawable.ic_verified);
        }

        return v;
    }



    @Nullable
    @Override
    public Pedidos getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}