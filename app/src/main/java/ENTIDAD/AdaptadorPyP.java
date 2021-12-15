package ENTIDAD;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.R;

import java.util.ArrayList;

public class AdaptadorPyP extends RecyclerView.Adapter<AdaptadorPyP.MyViewHolder> {
    Context context;
    ArrayList<ProductosPyP> listado;

    public AdaptadorPyP(Context context, ArrayList<ProductosPyP> listado){
        this.context = context;
        this.listado = listado;
    }

    @NonNull
    @Override
    public AdaptadorPyP.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_productospandp, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPyP.MyViewHolder holder, int position) {
        ProductosPyP modelo = this.listado.get(position);
        holder.txtNombreProducto.setText(modelo.getNombreCompleto());
        holder.txtCantidadProductosPP.setText(""+modelo.getCantidadvalidada()/modelo.getConversion());

        String path = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/"+modelo.getNombreCompleto()+".jpg";
        //String path = "https://corpogaranon.fortiddns.com:5827/app/inventarios/imagenes/"+modelo.getNombreCompleto()+".jpg";
        try {
            //holder.imagen.setImageDrawable(LoadImageFromWebOperations(a));
            Glide.with(context).load(path).asBitmap().override(200,200).fitCenter().error(R.drawable.icon_trusot).into(holder.imgProducto);

        }catch (Exception e) {
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

        TextView txtNombreProducto, txtCantidadProductosPP;
        ImageView imgProducto;
        public MyViewHolder(View itemView) {
            super(itemView);

            txtCantidadProductosPP = (TextView) itemView.findViewById(R.id.txtCantidadProductoPP);

            imgProducto = (ImageView)itemView.findViewById(R.id.imgProducto);
            txtNombreProducto = (TextView)itemView.findViewById(R.id.txtNombreProducto);
        }
    }
}
