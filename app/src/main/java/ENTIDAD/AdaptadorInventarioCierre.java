package ENTIDAD;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.R;
import java.util.ArrayList;

public class AdaptadorInventarioCierre extends RecyclerView.Adapter<AdaptadorInventarioCierre.MyViewHolder> {
    Context context;
    public static ArrayList<InventarioModelo> listado;

    public AdaptadorInventarioCierre(Context context, ArrayList<InventarioModelo> listado){
        this.context = context;
        this.listado = listado;
    }

    @NonNull
    @Override
    public AdaptadorInventarioCierre.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conteo_inventario, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInventarioCierre.MyViewHolder holder, int position) {
        InventarioModelo modelo = this.listado.get(position);
        String path = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/"+modelo.getProducto()+".jpg";
        try{
            holder.txtProductoInventario.setText(modelo.getProducto());
            Glide.with(context).load(path).asBitmap().error(R.drawable.icon_trusot).into(holder.imgProductoInventario);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.listado.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText edCantidad;
        TextView txtProductoInventario;
        ImageView imgProductoInventario;
        public MyViewHolder(View itemView) {
            super(itemView);
            edCantidad = (EditText)itemView.findViewById(R.id.edConteo);
            txtProductoInventario = (TextView)itemView.findViewById(R.id.txtProductoInventario);
            imgProductoInventario = (ImageView)itemView.findViewById(R.id.imgProductoInventario);

            edCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try{
                        listado.get(getAdapterPosition()).setCantidadconteo(Integer.parseInt(edCantidad.getText().toString()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}
