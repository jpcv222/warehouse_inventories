package ENTIDAD;

import android.content.Context;
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


public class AdaptadorLvPedidos extends ArrayAdapter<ProductoPedidos> {
    public static ArrayList<ProductoPedidos> listProduct;
    private Context context;

    public AdaptadorLvPedidos(ArrayList<ProductoPedidos> listProduct, Context context) {
        super(context, R.layout.row_pedidofaltante, listProduct);
        this.listProduct = listProduct;
        this.context = context;
    }

    private static class ProductHolder {
        public TextView txtCantidadProducto, txtTituloProducto;
        public ImageView imgProducto;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = null;


        ProductHolder holder = new ProductHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_pedidofaltante, null);


        } else {
            v = convertView;
        }
        ProductoPedidos product = this.listProduct.get(position);

        holder.imgProducto = (ImageView)v.findViewById(R.id.imgProductoPedido);
        holder.txtCantidadProducto = (TextView)v.findViewById(R.id.txtCantidadPedido);
        holder.txtTituloProducto = (TextView)v.findViewById(R.id.txtProductoPedido);

        holder.txtCantidadProducto.setText(""+product.getCantidad()/product.getConversion());
        holder.txtTituloProducto.setText(product.getProducto());

        String a = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/" + product.getProducto() + ".jpg";
        //String a = "https://corpogaranon.fortiddns.com:5827/app/inventarios/imagenes/"+product.getProducto()+".jpg";

        try {

            Glide.with(getContext()).load(a).asBitmap().override(200, 200).error(R.drawable.icon_trusot).fitCenter().into(holder.imgProducto);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();
        }


        return v;
    }


    @Nullable
    @Override
    public ProductoPedidos getItem(int position) {
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