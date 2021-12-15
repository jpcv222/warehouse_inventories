package ENTIDAD;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.ListaProductos;
import com.example.desarrolloii.inventario.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DATOS.LogAndroid;
import NEGOCIO.cnProductos;

import static com.example.desarrolloii.inventario.MainActivity.LoadImageFromWebOperations;
import static com.example.desarrolloii.inventario.MainActivity.nuke;
import static com.example.desarrolloii.inventario.MainActivity.tipo;
import static com.example.desarrolloii.inventario.MainActivity.tipo1;
import static com.example.desarrolloii.inventario.login.setDate;

public class ProductosAdapter  extends ArrayAdapter<Entrada> {

    ImageView imagen = null;
    String descripcion;
    LogAndroid log = new LogAndroid();
    public  ProductosAdapter(Context context, int resource, List<Entrada> objects, String descripcion) {
        super(context, resource, objects);
        this.descripcion = descripcion;
    }

    EditText paquetes, unidades, edTarimas;
    TextView txtcajas, txtpaquetes, txtunidades;

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        nuke();
        final Entrada producto = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_product,parent,false);
        }

        TextView TXTDescripcion = (TextView) convertView.findViewById(R.id.Descripcion);
        final EditText TXTCantidad = (EditText) convertView.findViewById(R.id.Cantidad);
        TextView TXTFECHA = (TextView) convertView.findViewById(R.id.TXTFECHA2);
       // ImageView button = (ImageView) convertView.findViewById(R.id.buttonBorrar);
        ImageView btnParcial = (ImageView)convertView.findViewById(R.id.imageButton);
        TextView txtParcial = (TextView)convertView.findViewById(R.id.textView7);

        imagen = (ImageView) convertView.findViewById(R.id.imagen);
        paquetes = (EditText)convertView.findViewById(R.id.cantidad);
        unidades = (EditText)convertView.findViewById(R.id.cantidad2);
        txtcajas = (TextView)convertView.findViewById(R.id.textView3);
        txtpaquetes = (TextView)convertView.findViewById(R.id.textView6);
        txtunidades = (TextView)convertView.findViewById(R.id.textView10);
        edTarimas = (EditText)convertView.findViewById(R.id.edTarimas);

        //Toast.makeText(getContext(), ""+producto.getCantida(), Toast.LENGTH_LONG).show();

        Log.d("productotarima", ""+producto.getCantidadtarima());
        Log.d("productopaquete", ""+producto.getCantidadpaquete());
        Log.d("productounidad", ""+producto.getCantidadunidad());
        Log.d("productocaja", ""+producto.getCantidadcaja());
        try {
            if (tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto") {
                txtcajas.setText("Total: ");
                TXTCantidad.setText("" + producto.getCantida());
                TXTDescripcion.setText("" + producto.getDescripcion());
                paquetes.setVisibility(View.GONE);
                unidades.setVisibility(View.GONE);
                txtpaquetes.setVisibility(View.GONE);
                txtunidades.setVisibility(View.GONE);

            } else {
                btnParcial.setVisibility(View.GONE);

                if (tipo1.equalsIgnoreCase("1")) {
                    TXTCantidad.setText("" + producto.getCantidadcajaP() / 50 / producto.getConversion());

                    paquetes.setText("" + producto.getCantidadpaquete() / 10);
                    unidades.setText("" + producto.getCantidadunidad());
                    if (producto.getCantidadtarima() == 0) {
                        edTarimas.setText("" + producto.getCantidadtarima());
                    } else if (producto.getCantidadtarima() / 50 / producto.getConversion() == 1) {
                        edTarimas.setText("0");
                    } else {
                        edTarimas.setText("" + producto.getCantidadtarima() / producto.getCantidadtarima());
                    }

                } else {
                    Log.d("getcantidadcaja", ""+producto.getCantidadcajaP());

                    if(producto.getCantidadcajaP() == 0){
                        TXTCantidad.setText("0");
                    }else{
                        TXTCantidad.setText(""+producto.getCantidadcajaP()/producto.getCantidadcajaP());
                    }

                    if(producto.getCantidadtarima() == 0){
                        edTarimas.setText("0");
                    }else{
                        edTarimas.setText(""+producto.getCantidadtarima()/producto.getCantidadtarima());
                    }
                        paquetes.setText("" + producto.getCantidadpaquete() / 10);
                        unidades.setText("" + producto.getCantidadunidad());
                        Log.d("cantidadcaja13", "" + producto.getCantidadcajaP());
                        Log.d("cantidadpaquete14", "" + producto.getCantidadpaquete());
                        Log.d("cantidadunidad15", "" + producto.getCantidadunidad());

                }


                TXTDescripcion.setText(producto.getDescripcion());
                TXTFECHA.setText(hoy(producto.GetFecha()));
                TXTCantidad.requestFocus();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);

            //Toast.makeText(getContext(), ""+e, Toast.LENGTH_LONG).show();
        }




       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cnProductos productos = new cnProductos(v.getContext(),null);
                //productos.BorrarEntrada(""+producto.GetID());
                Log.d("ids", ""+producto.GetID());
                Toast.makeText(getContext(), "Hola xd ", Toast.LENGTH_LONG).show();

            }
        });*/

        btnParcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnProductos productos = new cnProductos(v.getContext(), null);

                if(Integer.parseInt(TXTCantidad.getText().toString()) > Integer.parseInt(String.valueOf(producto.getCantida()))){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Atención");
                    dialog.setMessage("No puedes dar salida al producto por una cantidad mayor de "+producto.getCantida()+" por favor modifique la cantidad.");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            TXTCantidad.setError("Modifica cantidad");
                        }
                    });
                    dialog.show();

                }else {

                    productos.actualizarEntrada("" + producto.GetID(), TXTCantidad.getText().toString());
                    ListaProductos productos1 = new ListaProductos();
                    productos1.actualizarItem();
                   // Toast.makeText(getContext(), "ID: " + producto.GetID() + " Cantidad: " + TXTCantidad.getText().toString() + "Cantidad producto: " + producto.getCantida(), Toast.LENGTH_LONG).show();
                    TXTCantidad.setText("");

                }
            }
        });

        String path = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/"+producto.getDescripcion()+".jpg";
        try {
            //holder.imagen.setImageDrawable(LoadImageFromWebOperations(a));
            Glide.with(getContext()).load(path).override(200,200).fitCenter().error(R.drawable.icon_trusot).into(imagen);

        }catch (Exception e) {
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
        return convertView;
    }


    public String hoy(String fecha){
        String Resultado = fecha;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(fecha);
            Date date1 = new Date();
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (calendar.get(Calendar.DAY_OF_YEAR)==calendar1.get(Calendar.DAY_OF_YEAR)){
                String a ="HOY"+" "+new SimpleDateFormat("HH:mm:ss").format(date);
                return "HOY"+" "+new SimpleDateFormat("HH:mm:ss").format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Resultado;
    }


}
