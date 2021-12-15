package ENTIDAD;
import android.content.Context;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.example.desarrolloii.inventario.R;

import java.util.ArrayList;

public class AdapterPaqueterasSp extends ArrayAdapter<PaqueterasModelo> {

    Context context;
    ArrayList<PaqueterasModelo> objetos;
    public AdapterPaqueterasSp(Context context, int resource, ArrayList<PaqueterasModelo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objetos = objects;
    }

    private static class ViewHolder{

        public TextView txtPaquetera;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_paquetera, parent, false);
        PaqueterasModelo modelo = this.objetos.get(position);
        TextView text1 = (TextView) convertView.findViewById(R.id.txtPaqueteraAdpter);
        text1.setText(modelo.getPaquetera());

        return convertView;
    }


    @Override
    public View getDropDownView(int position,View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_paquetera, parent, false);
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.txtPaquetera = (TextView)view.findViewById(R.id.txtPaqueteraAdpter);
        PaqueterasModelo paquetera = this.objetos.get(position);
        viewHolder.txtPaquetera.setText(paquetera.getPaquetera());

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
    public PaqueterasModelo getItem(int position) {
        return super.getItem(position);
    }



    @Override
    public int getCount() {
        return this.objetos.size();
    }


}
