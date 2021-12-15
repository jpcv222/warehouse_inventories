package ENTIDAD;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.desarrolloii.inventario.ListaProductos;
import com.example.desarrolloii.inventario.ListaTotales;
import com.example.desarrolloii.inventario.MainActivity;
import com.example.desarrolloii.inventario.R;
import com.example.desarrolloii.inventario.principal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DATOS.LogAndroid;
import NEGOCIO.cnProductos;

import static com.example.desarrolloii.inventario.ListaProductos.Dialogo;
import static com.example.desarrolloii.inventario.ListaProductos.actualizarItem;
import static com.example.desarrolloii.inventario.MainActivity.tipo;
import static com.example.desarrolloii.inventario.MainActivity.tipo1;
import static com.example.desarrolloii.inventario.login.setDate;


public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{

    List<Entrada> listProducto = new ArrayList<>();
    Context context;
    LogAndroid log = new LogAndroid();

    public Adaptador(List<Entrada> listProducto, Context context) {
        this.listProducto = listProducto;
        this.context = context;
    }

    @NonNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final Adaptador.ViewHolder holder, final int position) {
        final Entrada producto = listProducto.get(position);
        if(producto.getCantidadcaja() == 0){
            holder.TXTCantidad.setEnabled(false);
        }
        if(producto.getCantidadpaquete() == 0){
            holder.paquetes.setEnabled(false);
        }

        if(producto.getCantidadtarima() == 0){
            holder.edTarimas.setEnabled(false);
        }

        if(producto.getCantidadunidad() == 0){
            holder.unidades.setEnabled(false);
        }

        try {
            if (tipo.equalsIgnoreCase("Salida Producto") || tipo == "Salida Producto") {

                holder.txtcajas.setText("Total: ");
                holder.TXTCantidad.setText("" + producto.getCantida()/producto.getConversion());
                holder.TXTDescripcion.setText("" + producto.getDescripcion());
                holder.paquetes.setVisibility(View.GONE);
                holder.unidades.setVisibility(View.GONE);
                holder.txtpaquetes.setVisibility(View.GONE);
                holder.txtunidades.setVisibility(View.GONE);
                holder.btnRemoveTarimas.setVisibility(View.GONE);
                holder.btnRemovePaquetes.setVisibility(View.GONE);
                holder.btnRemoveUnidades.setVisibility(View.GONE);
                holder.btnActualizar.setVisibility(View.GONE);
                holder.edTarimas.setVisibility(View.GONE);
                holder.txttarimas.setText("Paquetes: ");

                Log.d("cantidades", ""+producto.getCantida()+" "+producto.getConversion());

            } else {
                holder.btnParcial.setVisibility(View.GONE);
                Log.d("tarima", ""+producto.getCantidadtarima());
                if (producto.getTipoproducto().equalsIgnoreCase("1")) {
                    holder.TXTCantidad.setText("" + producto.getCantidadcajaP() / 50 / producto.getConversion());

                    holder.paquetes.setText("" + producto.getCantidadpaquete() / producto.getConversion());
                    holder.unidades.setText("" + producto.getCantidadunidad());

                    if (producto.getCantidadtarima() == 0) {
                        Log.d("tarima2", ""+producto.getCantidadtarima());
                        holder.edTarimas.setText("" + producto.getCantidadtarima());
                    } else if (producto.getCantidadtarima() / 50 / producto.getConversion() == 1) {
                        Log.d("tarima3", ""+producto.getCantidadtarima());
                        holder.edTarimas.setText("0");
                    } else {
                        holder.edTarimas.setText("" + producto.getCantidadtarima() / 50 /producto.getConversion()/producto.getCajastarima());
                        Log.d("tarima4", ""+producto.getCantidadtarima());
                    }

                    Log.d("cantidadcaja", "" + producto.getCantidadcajaP());
                    Log.d("cantidadpaquete", "" + producto.getCantidadpaquete()+" "+producto.getConversion());
                    Log.d("cantidadunidad", "" + producto.getCantidadunidad());

                } else {
                    holder.edTarimas.setEnabled(false);
                    holder.paquetes.setEnabled(false);

                    Log.d("getcantidadcaja", ""+producto.getCantidadcaja()+" "+producto.getConversion());

                    if(producto.getCantidadcajaP() == 0){
                        holder.TXTCantidad.setText("0");
                    }else{
                        holder.TXTCantidad.setText(""+producto.getCantidadcaja()/producto.getConversion()/producto.getUenvio());
                    }

                    if(producto.getCantidadtarima() == 0){
                        holder.edTarimas.setText("0");
                    }else{
                        holder.edTarimas.setText(""+producto.getCantidadtarima()/producto.getCantidadtarima());
                    }
                    holder.paquetes.setText("" + producto.getCantidadpaquete() / producto.getConversion());
                    holder.unidades.setText("" + producto.getCantidadunidad());
                    Log.d("cantidadcaja13", "" + producto.getCantidadcajaP());
                    Log.d("cantidadpaquete14", "" + producto.getCantidadpaquete());
                    Log.d("cantidadunidad15", "" + producto.getCantidadunidad());

                }


                holder.TXTDescripcion.setText(producto.getDescripcion());
                holder.TXTFECHA.setText(hoy(producto.GetFecha()));
                holder.TXTCantidad.requestFocus();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
            //Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
        }

        holder.btnParcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnProductos productos = new cnProductos(v.getContext(), null);

                if(Integer.parseInt(holder.TXTCantidad.getText().toString()) > Integer.parseInt(String.valueOf(producto.getCantida())) / producto.getConversion()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Atención");
                    dialog.setMessage("No puedes dar salida al producto por una cantidad mayor de "+producto.getCantida()/producto.getConversion()+" por favor modifique la cantidad.");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            holder.TXTCantidad.setError("Modifica cantidad");
                        }
                    });
                    dialog.show();

                }else {
                    int valor = Integer.parseInt(holder.TXTCantidad.getText().toString());
                    int total;
                    total = valor * producto.getConversion();

                    if(productos.actualizarEntrada("" + producto.GetID(), ""+total)){
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Atención");
                        dialog.setMessage("Se actualizó correctamente el producto");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Dialogo.dismiss();
                                ListaTotales.adapter.notifyDataSetChanged();
                                actualizarItem();
                            }
                        });
                        dialog.show();
                    }else{
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Atención");
                        dialog.setMessage("No se pudo actualizar la cantidad del producto, intente de nuevo");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Dialogo.dismiss();
                            }
                        });
                        dialog.show();
                    }

                }
                //notifyDataSetChanged();



            }
        });

        holder.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnProductos productos = new cnProductos(context,null);
                int cantidadtarima = 0, resultadoTarima = 0, cantidadcaja = 0, resultadoCaja = 0, cantidadpaquete = 0, resultadoPaquete, cantidadunidad = 0;

                if(producto.getTipoproducto().equals("1")){

                    resultadoTarima = Integer.parseInt(holder.edTarimas.getText().toString());
                    cantidadtarima = resultadoTarima * producto.getConversion()*50*producto.getCajastarima();

                    resultadoCaja = Integer.parseInt(holder.TXTCantidad.getText().toString());
                    cantidadcaja = resultadoCaja * 50* producto.getConversion();

                    resultadoPaquete = Integer.parseInt(holder.paquetes.getText().toString());
                    cantidadpaquete = resultadoPaquete * producto.getConversion();


                    if(productos.BorrarEntrada(""+producto.GetID(), String.valueOf(cantidadunidad), String.valueOf(cantidadpaquete), String.valueOf(cantidadcaja), String.valueOf(cantidadtarima))){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("ATENCIÓN");
                        alertDialog.setMessage("Se actualizó correctamente las cantidades de los productos");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        alertDialog.show();
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("ATENCIÓN");
                        alertDialog.setMessage("No se pudo actualizar correctamente la cantidad, intente de nuevo.");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        alertDialog.show();
                    }
                }else{

                    resultadoTarima = Integer.parseInt(holder.edTarimas.getText().toString());
                    cantidadtarima = resultadoTarima * producto.getConversion()*producto.getCajastarima();

                    resultadoCaja = Integer.parseInt(holder.TXTCantidad.getText().toString());
                    cantidadcaja = resultadoCaja * producto.getUenvio();

                    resultadoPaquete = Integer.parseInt(holder.paquetes.getText().toString());
                    cantidadpaquete = resultadoPaquete * producto.getConversion();

                    if(productos.BorrarEntrada(""+producto.GetID(), String.valueOf(cantidadunidad), String.valueOf(cantidadpaquete), String.valueOf(cantidadcaja), String.valueOf(cantidadtarima))){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("ATENCIÓN");
                        alertDialog.setMessage("Se actualizó correctamente las cantidades de los productos");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        alertDialog.show();
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("ATENCIÓN");
                        alertDialog.setMessage("No se pudo actualizar correctamente la cantidad, intente de nuevo.");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        alertDialog.show();
                    }
                }

                notifyItemChanged(position);
                notifyDataSetChanged();
                ListaProductos.adaptador.notifyDataSetChanged();
                //ListaProductos.actualizarItem();

                String unidades = holder.unidades.getText().toString();
                String cajas = holder.TXTCantidad.getText().toString();
                String paquetes = holder.paquetes.getText().toString();
                String tarimas = holder.edTarimas.getText().toString();

                holder.unidades.setText(unidades);
                holder.TXTCantidad.setText(cajas);
                holder.paquetes.setText(paquetes);
                holder.edTarimas.setText(tarimas);



            }
        });


        String path = "https://corpogaranon.fortiddns.com:666/trusot/app/inventarios/imagenes/"+producto.getDescripcion()+".jpg";
        //String path = "https://corpogaranon.fortiddns.com:5827/app/inventarios/imagenes/"+producto.getDescripcion()+".jpg";
        try {
            //holder.imagen.setImageDrawable(LoadImageFromWebOperations(a));
            Glide.with(context).load(path).override(200,200).fitCenter().error(R.drawable.icon_trusot).into(holder.imagen);

        }catch (Exception e) {
            e.printStackTrace();
            log.appendLog(setDate(), ""+e);
        }
    }

    @Override
    public int getItemCount() {
        return listProducto.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public EditText paquetes, unidades, edTarimas;
        public TextView txtcajas, txtpaquetes, txtunidades, txttarimas;
        ImageView imagen;
        public TextView TXTDescripcion;
        public EditText TXTCantidad;
        public TextView TXTFECHA;
        public ImageView btnRemoveTarimas, btnRemoveCajas, btnRemovePaquetes, btnRemoveUnidades;
        public ImageView btnParcial ;
        public TextView txtParcial;
        public Button btnActualizar;

        public ViewHolder(View convertView) {
            super(convertView);

            TXTDescripcion = (TextView) convertView.findViewById(R.id.Descripcion);
            TXTCantidad = (EditText) convertView.findViewById(R.id.Cantidad);
            TXTFECHA = (TextView) convertView.findViewById(R.id.TXTFECHA2);
            //button = (ImageView) convertView.findViewById(R.id.buttonBorrar);
            btnRemoveCajas = (ImageView)convertView.findViewById(R.id.btnRemoveCajas);
            btnRemovePaquetes = (ImageView)convertView.findViewById(R.id.btnRemovePaquetes);
            btnRemoveTarimas = (ImageView)convertView.findViewById(R.id.btnRemoveTarimas);
            btnRemoveUnidades = (ImageView)convertView.findViewById(R.id.btnRemoveUnidades);

            btnParcial = (ImageView)convertView.findViewById(R.id.imageButton);
            txtParcial = (TextView)convertView.findViewById(R.id.textView7);

            imagen = (ImageView) convertView.findViewById(R.id.imagen);
            paquetes = (EditText)convertView.findViewById(R.id.cantidad);
            unidades = (EditText)convertView.findViewById(R.id.cantidad2);
            txtcajas = (TextView)convertView.findViewById(R.id.textView3);
            txtpaquetes = (TextView)convertView.findViewById(R.id.textView6);
            txtunidades = (TextView)convertView.findViewById(R.id.textView10);
            edTarimas = (EditText)convertView.findViewById(R.id.edTarimas);
            btnActualizar = (Button)convertView.findViewById(R.id.btnActualizar);
            txttarimas = (TextView)convertView.findViewById(R.id.textView11);

        }
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
                String a ="Hoy"+" "+new SimpleDateFormat("HH:mm:ss").format(date);
                return "Hoy"+" "+new SimpleDateFormat("HH:mm:ss").format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Resultado;
    }

    public static String setDate() {
        String fechahora = "";
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(" yyyy-MM-dd hh:mm:ss ");
        fechahora = formatter.format(today);

        return fechahora;
    }




}