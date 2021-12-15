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


public class AdaptadorLvProductos extends ArrayAdapter<ProductosPyP> {
    public static ArrayList<ProductosPyP> listProduct;
    private Context context;

    public AdaptadorLvProductos(ArrayList<ProductosPyP> listProduct, Context context) {
        super(context, R.layout.row_productospandp, listProduct);
        this.listProduct = listProduct;
        this.context = context;
    }

    private static class ProductHolder {
        public TextView titulo, txtCantidad;

        public CheckBox checkBox;
        public ImageView imgProducto;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = null;


        ProductHolder holder = new ProductHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_productospandp, null);


        } else {
            v = convertView;
        }
        ProductosPyP product = this.listProduct.get(position);

        String a = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/" + product.getNombreCompleto() + ".jpg";
        //String a = "https://corpogaranon.fortiddns.com:5827/app/inventarios/imagenes/"+product.getNombreCompleto()+".jpg";

        try {
            holder.txtCantidad = (TextView) v.findViewById(R.id.txtCantidadProductoPP);
            holder.checkBox = (CheckBox)v.findViewById(R.id.chckValidation);
            holder.imgProducto = (ImageView)v.findViewById(R.id.imgProducto);
            holder.titulo = (TextView)v.findViewById(R.id.txtNombreProducto);


            holder.checkBox.setOnCheckedChangeListener((PandP) context);
            holder.titulo.setText(product.getNombreCompleto());
            holder.txtCantidad.setText(""+product.getCantidadvalidada()/product.getConversion());

            holder.checkBox.setChecked(product.isChecked());
            holder.checkBox.setTag(product);
            //holder.imagen.setImageDrawable(LoadImageFromWebOperations(a));
            Glide.with(getContext()).load(a).asBitmap().override(200, 200).error(R.drawable.icon_trusot).fitCenter().into(holder.imgProducto);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();
        }


        return v;
    }


    @Nullable
    @Override
    public ProductosPyP getItem(int position) {
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