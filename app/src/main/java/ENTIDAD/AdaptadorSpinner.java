package ENTIDAD;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desarrolloii.inventario.R;

import java.util.ArrayList;

public class AdaptadorSpinner extends ArrayAdapter<Pedidos> {

    Context context;
    ArrayList<Pedidos> objetos;
    public AdaptadorSpinner(Context context, int resource, ArrayList<Pedidos> objects) {
        super(context, R.layout.row_spinner_custom, objects);
        this.context = context;
        this.objetos = objects;
    }

    private static class ViewHolder{

        public TextView txtPedido;

    }


    @Override
    public View getDropDownView(int position,View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_spinner_custom, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtPedido = (TextView)view.findViewById(R.id.txtPedido);
        Pedidos pedidos = this.objetos.get(position);
        viewHolder.txtPedido.setText(pedidos.getIdpedidofinal());

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public Pedidos getItem(int position) {
        return super.getItem(position);
    }



    @Override
    public int getCount() {
        return this.objetos.size();
    }


}
