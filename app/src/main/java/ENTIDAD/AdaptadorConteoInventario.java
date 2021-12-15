package ENTIDAD;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.R;

import java.util.ArrayList;

public class AdaptadorConteoInventario extends ArrayAdapter<InventarioModelo> {
    Context context;
    public static ArrayList<InventarioModelo> listado;

    public AdaptadorConteoInventario(@NonNull Context context, int resource, @NonNull ArrayList<InventarioModelo> listado) {
        super(context, resource, listado);
        this.context = context;
        this.listado = listado;
    }


    private class ItemsConteo{
        public TextView nombreProducto;
        public EditText edCantidad;
        public ImageView imgProducto;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_conteo_inventario, null);
        }else{
            view = convertView;
        }
        final ItemsConteo holder = new ItemsConteo();
        InventarioModelo modelo = this.listado.get(position);

        holder.nombreProducto = (TextView)view.findViewById(R.id.txtProductoInventario);
        holder.edCantidad = (EditText)view.findViewById(R.id.edConteo);
        holder.imgProducto = (ImageView)view.findViewById(R.id.imgProductoInventario);

        holder.nombreProducto.setText(modelo.getProducto());

        holder.edCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //listado.get(position).setCantidad_fisica(Integer.parseInt(holder.edCantidad.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("Data", holder.edCantidad.getText().toString());
            }
        });

        String a = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/" + modelo.getProducto() + ".jpg";
        try{
            Glide.with(context).load(a).asBitmap().error(R.drawable.icon_trusot).override(150,150).into(holder.imgProducto);
        }catch (Exception e){
            e.printStackTrace();
        }



        return view;
    }

    @Override
    public int getCount() {
        return this.listado.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }


    @Nullable
    @Override
    public InventarioModelo getItem(int position) {
        return super.getItem(position);
    }
}
