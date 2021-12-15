package ENTIDAD;

import android.animation.LayoutTransition;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.ListaTotales;
import com.example.desarrolloii.inventario.MainActivity;
import com.example.desarrolloii.inventario.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import DATOS.PicassoTrustAll;


import static com.example.desarrolloii.inventario.MainActivity.LoadImageFromWebOperations;
import static com.example.desarrolloii.inventario.MainActivity.cantidad;
import static com.example.desarrolloii.inventario.MainActivity.codigob;
import static com.example.desarrolloii.inventario.MainActivity.nuke;
import static com.example.desarrolloii.inventario.MainActivity.tipo;
import static com.example.desarrolloii.inventario.MainActivity.tipo1;

//en esta clase se maneja el adaptador de los items de la lista de totales


public class Totalesadapter extends ArrayAdapter<Total>{
    TextView Cantidad = null, cantidadpaquetes, cantidadunidad, txtDescripcion, txtTarimas;
    ImageView imageView = null;

    private List<Total> totals;
    public Totalesadapter( Context context, int resource, List<Total> objects) {
        super(context, resource, objects);
        this.totals = new ArrayList<Total>();
        this.totals.addAll(objects);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListaTotales.totals.size();
    }

    @Override
    public Total getItem(int position) {
        return this.totals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //este metodo se llama cuando se mete
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        nuke();
       // Total total = totals.get(position);
        Total total = totals.get(position);
        Producto producto = totals.get(position);
        View listViewItem = convertView;
        ViewHolder holder = null;

        if(listViewItem == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewItem = inflater.inflate(R.layout.cardtotales, parent,false);
            holder = new ViewHolder(listViewItem);
            listViewItem.setTag(holder);
        }else{
            holder = (ViewHolder) listViewItem.getTag();
        }

                Cantidad = (TextView) listViewItem.findViewById(R.id.Cantidadtotal);
                cantidadpaquetes = (TextView) listViewItem.findViewById(R.id.cantidadtotal);
                cantidadunidad = (TextView )listViewItem.findViewById(R.id.cantidadtotal2);
                txtDescripcion = (TextView) listViewItem.findViewById(R.id.txtDescripcion);
                txtTarimas = (TextView) listViewItem.findViewById(R.id.cantidadtotal4);

                txtDescripcion.setText(""+total.getDescripcion());

                try {

                    if (tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto") {
                        if(producto.getTipoproducto().toString() == "1" || producto.getTipoproducto().toString().equalsIgnoreCase("1")){
                            cantidadunidad.setText("Cajas: " + total.getCantida()/total.getConversion()/50);
                            cantidadpaquetes.setText("Paquetes: "+total.getCantida()/total.getConversion());
                        }else{
                            cantidadunidad.setText("Cajas: " + total.getCantida()/total.getConversion()/total.getUnidadenvio());
                            cantidadpaquetes.setText("Unidades: "+total.getCantida()/total.getConversion());
                        }
                        Log.d("cantidadAdapter", ""+total.getCantidadcaja()+" "+total.getCantida());
                    } else {
                        if(total.getCantida() == 0){
                            Log.d("tarimas 1", ""+total.getCantida());
                            txtTarimas.setText("Tarimas: "+total.getCantida());
                        }else{
                            Log.d("tarimas 2", ""+total.getCantida()/total.getConversion()/total.getCajastarima()/total.getUnidadenvio()+" cantidad" +total.getCantida() );
                            txtTarimas.setText("Tarimas: "+total.getCantida()/total.getConversion()/total.getCajastarima()/total.getUnidadenvio());
                        }


                        if(total.getCantidadcajaP()/total.getConversion()/ total.getUnidadenvio() == 0){
                            cantidadunidad.setText("Cajas: " + total.getCantidadcaja() / total.getUnidadenvio());
                        }else{
                            cantidadunidad.setText("Cajas: " + total.getCantidadcaja() / total.getConversion() / total.getUnidadenvio());
                        }

                        //cantidadpaquetes.setText("Paquetes 1: " + total.getCantidadpaquete());

                        if( total.getCantidadpaquete() == 0){
                            if((total.getCantidadpaquete() != 0 || total.getCantida() != 0 || total.getCantidadcajaP() != 0) && total.getTipoproducto().equals("1")){
                                cantidadpaquetes.setText("Paquetes: " + total.getCantidadpaquete());
                            }else{
                                cantidadpaquetes.setText("Paquetes: " + total.getCantidadpaquete());
                            }


                        }else{
                            if(total.getTipoproducto().equals("1")){
                                if(total.getCantidadpaquete() > 0 && total.getCantidadpaquete() < 10){
                                    cantidadpaquetes.setText("Paquetes: " + total.getCantidadpaquete());
                                }else{
                                    cantidadpaquetes.setText("Paquetes: " + total.getCantidadpaquete());
                                }

                            }else{
                                cantidadpaquetes.setText("Paquetes: " + total.getCantidadpaquete()/producto.getConversion());
                            }

                        }

                        Cantidad.setText("Unidades: " + total.getCantidadunidad());
                    }
                    //Toast.makeText(getContext(), "" + total.getCantidadtarima(), Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                String a = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/"+total.getDescripcion()+".jpg";
                try {
                    //holder.imagen.setImageDrawable(LoadImageFromWebOperations(a));
                    Glide.with(getContext()).load(a).override(200,200).fitCenter().error(R.drawable.icon_trusot).into(holder.imagen);

                }catch (Exception e){
                    e.printStackTrace();
                   // Toast.makeText(getContext(), ""+e, Toast.LENGTH_LONG).show();
                }

                return listViewItem;

        }

        class ViewHolder{
            public ImageView imagen;
            ViewHolder(View v){
                imagen = (ImageView)v.findViewById(R.id.imagentotal);
            }
        }




}
